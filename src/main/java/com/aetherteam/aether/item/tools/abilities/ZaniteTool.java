package com.aetherteam.aether.item.tools.abilities;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.item.EquipmentUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.concurrent.atomic.AtomicReference;

public interface ZaniteTool {
    ResourceLocation MINING_EFFICIENCY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "zanite_tool_mining_efficiency");

    /**
     * Calculates mining speed increase using the default mining speed inputted into the Zanite value buff function.
     *
     * @param modifiers The originak {@link ItemAttributeModifiers}.
     * @param stack The {@link ItemStack} being used for mining.
     * @param baseValue The mining speed of the stack as a {@link Double}.
     * @return The buffed {@link ItemAttributeModifiers.Entry} for the mining speed of the zanite tool.
     */
    default ItemAttributeModifiers.Entry increaseSpeed(ItemAttributeModifiers modifiers, ItemStack stack, double baseValue) {
        return new ItemAttributeModifiers.Entry(Attributes.MINING_EFFICIENCY,
            new AttributeModifier(MINING_EFFICIENCY_MODIFIER_ID, this.calculateSpeedIncrease(Attributes.MINING_EFFICIENCY, baseValue, MINING_EFFICIENCY_MODIFIER_ID, modifiers, stack), AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND);
    }

    default double calculateSpeedIncrease(Holder<Attribute> base, double baseValue, ResourceLocation bonusModifier, ItemAttributeModifiers modifiers, ItemStack stack) {
        AtomicReference<Double> baseStat = new AtomicReference<>(baseValue);
        modifiers.forEach(EquipmentSlotGroup.MAINHAND, (attribute, modifier) -> {
            if (attribute.value() == base.value() && !modifier.id().equals(bonusModifier)) {
                baseStat.updateAndGet(v -> v + modifier.amount());
            }
        });
        return EquipmentUtil.calculateZaniteBuff(stack, baseStat.get()) - baseStat.get();
    }
}
