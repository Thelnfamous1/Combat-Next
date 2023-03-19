package com.infamous.combat_next;

import com.infamous.combat_next.network.CNNetwork;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CombatNext.MODID)
public class ModEventHandler {

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            CNNetwork.register();
            CombatUtil.registerTridentDispenseBehavior();
            CombatUtil.setAttributeSyncable(Attributes.ATTACK_DAMAGE);
            CombatUtil.setAttributeSyncable(Attributes.ATTACK_KNOCKBACK);
        });
    }

}
