package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MagicCombatConfigs;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttackDamageMobEffect.class)
public abstract class AttackDamageMobEffectMixin extends MobEffect {

    protected AttackDamageMobEffectMixin(MobEffectCategory category, int color) {
        super(category, color);
    }

    /*
    @Inject(method = "getAttributeModifierValue", at = @At("HEAD"), cancellable = true)
    private void handleGetAttributeModifierValue(int level, AttributeModifier modifier, CallbackInfoReturnable<Double> cir){
        if(this == MobEffects.DAMAGE_BOOST && MagicCombatConfigs.getStrengthEffectChange().get()){
            cir.setReturnValue(super.getAttributeModifierValue(level, modifier));
        }
    }
     */

    @ModifyReturnValue(method = "getAttributeModifierValue", at = @At("RETURN"))
    private double modifyGetAttributeModifierValue(double original, int level, AttributeModifier modifier){
        if(this == MobEffects.DAMAGE_BOOST && MagicCombatConfigs.getStrengthEffectChange().get()){
            return super.getAttributeModifierValue(level, modifier);
        } else{
            return original;
        }
    }
}
