package com.infamous.combat_next.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public interface CombatMinecraft {

    static CombatMinecraft cast(Minecraft minecraft){
        return (CombatMinecraft) minecraft;
    }

    @Nullable EntityHitResult getEntityHitResult();

    void setEntityHitResult(@Nullable EntityHitResult entityHitResult);
}
