package com.infamous.combat_next.network;

import com.infamous.combat_next.CombatNext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CNNetwork {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final String PROTOCOL_VERSION = "1";
    private static final ResourceLocation LOCATION = new ResourceLocation(CombatNext.MODID, "sync");
    public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.newSimpleChannel(
            LOCATION,
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register(){
        SYNC_CHANNEL.registerMessage(COUNTER.getAndIncrement(),
                ServerboundMissPacket.class,
                ServerboundMissPacket::write,
                ServerboundMissPacket::new,
                ServerboundMissPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        SYNC_CHANNEL.registerMessage(COUNTER.getAndIncrement(),
                ClientboundConfigSyncPacket.class,
                ClientboundConfigSyncPacket::write,
                ClientboundConfigSyncPacket::new,
                ClientboundConfigSyncPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CombatNext.LOGGER.info("Registered {} messages for NetworkChannel {}", COUNTER.get(), LOCATION);
    }

    public static <MSG> void syncToPlayer(ServerPlayer serverPlayer, MSG packet)
    {
        SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
        CombatNext.LOGGER.info("Sending sync packet {} to {}", packet, serverPlayer);
    }
}
