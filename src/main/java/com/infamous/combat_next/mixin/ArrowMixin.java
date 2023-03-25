package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MagicCombatConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

@Mixin(Arrow.class)
public abstract class ArrowMixin extends AbstractArrow {

    protected ArrowMixin(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    /*
    @Redirect(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0))
    private boolean handleInstantEffects(LivingEntity target, MobEffectInstance effectInstance, @Nullable Entity source){
        if(effectInstance.getEffect().isInstantenous()){
            effectInstance.getEffect().applyInstantenousEffect(this, this.getOwner(), target, effectInstance.getAmplifier(), MagicCombatConfigs.getArrowInstantEffectScale().get());
            return true;
        } else{
            return target.addEffect(new MobEffectInstance(effectInstance.getEffect(), Math.max(effectInstance.getDuration() / 8, 1), effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()), source);
        }
    }
     */

    @WrapOperation(method = "doPostHurtEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0))
    private boolean handleInstantEffects(LivingEntity target, MobEffectInstance effectInstance, @Nullable Entity source, Operation<Boolean> original){
        if(effectInstance.getEffect().isInstantenous() && MagicCombatConfigs.getArrowInstantEffectChange().get()){
            effectInstance.getEffect().applyInstantenousEffect(this, this.getOwner(), target, effectInstance.getAmplifier(), MagicCombatConfigs.getArrowInstantEffectScale().get());
            return true;
        } else{
            return original.call(target, effectInstance, source);
        }
    }
}
