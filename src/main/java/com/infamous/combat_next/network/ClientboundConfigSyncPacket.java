package com.infamous.combat_next.network;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundConfigSyncPacket {

    private ClientboundConfigSyncPacket(){

    }

    public ClientboundConfigSyncPacket(FriendlyByteBuf ignoredByteBuf){

    }

    public static ClientboundConfigSyncPacket createConfigSyncPacket() {
        return new ClientboundConfigSyncPacket();
    }

    public void write(FriendlyByteBuf ignoredByteBuf){

    }

    public static void handle(ClientboundConfigSyncPacket ignoredPacket, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(CombatUtil::applySyncedConfigs);
        ctx.get().setPacketHandled(true);
    }

}
