package com.infamous.combat_next.client;

import com.infamous.combat_next.mixin.MultiPlayerGameModeAccessor;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Optional;

public class ClientCombatUtil {

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
}
