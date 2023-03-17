package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "blockUsingShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;disableShield(Z)V"), cancellable = true)
    private void handleDisableShield(LivingEntity attacker, CallbackInfo ci){
        ci.cancel();
        CombatUtil.newDisableShield((Player) (Object)this, attacker);
    }

    @ModifyConstant(method = "hurt", constant = @Constant(floatValue = 0.0F, ordinal = 1))
    private float bypassZeroDamage(float constant, DamageSource source, float damage){
        return damage + 1; // impossible to achieve, so guaranteed to fail
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/minecraftforge/common/ToolAction;)Z", remap = false))
    private boolean canSweep(ItemStack instance, ToolAction toolAction){
        return instance.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) > 0;
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    private float getDamageBonus(ItemStack stack, MobType p_44835_, Entity target){
        return CombatUtil.getDamageBonusRedirect(stack, target);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void dontResetTickerIfWeaponSwapped(Player instance){

    }
}
