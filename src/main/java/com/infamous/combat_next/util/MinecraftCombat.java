package com.infamous.combat_next.util;

import net.minecraft.client.Minecraft;

public interface MinecraftCombat {

    static MinecraftCombat cast(Minecraft minecraft){
        return (MinecraftCombat) minecraft;
    }

    int getLeftClickDelay();

    void setLeftClickDelay(int leftClickDelay);
}
