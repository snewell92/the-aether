package com.aetherteam.aether.item.tools.zanite;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ZaniteTool;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Zanite mining speed boost behavior is called by {@link com.aetherteam.aether.event.listeners.abilities.ToolAbilityListener#modifyBreakSpeed(PlayerEvent.BreakSpeed)}.
 */
public class ZaniteHoeItem extends HoeItem implements ZaniteTool {
    public ZaniteHoeItem() {
        super(AetherItemTiers.ZANITE, new Item.Properties().attributes(HoeItem.createAttributes(AetherItemTiers.ZANITE, -2.0F, -1.0F)));
    }
}
