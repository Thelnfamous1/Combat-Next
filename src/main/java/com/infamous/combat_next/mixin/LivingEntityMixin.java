package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatExtensions;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements CombatExtensions {
    private DamageSource lastBlockedDamageSource;
    private long lastBlockedDamageStamp;

    @Shadow public abstract void knockback(double p_147241_, double p_147242_, double p_147243_);

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5, ordinal = 0))
    int getShieldWarmUpDelay(int vanilla){
        return 0;
    }

    @ModifyConstant(method = "isDamageSourceBlocked", constant = @Constant(doubleValue = 0.0D, ordinal = 1))
    double getShieldArc(double vanilla){
        return CombatUtil.SHIELD_ARC;
    }

    @Inject(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"))
    private void handleKnockbackHurtMarked(double strength, double ratioX, double ratioZ, CallbackInfo ci){
        // Fix MC-223238 and MC-248310 by setting the victim to hurtMarked
        this.markHurt();
    }

    @Inject(method = "blockedByShield", at = @At("RETURN"), cancellable = true)
    private void fixBlockedByShield(LivingEntity defender, CallbackInfo ci){
        ci.cancel();
        // Fix MC-147694 by switching attacker and defender
        this.knockback(0.5D, this.getX() - defender.getX(), this.getZ() - defender.getZ());
    }

    @ModifyConstant(method = "hurt", constant = @Constant(floatValue = 10.0F, ordinal = 0))
    private float getTicksLeftBeforeDamageable(float constant){
        return 0.0F;
    }

    @Override
    public DamageSource getLastBlockedDamageSource() {
        if (this.level.getGameTime() - this.lastBlockedDamageStamp > 40L) {
            this.lastBlockedDamageSource = null;
        }

        return this.lastBlockedDamageSource;
    }

    @Override
    public void setLastBlockedDamageSource(DamageSource source) {
        this.lastBlockedDamageSource = source;
        this.lastBlockedDamageStamp = this.level.getGameTime();
    }
}
