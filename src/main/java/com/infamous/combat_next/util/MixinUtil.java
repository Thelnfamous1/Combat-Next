package com.infamous.combat_next.util;

import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;

public class MixinUtil {

    public static boolean isSweepingEdge(Object object){
        return object instanceof SweepingEdgeEnchantment;
    }
}
