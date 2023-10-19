package com.infamous.combat_next.util;

import net.minecraft.client.Minecraft;

public interface MinecraftCombat {

    static MinecraftCombat cast(Minecraft minecraft){
        return (MinecraftCombat) minecraft;
    }

    default boolean noLeftClickDelay(){
        return this.getLeftClickDelay() == 0;
    }

    default void updateLeftClickDelay(){
        int leftClickDelay = this.getLeftClickDelay();
        if(leftClickDelay > 0){
            this.setLeftClickDelay(leftClickDelay - 1);
        }
    }

    int getLeftClickDelay();

    void setLeftClickDelay(int leftClickDelay);
    
    boolean setRetainAttack(boolean retain);

}
