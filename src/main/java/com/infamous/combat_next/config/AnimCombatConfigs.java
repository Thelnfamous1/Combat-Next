package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AnimCombatConfigs {
    private static ForgeConfigSpec.BooleanValue armHeightAnim;

    static void createClientConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect client animations.", "Animation Config Options", b -> {
            armHeightAnim = b
                    .comment("""
                            Determines whether vanilla or CTS arm height animation will be used after attacks.
                            """)
                    .define("arm_height_anim", true);
        });
    }

    public static ForgeConfigSpec.BooleanValue getArmHeightAnim() {
        return armHeightAnim;
    }
}

