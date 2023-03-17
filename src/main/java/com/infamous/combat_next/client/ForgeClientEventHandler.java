package com.infamous.combat_next.client;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CombatNext.MODID, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    static void onLeftClick(InputEvent.InteractionKeyMappingTriggered event){
        if(!event.isCanceled() && event.isAttack()){
            Player player = Minecraft.getInstance().player;
            //noinspection ConstantConditions
            if(CombatUtil.onAttackCooldown(player, 0.5F)){
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }
}
