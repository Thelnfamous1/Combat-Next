package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MagicCombatConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffect.class)
public class MobEffectMixin {

    /*
    @ModifyConstant(method = "applyEffectTick", constant = @Constant(intValue = 4, ordinal = 0))
    private int getTickBaseHeal(int constant){
        return MagicCombatConfigs.getInstantEffectBaseHealAmount().get();
    }
     */

    @ModifyExpressionValue(method = "applyEffectTick", at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0))
    private int modifyTickBaseHeal(int constant){
        return MagicCombatConfigs.getInstantEffectBaseHealAmount().get();
    }

    /*
    @ModifyConstant(method = "applyInstantenousEffect", constant = @Constant(intValue = 4, ordinal = 0))
    private int getInstantBaseHeal(int constant){
        return MagicCombatConfigs.getInstantEffectBaseHealAmount().get();
    }
     */

    @ModifyExpressionValue(method = "applyInstantenousEffect", at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0))
    private int modifyInstantBaseHeal(int constant){
        return MagicCombatConfigs.getInstantEffectBaseHealAmount().get();
    }
}
