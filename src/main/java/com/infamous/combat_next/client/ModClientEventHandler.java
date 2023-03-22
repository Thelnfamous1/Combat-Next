package com.infamous.combat_next.client;

import com.infamous.combat_next.CombatNext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CombatNext.MODID, value = Dist.CLIENT)
public class ModClientEventHandler {

    @SubscribeEvent
    static void registerOverlay(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), CNGuiOverlay.SHIELD_INDICATOR_CROSSHAIR.id().getPath(), CNGuiOverlay.SHIELD_INDICATOR_CROSSHAIR.overlay);
        event.registerBelow(VanillaGuiOverlay.HOTBAR.id(), CNGuiOverlay.SHIELD_INDICATOR_HOTBAR.id().getPath(), CNGuiOverlay.SHIELD_INDICATOR_HOTBAR.overlay);
    }
}
