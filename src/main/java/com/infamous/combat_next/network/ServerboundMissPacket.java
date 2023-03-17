package com.infamous.combat_next.network;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundMissPacket {

   private ServerboundMissPacket() {
   }

   public static ServerboundMissPacket createMissPacket() {
      return new ServerboundMissPacket();
   }

   public ServerboundMissPacket(FriendlyByteBuf ignoredByteBuf) {
   }

   public void write(FriendlyByteBuf ignoredByteBuf) {
   }

   public static void handle(ServerboundMissPacket ignoredPacket, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sender = ctx.get().getSender();
         //noinspection ConstantConditions
         CombatUtil.attackEmpty(sender);
      });
      ctx.get().setPacketHandled(true);
   }
}