package com.infamous.combat_next.mixin;

import net.minecraft.world.entity.ai.behavior.RamTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RamTarget.class)
public class RamTargetMixin {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V",
    at = @At(value = "STORE", ordinal = 0), ordinal = 2)
    private float dontReduceKnockbackIfShielded(float original){
        return 1.0F; // we already control shielded knockback during LivingKnockbackEvent
    }
}
