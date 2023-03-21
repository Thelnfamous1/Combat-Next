package com.infamous.combat_next.mixin;

import com.infamous.combat_next.client.ClientCombatUtil;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "releaseUsingItem", at = @At("HEAD"), cancellable = true)
    private void handleReleaseUsingItem(Player player, CallbackInfo ci){
        if(ClientCombatUtil.isCrouchShielding(player) && CombatUtil.canShieldOnCrouch(player)){
            ci.cancel();
        }
    }
}
