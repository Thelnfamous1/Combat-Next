package com.infamous.combat_next.data;

import com.infamous.combat_next.CombatNext;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CombatNext.MODID)
public class DataEventHandler {

    @SubscribeEvent
    static void onDataGathering(GatherDataEvent event){
        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(includeClient, new CNLangProvider(generator));
        CNBlockTagsProvider blockTagsProvider = new CNBlockTagsProvider(generator, existingFileHelper);
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new CNItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
    }
}
