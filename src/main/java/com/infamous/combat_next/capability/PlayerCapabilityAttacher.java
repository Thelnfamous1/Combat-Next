package com.infamous.combat_next.capability;

import com.infamous.combat_next.CombatNext;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CombatNext.MODID)
public class PlayerCapabilityAttacher {

    private PlayerCapabilityAttacher() {
    }

    @SubscribeEvent
    static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player){
            final PlayerCapabilityProvider provider = new PlayerCapabilityProvider();
            event.addCapability(PlayerCapabilityProvider.LOCATION, provider);
        }
    }

    @SubscribeEvent
    static void clone(final PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer && event.getOriginal() instanceof ServerPlayer oldPlayer) {
            oldPlayer.reviveCaps();

            newPlayer.getCapability(PlayerCapability.INSTANCE).ifPresent(newCap ->
                    oldPlayer.getCapability(PlayerCapability.INSTANCE).ifPresent(oldCap ->
                            newCap.deserializeNBT(oldCap.serializeNBT())));

            oldPlayer.invalidateCaps();
        }
    }
    private static class PlayerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        private static final ResourceLocation LOCATION = new ResourceLocation(CombatNext.MODID, "player");

        private final PlayerCapability backend = new PlayerCapability.Implementation();
        private final LazyOptional<PlayerCapability> optionalData = LazyOptional.of(() -> this.backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return PlayerCapability.INSTANCE.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }
}