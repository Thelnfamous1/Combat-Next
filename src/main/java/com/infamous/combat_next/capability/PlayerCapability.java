package com.infamous.combat_next.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface PlayerCapability extends INBTSerializable<CompoundTag> {

    String PLAYED_BEFORE_TAG = "played_before";
    Capability<PlayerCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    default boolean getPlayedBeforeAndUpdate(){
        boolean playedBefore = this.getPlayedBefore();
        if(!playedBefore){
            this.setPlayedBefore(true);
        }
        return playedBefore;
    }

    boolean getPlayedBefore();
    void setPlayedBefore(boolean playedBefore);

    class Implementation implements PlayerCapability {
        private boolean playedBefore;

        @Override
        public boolean getPlayedBefore() {
            return this.playedBefore;
        }

        @Override
        public void setPlayedBefore(boolean playedBefore) {
            this.playedBefore = playedBefore;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(PLAYED_BEFORE_TAG, this.playedBefore);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.playedBefore = nbt.getBoolean(PLAYED_BEFORE_TAG);
        }
    }
}