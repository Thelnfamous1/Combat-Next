package com.infamous.combat_next.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Item.class)
public interface ItemAccessor {

    @Accessor
    static UUID getBASE_ATTACK_DAMAGE_UUID() {
        throw new AssertionError();
    }

    @Accessor
    static UUID getBASE_ATTACK_SPEED_UUID() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor
    void setMaxStackSize(int maxStackSize);

}
