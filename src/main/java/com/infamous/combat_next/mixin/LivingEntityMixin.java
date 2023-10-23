package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.BugFixConfigs;
import com.infamous.combat_next.config.GeneralCombatConfigs;
import com.infamous.combat_next.config.ShieldCombatConfigs;
import com.infamous.combat_next.config.ShieldCombatValues;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    @Shadow public abstract void knockback(double p_147241_, double p_147242_, double p_147243_);

    @Shadow public abstract ItemStack getUseItem();

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    /*
    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5, ordinal = 0))
    int getShieldWarmUpDelay(int vanilla){
        if(ShieldCombatConfigs.getShieldWarmUpDelayChange().get()){
            return ShieldCombatValues.getWarmUpDelay(this.getUseItem().getItem()).orElse(vanilla);
        }
        return vanilla;
    }
     */

    @ModifyExpressionValue(method = "isBlocking", at = @At(value = "CONSTANT", args = "intValue=5", ordinal = 0))
    int getShieldWarmUpDelay(int vanilla){
        if(ShieldCombatConfigs.getShieldWarmUpDelayChange().get()){
            return ShieldCombatValues.getWarmUpDelay(this.getUseItem().getItem()).orElse(vanilla);
        }
        return vanilla;
    }

    /*
    @ModifyConstant(method = "isDamageSourceBlocked", constant = @Constant(doubleValue = 0.0D, ordinal = 1))
    double getMaxDotProduct(double vanilla){
        if(ShieldCombatConfigs.getShieldProtectionArcChange().get()){
            Optional<Float> protectionArc = ShieldCombatValues.getProtectionArc(this.getUseItem().getItem());
            if(protectionArc.isPresent()){
                return Mth.cos(protectionArc.get() * (Mth.PI / 360.0F)) * -1.0D;
            }
        }
        return vanilla;
    }
     */

    @ModifyExpressionValue(method = "isDamageSourceBlocked", at = @At(value = "CONSTANT", args = "doubleValue=0.0D", ordinal = 1))
    double modifyMaxDotProduct(double vanilla){
        if(ShieldCombatConfigs.getShieldProtectionArcChange().get()){
            Optional<Float> protectionArc = ShieldCombatValues.getProtectionArc(this.getUseItem().getItem());
            if(protectionArc.isPresent()){
                return Mth.cos(protectionArc.get() * (Mth.PI / 360.0F)) * -1.0D;
            }
        }
        return vanilla;
    }

    @Inject(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"))
    private void handleKnockbackHurtMarked(double strength, double ratioX, double ratioZ, CallbackInfo ci){
        if(BugFixConfigs.fixShieldUserKnockback.get()){
            // Fix MC-223238 and MC-248310 by setting the victim to hurtMarked
            this.markHurt();
        }
    }

    @Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
    private void fixBlockedByShield(LivingEntity defender, CallbackInfo ci){
        if(BugFixConfigs.fixShieldAttackerKnockback.get()){
            ci.cancel();
            // Fix MC-147694 by switching attacker and defender
            this.knockback(0.5D, defender.getX() - this.getX(), defender.getZ() - this.getZ());
        }
    }

    /*
    @ModifyConstant(method = "hurt", constant = @Constant(floatValue = 10.0F, ordinal = 0))
    private float getTicksLeftBeforeDamageable(float constant){
        return GeneralCombatConfigs.getIFramesLeftBeforeDamageable().get();
    }
     */

    @ModifyExpressionValue(method = "hurt", at = @At(value = "CONSTANT", args = "floatValue=10.0F", ordinal = 0))
    private float modifyTicksLeftBeforeDamageable(float constant){
        return GeneralCombatConfigs.getIFramesLeftBeforeDamageable().get();
    }

}
