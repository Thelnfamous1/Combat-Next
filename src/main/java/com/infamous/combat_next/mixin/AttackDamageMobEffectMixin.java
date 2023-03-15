package com.infamous.combat_next.mixin;

import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttackDamageMobEffect.class)
public abstract class AttackDamageMobEffectMixin extends MobEffect {

    protected AttackDamageMobEffectMixin(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Inject(method = "getAttributeModifierValue", at = @At("HEAD"), cancellable = true)
    private void handleGetAttributeModifierValue(int level, AttributeModifier modifier, CallbackInfoReturnable<Double> cir){
        if(this == MobEffects.DAMAGE_BOOST){
            cir.setReturnValue(super.getAttributeModifierValue(level, modifier));
        }
    }
}
