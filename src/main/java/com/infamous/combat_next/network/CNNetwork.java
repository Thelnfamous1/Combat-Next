package com.infamous.combat_next.network;

import com.infamous.combat_next.CombatNext;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CNNetwork {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CombatNext.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register(){
        INSTANCE.registerMessage(COUNTER.getAndIncrement(),
                ServerboundMissPacket.class,
                ServerboundMissPacket::write,
                ServerboundMissPacket::new,
                ServerboundMissPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
