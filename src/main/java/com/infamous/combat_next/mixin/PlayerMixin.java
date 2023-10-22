package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.GeneralCombatConfigs;
import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.config.ShieldCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.util.PlayerCombat;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerCombat {

    private float currentDamageBonus;
    @Nullable
    private CriticalHitEvent criticalHitEvent;
    private int missedAttackRecovery = -1;

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "blockUsingShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;disableShield(Z)V"), cancellable = true)
    private void handleDisableShield(LivingEntity attacker, CallbackInfo ci){
        if(ShieldCombatConfigs.getShieldDisableChange().get()){
            ci.cancel();
            CombatUtil.newDisableShield((Player) (Object)this, attacker);
        }
    }

    /*
    @ModifyConstant(method = "hurt", constant = @Constant(floatValue = 0.0F, ordinal = 1))
    private float bypassZeroDamage(float constant, DamageSource source, float damage){
        if(GeneralCombatConfigs.getPlayersAlwaysHurt().get()){
            return Float.MIN_VALUE; // impossible to achieve, so guaranteed to fail
        } else{
            return constant;
        }
    }
     */

    @ModifyExpressionValue(method = "hurt", at = @At(value = "CONSTANT", args = "floatValue=0.0F", ordinal = 1))
    private float bypassZeroDamage(float constant, DamageSource source, float damage){
        if(GeneralCombatConfigs.getPlayersAlwaysHurt().get()){
            return Float.MIN_VALUE; // impossible to achieve, so guaranteed to fail
        } else{
            return constant;
        }
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    public float nullTime(float original) {
        return MeleeCombatConfigs.getAttackCooldownImpactOnDamage().get() ? original : 1;
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 3)
    private boolean canSweep(boolean original){
        return CombatUtil.canSweepAttack(this.getItemInHand(InteractionHand.MAIN_HAND));
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
    private float modifyDamageBonus1(float original, Entity target){
        return CombatUtil.recalculateDamageBonus(this.getMainHandItem(), original, target);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
    private float modifyDamageBonus2(float original, Entity target){
        return CombatUtil.recalculateDamageBonus(this.getMainHandItem(), original, target);
    }

    @ModifyReturnValue(method = "getCurrentItemAttackStrengthDelay", at = @At(value = "RETURN"))
    private float currentItemStrengthDelayAdjust(float original) {
        return MeleeCombatConfigs.getAttackDurationAdjustment().get() ? (int)(original + 0.5F) : original;
    }

    /*
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void dontResetTickerIfWeaponSwapped(Player instance){
        if(!MeleeCombatConfigs.getAttackCooldownWhenSwitchingPrevented().get()){
            instance.resetAttackStrengthTicker();
        }
    }
     */

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private boolean dontResetTickerIfWeaponSwapped(Player instance){
        return !MeleeCombatConfigs.getAttackCooldownWhenSwitchingPrevented().get();
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 2), ordinal = 1)
    private float scaleAndStoreDamageBonus(float original){
        float damageBonus = CombatUtil.scaleDamageBonus(this, original);
        this.currentDamageBonus = damageBonus;
        return damageBonus;
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE"), ordinal = 0)
    private CriticalHitEvent storeCriticalHitEvent(CriticalHitEvent original){
        this.criticalHitEvent = original;
        return original;
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 3), ordinal = 0)
    private float addCritDamageBonus(float original){
        if(this.criticalHitEvent != null && MeleeCombatConfigs.getEnchantmentDamageScalesWithCriticalHits().get()){
            original += ((this.criticalHitEvent.getDamageModifier() - 1.0F) * this.currentDamageBonus);
        }
        return original;
    }

    @Override
    public int getMissedAttackRecovery() {
        return missedAttackRecovery;
    }

    @Override
    public void setMissedAttackRecovery(int missedAttackRecovery) {
        this.missedAttackRecovery = missedAttackRecovery;
    }
}
