package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.config.ShieldCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
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
public abstract class PlayerMixin extends LivingEntity{

    private float currentDamageBonus;
    @Nullable
    private CriticalHitEvent criticalHitEvent;

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

    @ModifyConstant(method = "hurt", constant = @Constant(floatValue = 0.0F, ordinal = 1))
    private float bypassZeroDamage(float constant, DamageSource source, float damage){
        return damage + 1; // impossible to achieve, so guaranteed to fail
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

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void dontResetTickerIfWeaponSwapped(Player instance){
        if(!MeleeCombatConfigs.getAttackCooldownWhenSwitchingPrevented().get()){
            instance.resetAttackStrengthTicker();
        }
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
}
