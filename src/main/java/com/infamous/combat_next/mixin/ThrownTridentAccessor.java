package com.infamous.combat_next.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThrownTrident.class)
public interface ThrownTridentAccessor {

    @Accessor
    void setTridentItem(ItemStack stack);

    @Accessor
    static EntityDataAccessor<Byte> getID_LOYALTY() {
        throw new AssertionError();
    }

    @Accessor
    static EntityDataAccessor<Boolean> getID_FOIL() {
        throw new AssertionError();
    }
}
