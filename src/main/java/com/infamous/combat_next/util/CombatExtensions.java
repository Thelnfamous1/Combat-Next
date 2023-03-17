package com.infamous.combat_next.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface CombatExtensions{

    static CombatExtensions cast(LivingEntity livingEntity){
        return (CombatExtensions) livingEntity;
    }

    DamageSource getLastBlockedDamageSource();

    void setLastBlockedDamageSource(DamageSource source);
}
