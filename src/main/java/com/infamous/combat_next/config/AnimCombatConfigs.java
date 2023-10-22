package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AnimCombatConfigs {
    private static ForgeConfigSpec.BooleanValue armHeightAnim;
    private static ForgeConfigSpec.BooleanValue armHeightRaisesToCharged;

    static void createClientConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect client animations.", "Animation Config Options", b -> {
            armHeightAnim = b
                    .comment("""
                            Determines whether vanilla or CTS arm height animation will be used after attacks.
                            For vanilla, this value is false.
                            """)
                    .define("arm_height_anim", true);
            armHeightRaisesToCharged = b
                    .comment("""
                            Determines whether the arm will raise up to the charged attack value.
                            For vanilla, this value is false.
                            """)
                    .define("arm_height_raises_to_charged", true);
        });
    }

    public static ForgeConfigSpec.BooleanValue getArmHeightAnim() {
        return armHeightAnim;
    }
    public static ForgeConfigSpec.BooleanValue getArmHeightRaisesToCharged() {
        return armHeightRaisesToCharged;
    }
}

