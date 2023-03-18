package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Nullable
    public LocalPlayer player;

    @Shadow protected abstract boolean startAttack();

    @Shadow @Nullable public ClientLevel level;

    @Redirect(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V", ordinal = 0))
    private void dontResetTickerIfMissed(LocalPlayer instance){
        // NO-OP
    }

    @Inject(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"), cancellable = true)
    private void handleContinueAttack(boolean started, CallbackInfo ci){
        //noinspection ConstantConditions
        if (started && !CombatUtil.onAttackCooldown(this.player, -1.0F)) {
            ci.cancel();
            this.startAttack();
        }
    }

}
