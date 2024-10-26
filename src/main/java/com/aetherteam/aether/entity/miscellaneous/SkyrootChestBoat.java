package com.aetherteam.aether.entity.miscellaneous;

import com.aetherteam.aether.entity.AetherEntityTypes;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SkyrootChestBoat extends ChestBoat {
    public SkyrootChestBoat(EntityType<? extends SkyrootChestBoat> entityType, Level level) {
        super(entityType, level);
        this.setVariant(SkyrootBoat.SKYROOT);
    }

    public SkyrootChestBoat(Level level, double x, double y, double z) {
        this(AetherEntityTypes.SKYROOT_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }
}
