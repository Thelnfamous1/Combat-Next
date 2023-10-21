package com.infamous.combat_next.mixin;

import com.infamous.combat_next.client.ClientCombatUtil;
import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.config.ShieldCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.util.MinecraftCombat;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements MinecraftCombat {

    @Shadow @Nullable
    public LocalPlayer player;

    @Shadow protected abstract boolean startAttack();

    @Shadow @Nullable public ClientLevel level;
    @Shadow @Final public Options options;
    @Shadow @javax.annotation.Nullable public HitResult hitResult;
    @Shadow @Final public MouseHandler mouseHandler;
    @Shadow @javax.annotation.Nullable public Screen screen;
    @Shadow protected int missTime;
    private int leftClickDelay;
    public boolean retainAttack = false;
    
    /*
    @Redirect(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V", ordinal = 0))
    private void dontResetTickerIfMissed(LocalPlayer instance){
        // NO-OP
    }
     */

    @WrapWithCondition(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V", ordinal = 0))
    private boolean dontResetTickerIfMissed(LocalPlayer instance){
        return false;
    }

    @Inject(method = "continueAttack", at = @At(value = "HEAD"), cancellable = true)
    private void handleContinueAttack(boolean started, CallbackInfo ci){
        //noinspection ConstantConditions
	    boolean retained = this.screen == null && (this.options.keyAttack.isDown() || this.retainAttack) && this.mouseHandler.isMouseGrabbed();
        if (missTime <= 0) {
		    if (player != null && !this.player.isUsingItem()) {
			    if (retained && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
			    	this.retainAttack = false;
			    } if (retained && !CombatUtil.onAttackCooldown(this.player, -1.0F) && MeleeCombatConfigs.getAttackWhenKeyHeld().get() && this.noLeftClickDelay()) {
                    ci.cancel();
                    this.retainAttack = false;
                    this.startAttack();
			    }
		    }
	    }
    }

    @Inject(at = @At("HEAD"), method = "startAttack")
    private void handleStartAttack(CallbackInfoReturnable<Boolean> cir){
        this.leftClickDelay = MeleeCombatConfigs.getAttackHeldDelayTicks().get();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "handleKeybinds", at = @At(value = "STORE", ordinal = 1))
    private boolean modifyStartedAttack(boolean original){
        //noinspection ConstantConditions
        if (this.player.isUsingItem() && ClientCombatUtil.isCrouchShielding(this.player) && ShieldCombatConfigs.getShieldAttackWhileCrouchShielding().get()) {
            while (this.options.keyAttack.consumeClick()) {
                original |= this.startAttack();
            }
        }
        return original;
    }

    /*
    @Redirect(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean redirectIsUsingItem(LocalPlayer instance){
        return instance.isUsingItem() && (!ClientCombatUtil.isCrouchShielding(instance) || !ShieldCombatConfigs.getShieldAttackWhileCrouchShielding().get());
    }
     */

    @ModifyExpressionValue(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean modifyIsUsingItem(boolean isUsingItem){
        return isUsingItem && (!ClientCombatUtil.isCrouchShielding(this.player) || !ShieldCombatConfigs.getShieldAttackWhileCrouchShielding().get());
    }

    @Override
    public int getLeftClickDelay() {
        return this.leftClickDelay;
    }

    @Override
    public void setLeftClickDelay(int leftClickDelay) {
        this.leftClickDelay = leftClickDelay;
    }

    @Override
    public void setRetainAttack(boolean retain) {
        this.retainAttack = retain;
    }

}
