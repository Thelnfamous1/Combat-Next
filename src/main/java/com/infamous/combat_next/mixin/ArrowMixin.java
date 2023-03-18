package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Arrow.class)
public abstract class ArrowMixin extends AbstractArrow {

    protected ArrowMixin(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0))
    private boolean handleInstantEffects(LivingEntity target, MobEffectInstance effectInstance, @Nullable Entity source){
        if(effectInstance.getEffect().isInstantenous()){
            effectInstance.getEffect().applyInstantenousEffect(this, this.getOwner(), target, effectInstance.getAmplifier(), CombatUtil.INSTANT_ARROW_EFFECT_MULTIPLIER);
            return true;
        } else{
            return target.addEffect(new MobEffectInstance(effectInstance.getEffect(), Math.max(effectInstance.getDuration() / 8, 1), effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()), source);
        }
    }
}
