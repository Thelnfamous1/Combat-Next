package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.ShieldCombatConfigs;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RamTarget.class)
public class RamTargetMixin {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V",
    at = @At(value = "STORE", ordinal = 0), ordinal = 2)
    private float modifyRamKnockbackScale(float original){
        if(ShieldCombatConfigs.getShieldGoatRamFullKnockback().get()){
            return 1.0F;
        } else{
            return original;
        }
    }
}
