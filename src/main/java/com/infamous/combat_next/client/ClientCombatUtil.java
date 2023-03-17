package com.infamous.combat_next.client;

import com.infamous.combat_next.mixin.MultiPlayerGameModeAccessor;
import com.infamous.combat_next.util.CombatMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

public class ClientCombatUtil {

    public static void ensureHasSentCarriedItem(){
        //noinspection ConstantConditions
        ((MultiPlayerGameModeAccessor)Minecraft.getInstance().gameMode).callEnsureHasSentCarriedItem();
    }

    public static boolean hitEntity(Player player) {
        EntityHitResult entityHitResult = CombatMinecraft.cast(Minecraft.getInstance()).getEntityHitResult();
        if (entityHitResult != null) {
            //noinspection ConstantConditions
            Minecraft.getInstance().gameMode.attack(player, entityHitResult.getEntity());
            return true;
        }
        return false;
    }
}
