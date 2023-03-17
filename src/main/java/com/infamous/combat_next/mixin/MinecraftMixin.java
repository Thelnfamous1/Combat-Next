package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatMinecraft;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements CombatMinecraft {

    @Shadow @Nullable
    public LocalPlayer player;

    @Shadow protected abstract boolean startAttack();

    @Shadow @Nullable public ClientLevel level;

    @Nullable public EntityHitResult entityHitResult;

    @Redirect(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V", ordinal = 0))
    private void dontResetTickerIfMissed(LocalPlayer instance){
        // NO-OP
    }

    @Redirect(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"))
    private void tryToContinueAttacking(MultiPlayerGameMode instance, boolean started){
        //noinspection ConstantConditions
        if (started && !CombatUtil.onAttackCooldown(this.player, -1.0F)) {
            this.startAttack();
        } else{
            instance.stopDestroyBlock();
        }
    }

    @Override
    @Nullable
    public EntityHitResult getEntityHitResult() {
        return this.entityHitResult;
    }

    @Override
    public void setEntityHitResult(@Nullable EntityHitResult entityHitResult) {
        this.entityHitResult = entityHitResult;
    }
}
