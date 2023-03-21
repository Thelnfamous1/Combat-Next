package com.infamous.combat_next.client;

import com.infamous.combat_next.config.ShieldCombatConfigs;
import com.infamous.combat_next.mixin.MultiPlayerGameModeAccessor;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Optional;

public class ClientCombatUtil {
    //public static final String SHIELD_ON_CROUCH_TRANSLATION_KEY = "options.shieldOnCrouch";
    //private static final OptionInstance<Boolean> shieldOnCrouch = OptionInstance.createBoolean(SHIELD_ON_CROUCH_TRANSLATION_KEY, true);

    public static void ensureHasSentCarriedItem(){
        //noinspection ConstantConditions
        ((MultiPlayerGameModeAccessor)Minecraft.getInstance().gameMode).callEnsureHasSentCarriedItem();
    }

    public static boolean hitEntity(Player player) {
        Optional<EntityHitResult> entityHitResult = CombatUtil.getEntityHit(player);
        if (entityHitResult.isPresent()) {
            //noinspection ConstantConditions
            Minecraft.getInstance().gameMode.attack(player, entityHitResult.get().getEntity());
            return true;
        }
        return false;
    }

    public static boolean isCrouchShielding(Player player){
        return Minecraft.getInstance().options.keyShift.isDown()
                && !Minecraft.getInstance().options.keyUse.isDown()
                && CombatUtil.isUsingOffhandShield(player);
    }

    public static void handleShiftKeyDown() {
        if (Minecraft.getInstance().getOverlay() == null && (Minecraft.getInstance().screen == null || Minecraft.getInstance().screen.passEvents)) {
            //noinspection ConstantConditions
            if(Minecraft.getInstance().options.keyShift.isDown()
                    && CombatUtil.hasOffhandShield(Minecraft.getInstance().player)
                    && CombatUtil.canShieldOnCrouch(Minecraft.getInstance().player)
                    && !Minecraft.getInstance().player.isUsingItem()
                    && ShieldCombatConfigs.getShieldCrouch().get()){
                startUsingOffhandShield();
            }
        }
    }

    private static void startUsingOffhandShield() {
        Minecraft minecraft = Minecraft.getInstance();
        MultiPlayerGameMode gameMode = minecraft.gameMode;
        LocalPlayer player = minecraft.player;
        //noinspection ConstantConditions
        if (!player.isHandsBusy()) {
            ItemStack itemInHand = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!itemInHand.isEmpty() && CombatUtil.isShield(itemInHand)) {
                //noinspection ConstantConditions
                InteractionResult useResult = gameMode.useItem(player, InteractionHand.OFF_HAND);
                if (useResult.consumesAction()) {
                    if (useResult.shouldSwing()) {
                        player.swing(InteractionHand.OFF_HAND);
                    }

                    minecraft.gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.OFF_HAND);
                }
            }
        }
    }
}
