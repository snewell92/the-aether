package com.aetherteam.aether.mixin;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.item.accessories.cape.CapeItem;
import com.aetherteam.aether.item.accessories.gloves.GlovesItem;
import com.aetherteam.aether.item.accessories.pendant.PendantItem;
import com.aetherteam.aether.mixin.mixins.common.accessor.MinecraftServerAccessor;
import com.mojang.datafixers.util.Pair;
import io.wispforest.accessories.Accessories;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.slot.SlotTypeReference;
import io.wispforest.accessories.impl.ExpandedSimpleContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.nio.file.Path;
import java.util.stream.StreamSupport;

public class AetherMixinHooks {
    private static final ResourceLocation SWUFF_CAPE_LOCATION = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "textures/models/accessory/capes/swuff_accessory.png");

    /**
     * Checks whether a cape accessory is visible.
     *
     * @param livingEntity The {@link LivingEntity} wearing the cape.
     * @return Whether the cape is visible, as a {@link Boolean}.
     * @see com.aetherteam.aether.mixin.mixins.client.PlayerSkinMixin
     */
    public static ItemStack isCapeVisible(LivingEntity livingEntity) {
        AccessoriesCapability accessories = AccessoriesCapability.get(livingEntity);
        if (accessories != null) {
            AccessoriesContainer accessoriesContainer = accessories.getContainer(CapeItem.getStaticIdentifier());

            if (accessoriesContainer != null) {
                ExpandedSimpleContainer simpleAccessoriesContainer = accessoriesContainer.getAccessories();
                ExpandedSimpleContainer simpleCosmeticsContainer = accessoriesContainer.getCosmeticAccessories();

                Pair<Integer, ItemStack> stack = StreamSupport.stream(simpleAccessoriesContainer.spliterator(), true).findFirst().orElse(null);
                Pair<Integer, ItemStack> cosmeticStack = StreamSupport.stream(simpleCosmeticsContainer.spliterator(), true).findFirst().orElse(null);
                if (cosmeticStack != null && !cosmeticStack.getSecond().isEmpty() && Accessories.config().clientOptions.showCosmeticAccessories()) {
                    stack = cosmeticStack;
                }
                if (stack != null) {
                    if (accessoriesContainer.shouldRender(stack.getFirst())) {
                        return stack.getSecond();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Gets the cape texture from a {@link CapeItem}.
     *
     * @param stack The {@link ItemStack}.
     * @return The {@link ResourceLocation} texture from the cape.
     */
    public static ResourceLocation getCapeTexture(ItemStack stack) {
        if (stack.getItem() instanceof CapeItem capeItem) {
            if (stack.getHoverName().getString().equalsIgnoreCase("swuff_'s cape")) { // Easter Egg cape texture.
                return SWUFF_CAPE_LOCATION;
            } else {
                return capeItem.getCapeTexture();
            }
        }
        return null;
    }

    /**
     * Checks whether the {@link SelectWorldScreen} is open and the level that the lock belongs to is the same one as the level loaded by the world preview.
     *
     * @param basePath The {@link Path} for the level directory.
     * @return Whether the level can be unlocked, as a {@link Boolean}.
     * @see com.aetherteam.aether.mixin.mixins.common.DirectoryLockMixin
     */
    public static boolean canUnlockLevel(Path basePath) {
        if (Minecraft.getInstance().screen != null && Minecraft.getInstance().screen instanceof SelectWorldScreen && Minecraft.getInstance().getSingleplayerServer() != null) {
            return basePath.getFileName().toString().equals(((MinecraftServerAccessor) Minecraft.getInstance().getSingleplayerServer()).aether$getStorageSource().getLevelId());
        }
        return false;
    }

    /**
     * Whether an accessory can be equipped or replace an already equipped accessory.
     *
     * @param mob       The {@link Mob} to equip the accessory to.
     * @param candidate The {@link ItemStack} to try to equip.
     * @param existing  The {@link ItemStack} already equipped.
     * @return Whether the accessory can be equipped or replaced, as a {@link Boolean}.
     */
    public static boolean canReplaceCurrentAccessory(Mob mob, ItemStack candidate, ItemStack existing) {
        if (EnchantmentHelper.hasAnyEnchantments(existing)) {
            return false;
        } else {
            if (candidate.getItem() instanceof GlovesItem candidateGloves) {
                if (!(existing.getItem() instanceof GlovesItem existingGloves)) {
                    return true;
                } else {
                    if (candidateGloves.getDamage() != existingGloves.getDamage()) {
                        return candidateGloves.getDamage() > existingGloves.getDamage();
                    } else {
                        return mob.canReplaceEqualItem(candidate, existing);
                    }
                }
            } else if (candidate.getItem() instanceof PendantItem) {
                if (!(existing.getItem() instanceof PendantItem)) {
                    return true;
                } else {
                    return mob.canReplaceEqualItem(candidate, existing);
                }
            }
        }
        return false;
    }

    /**
     * Gets the corresponding slot identifier for an accessory item.
     *
     * @param livingEntity The {@link LivingEntity} to get the accessory from.
     * @param stack        The accessory {@link ItemStack}.
     * @return The slot identifier {@link String}.
     */
    public static SlotTypeReference getIdentifierForItem(LivingEntity livingEntity, ItemStack stack) {
        if (stack.getItem() instanceof GlovesItem glovesItem) {
            return glovesItem.getIdentifier();
        } else if (stack.getItem() instanceof PendantItem pendantItem && (livingEntity.getType() == EntityType.PIGLIN || livingEntity.getType() == EntityType.ZOMBIFIED_PIGLIN)) {
            return pendantItem.getIdentifier();
        }
        return null;
    }

    /**
     * Gets an accessory from an entity.
     *
     * @param livingEntity The {@link LivingEntity} to get the accessory from.
     * @param identifier The {@link SlotTypeReference} for the slot identifier.
     * @return The accessory {@link ItemStack} gotten from the entity.
     */
    public static ItemStack getItemByIdentifier(LivingEntity livingEntity, SlotTypeReference identifier) {
        AccessoriesCapability accessories = AccessoriesCapability.get(livingEntity);
        if (accessories != null) {
            AccessoriesContainer accessoriesContainer = accessories.getContainer(identifier);
            if (accessoriesContainer != null) {
                return accessoriesContainer.getAccessories().getItem(0);
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Equips an accessory to an entity.
     *
     * @param livingEntity The {@link LivingEntity} to equip to.
     * @param itemStack    The {@link ItemStack} to equip.
     * @param identifier   The {@link SlotTypeReference} for the slot identifier.
     */
    public static void setItemByIdentifier(LivingEntity livingEntity, ItemStack itemStack, SlotTypeReference identifier) {
        AccessoriesCapability accessories = AccessoriesCapability.get(livingEntity);
        if (accessories != null) {
            AccessoriesContainer accessoriesContainer = accessories.getContainer(identifier);
            if (accessoriesContainer != null) {
                accessoriesContainer.getAccessories().setItem(0, itemStack);
            }
        }
    }
}
