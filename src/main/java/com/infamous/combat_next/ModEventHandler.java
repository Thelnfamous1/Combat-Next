package com.infamous.combat_next;

import com.infamous.combat_next.network.CNNetwork;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CombatNext.MODID)
public class ModEventHandler {

    private static final double BASE_ATTACK_RANGE = 2.5D;

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            CNNetwork.register();
            CombatUtil.registerTridentDispenseBehavior();
            CombatUtil.modifyStrengthEffect();
            CombatUtil.modifyItemMaxStackSizes();
        });
    }

    @SubscribeEvent
    static void onEntityAttributeModification(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER, ForgeMod.ATTACK_RANGE.get(), BASE_ATTACK_RANGE);
    }
}
