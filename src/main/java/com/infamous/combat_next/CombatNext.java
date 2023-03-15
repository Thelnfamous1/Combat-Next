package com.infamous.combat_next;

import com.infamous.combat_next.registry.EnchantmentRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CombatNext.MODID)
public class CombatNext
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "combat_next";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public CombatNext()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EnchantmentRegistry.ENCHANTMENTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
