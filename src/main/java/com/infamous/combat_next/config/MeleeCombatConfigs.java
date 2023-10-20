package com.infamous.combat_next.config;

import com.infamous.combat_next.util.HitboxInflationType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class MeleeCombatConfigs {
    private static ForgeConfigSpec.IntValue attackMissCooldownTicks;
    private static ForgeConfigSpec.DoubleValue attackReachBonusWhenSupercharged;
    private static ForgeConfigSpec.DoubleValue attackStrengthScaleSuperchargeThreshold;
    private static ForgeConfigSpec.DoubleValue sweepingDamageScale;
    private static ForgeConfigSpec.BooleanValue weaponRebalancing;
    private static ForgeConfigSpec.BooleanValue attackMissReducedCooldown;
    private static ForgeConfigSpec.BooleanValue attackMissSweepAttack;
    private static ForgeConfigSpec.BooleanValue attackDuringCooldownPrevented;
    private static ForgeConfigSpec.BooleanValue attackGracePeriod;
    private static ForgeConfigSpec.DoubleValue attackGracePeriodTime;
    private static ForgeConfigSpec.BooleanValue attackThroughNonSolidBlocks;
    private static ForgeConfigSpec.BooleanValue attackCriticalWhenSprinting;
    private static ForgeConfigSpec.BooleanValue attackSupercharge;
    private static ForgeConfigSpec.BooleanValue sweepAttackChange;
    private static ForgeConfigSpec.BooleanValue attackWhenKeyHeld;
    private static ForgeConfigSpec.BooleanValue enchantmentDamageScalesWithModifiers;
    private static ForgeConfigSpec.BooleanValue enchantmentDamageScalesWithCriticalHits;
    private static ForgeConfigSpec.BooleanValue axeHitEnemyChange;
    private static ForgeConfigSpec.IntValue attackHeldDelayTicks;
    private static ForgeConfigSpec.BooleanValue attackCooldownWhenSwitchingPrevented;
    private static ForgeConfigSpec.BooleanValue attackCooldownImpactOnDamage;
    private static ForgeConfigSpec.BooleanValue sweepingEdgeOnAxes;
    private static ForgeConfigSpec.BooleanValue playerAttackDamageBaseChange;
    private static ForgeConfigSpec.BooleanValue playerAttackReachBaseChange;
    private static ForgeConfigSpec.BooleanValue playerAttributeChange;
    private static ForgeConfigSpec.BooleanValue playerAttributeChangeFirstLogin;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> weaponAttackDamageEntries;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> weaponAttackSpeedEntries;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> weaponAttackRangeEntries;
    private static ForgeConfigSpec.BooleanValue iFramesByWeaponAttackCooldown;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect melee combat.", "Melee Combat Config Options", b -> {
            attackCooldownWhenSwitchingPrevented = b
                    .comment("""
                            Toggles the prevention of switching weapons triggering attack cooldown.
                            For vanilla, this value is false.
                            """)
                    .define("attack_cooldown_when_switching_prevented", true);
            attackCooldownImpactOnDamage = b
                    .comment("""
                            Toggles the removal of attacking too early changing damage and disabling crits
                            For vanilla, this value is true.
                            """)
                    .define("attack_cooldown_impact_on_damage", false);
            attackCriticalWhenSprinting = b
                    .comment("""
                            Toggles attacks becoming critical when sprinting.
                            For vanilla, this value is false.
                            """)
                    .define("attack_critical_when_sprinting", true);
            attackDuringCooldownPrevented = b
                    .comment("""
                            Toggles the prevention of attacking while on attack cooldown.
                            For vanilla, this value is false.
                            """)
                    .define("attack_during_cooldown_prevented", true);
            attackGracePeriod = b
                    .comment("""
                            Toggles the "grace period" where if you attack before 100% when you shouldn't be able to, but are between 80% and 100%, the attack is delayed until one tick after 100%.
                            For vanilla, this value is false.
                            """)
                    .define("attack_grace_period", true);
            attackGracePeriodTime = b
                    .comment("""
                            Determines the grace period time, between 0 and 1, default is 0.8, representing the 80%-100% window normally present.
                            For vanilla, this value is 1.
                            """)
                    .defineInRange("attack_grace_period_time", 0.8, 0, 1);
            attackHeldDelayTicks = b
                    .comment("""
                            Adjusts the minimum delay in ticks (1/20 seconds) between attacks when holding the attack key.
                            Note: The higher this value is, the more it may negatively affect weapons with a high attack speed.
                            """)
                    .defineInRange("attack_held_delay_ticks", 4, 0, 20);
            attackMissCooldownTicks = b
                    .comment("""
                            Adjusts the attack cooldown ticks (1/20 seconds) given when missing an attack.
                            Note: The "attack_miss_reduced_cooldown" config value must be set to true.
                            """)
                    .defineInRange("attack_miss_cooldown_ticks", 4, 0, 20);
            attackMissReducedCooldown = b
                    .comment("""
                            Toggles missed attacks no longer triggering the full attack strength cooldown.
                            It will instead set the cooldown to the "attack_miss_cooldown_ticks" config value.
                            For vanilla, this value is false.
                            """)
                    .define("attack_miss_reduced_cooldown", true);
            attackMissSweepAttack = b
                    .comment("""
                            Toggles missed attacks still triggering sweep attacks in front of the player.
                            For vanilla, this value is false.
                            """)
                    .define("attack_miss_sweep_attack", true);
            attackReachBonusWhenSupercharged = b
                    .comment("""
                            Adjusts the bonus attack reach given when the held weapon is "supercharged", in blocks.
                            Note: The "attack_supercharge" config value must be set to true.
                            """)
                    .defineInRange("attack_reach_bonus_when_supercharged", 1.0D, 0.0D, 3.0D);
            attackStrengthScaleSuperchargeThreshold = b
                    .comment("""
                            Adjusts the threshold at which the player's attack strength scale is considered "supercharged".
                            Note: The "attack_supercharge" config value must be set to true.
                            """)
                    .defineInRange("attack_strength_scale_supercharge_threshold", 2.0F, 1.0D, 4.0D);
            attackSupercharge = b
                    .comment("""
                            Toggles attacks becoming "supercharged", granting the player bonus attack reach.
                            The attack strength scale will be checked against the "attack_strength_scale_supercharge_threshold" config value.
                            Note: Bonus attack reach is not given if the player is crouching.
                            For vanilla, this value is false.
                            """)
                    .define("attack_supercharge", true);
            attackThroughNonSolidBlocks = b
                    .comment("""
                            Toggles attacking through non-solid blocks, such as grass and vines.
                            For vanilla, this value is false.
                            """)
                    .define("attack_through_non_solid_blocks", true);
            attackWhenKeyHeld = b
                    .comment("""
                            Toggles attacking automatically when holding the attack key.
                            For vanilla, this value is false.
                            """)
                    .define("attack_when_key_held", true);
            axeHitEnemyChange = b
                    .comment("""
                            Toggles Axes taking taking less durability damage when hitting an enemy.
                            For vanilla, this value is false.
                            """)
                    .define("axe_hit_enemy_change", true);
            enchantmentDamageScalesWithCriticalHits = b
                    .comment("""
                            Toggles weapon enchantment damage being scaled by the critical hit multiplier.
                            For vanilla, this value is false.
                            """)
                    .define("enchantment_damage_scales_with_critical_hits", true);
            enchantmentDamageScalesWithModifiers = b
                    .comment("""
                            Toggles weapon enchantment damage being scaled by multiplicative Attack Damage attribute modifiers.
                            For vanilla, this value is false.
                            """)
                    .define("enchantment_damage_scales_with_modifiers", true);
            iFramesByWeaponAttackCooldown = b
                    .comment("""
                            Toggles the number of a target's i-frames (invulnerable frames) given when attacked being based on the attack cooldown of the attacker's weapon.
                            A faster weapon, with a lower attack cooldown, means the target will have less i-frames given.
                            For vanilla, this value is false.
                            """)
                    .define("i_frames_by_weapon_attack_cooldown", true);
            playerAttackDamageBaseChange = b
                    .comment("""
                            Toggles the increase of a player's Attack Damage base value to 2 from 1 (in half-hearts).
                            Note: The "player_attribute_change" config value must be set to true.
                            For vanilla, this value is false.
                            """)
                    .define("player_attack_damage_base_change", true);
            playerAttackReachBaseChange = b
                    .comment("""
                            Toggles the decrease of a player's Attack Range base value to 2.5 from 3 (in blocks).
                            Note: The "player_attribute_change" config value must be set to true.
                            For vanilla, this value is false.
                            """)
                    .define("player_attack_reach_base_change", true);
            playerAttributeChange = b
                    .comment("""
                            Toggles players having their Attack Damage and Attack Range base values changed upon login.
                            For vanilla, this value is false.
                            """)
                    .define("player_attribute_change", true);
            playerAttributeChangeFirstLogin = b
                    .comment("""
                            Toggles players having Attack Damage and Attack Range base values changed ONLY upon first login to world.
                            If false, any previously saved Attack Damage and Attack Range base values will be overwritten.
                            Note: The "player_attribute_change" config value must be set to true.
                            """)
                    .define("player_attribute_change_first_login", true);
            sweepAttackChange = b
                    .comment("""
                            Toggles sweep attacks being triggered only if the weapon is enchanted with Sweeping Edge.
                            For vanilla, this value is false.
                            """)
                    .define("sweep_attack_change", true);
            sweepingDamageScale = b
                    .comment("""
                            Adjusts how much sweeping damage is scaled by during a sweep attack.
                            For vanilla, this value is 1.0 (unscaled).
                            """)
                    .defineInRange("sweeping_damage_scale", 0.5F, 0.0F, 1.0F);
            sweepingEdgeOnAxes = b
                    .comment("""
                            Toggles Axes being able to receive the Sweeping Edge enchantment in Survival via Anvils.
                            For vanilla, this value is false.
                            """)
                    .define("sweeping_edge_on_axes", true);
            weaponAttackDamageEntries = b
                    .comment("""
                            A list of weapon item ids mapped to their corresponding Attack Damage value, in half-hearts.
                            Format each entry as a namespaced id (ex. for the Wooden Sword, "minecraft:wooden_sword"), follow by a "#", follow by a non-negative decimal value (ex. 5.0).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("weapon_attack_damage_entries", ConfigUtil.getMapAsStringList(ConfigUtil.DEFAULT_ATTACK_DAMAGE_VALUES), entry -> entry instanceof String);
            weaponAttackRangeEntries = b
                    .comment("""
                            A list of weapon item ids mapped to their corresponding Attack Range value, in blocks.
                            Format each entry as a namespaced id (ex. for the Wooden Sword,, "minecraft:wooden_sword"), follow by a "#", follow by a non-negative decimal value (ex. 3.0).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("weapon_attack_range_entries", ConfigUtil.getMapAsStringList(ConfigUtil.DEFAULT_ATTACK_RANGE_VALUES), entry -> entry instanceof String);
            weaponAttackSpeedEntries = b
                    .comment("""
                            A list of weapon item ids mapped to their corresponding Attack Speed value, in attacks per second.
                            Format each entry as a namespaced id (ex. for the Wooden Sword,, "minecraft:wooden_sword"), follow by a "#", follow by a non-negative decimal value (ex. 3.0).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("weapon_attack_speed_entries", ConfigUtil.getMapAsStringList(ConfigUtil.DEFAULT_ATTACK_SPEED_VALUES), entry -> entry instanceof String);
            weaponRebalancing = b
                    .comment("""
                            Toggles the re-balancing of the Attack Damage, Attack Speed and Attack Range modifiers for specific weapons.
                            Each weapon's Attack Damage will be their Attack Damage value obtained from the "weapon_attack_damage_entries" config value.
                            Each weapon's Attack Speed will be their Attack Speed value obtained from the "weapon_attack_speed_entries" config value.
                            Each weapon's Attack Range will be their Attack Range value obtained from the "weapon_attack_range_entries" config value.
                            For vanilla, this value is false.
                            """)
                    .define("weapon_rebalancing", true);
        });
    }

    public static ForgeConfigSpec.IntValue getAttackMissCooldownTicks() {
        return attackMissCooldownTicks;
    }

    public static ForgeConfigSpec.DoubleValue getAttackReachBonusWhenSupercharged() {
        return attackReachBonusWhenSupercharged;
    }

    public static ForgeConfigSpec.DoubleValue getAttackStrengthScaleSuperchargeThreshold() {
        return attackStrengthScaleSuperchargeThreshold;
    }

    public static ForgeConfigSpec.DoubleValue getSweepingDamageScale() {
        return sweepingDamageScale;
    }

    public static ForgeConfigSpec.BooleanValue getWeaponRebalancing() {
        return weaponRebalancing;
    }

    public static ForgeConfigSpec.BooleanValue getAttackMissReducedCooldown() {
        return attackMissReducedCooldown;
    }

    public static ForgeConfigSpec.BooleanValue getAttackMissSweepAttack() {
        return attackMissSweepAttack;
    }

    public static ForgeConfigSpec.BooleanValue getAttackDuringCooldownPrevented() {
        return attackDuringCooldownPrevented;
    }

    public static ForgeConfigSpec.BooleanValue getAttackGracePeriod() {
        return attackGracePeriod;
    }

    public static ForgeConfigSpec.DoubleValue getAttackGracePeriodTime() {
        return attackGracePeriodTime;
    }

    public static ForgeConfigSpec.BooleanValue getAttackThroughNonSolidBlocks() {
        return attackThroughNonSolidBlocks;
    }

    public static ForgeConfigSpec.BooleanValue getAttackCriticalWhenSprinting() {
        return attackCriticalWhenSprinting;
    }

    public static ForgeConfigSpec.BooleanValue getAttackSupercharge() {
        return attackSupercharge;
    }

    public static ForgeConfigSpec.BooleanValue getSweepAttackChange() {
        return sweepAttackChange;
    }

    public static ForgeConfigSpec.BooleanValue getAttackWhenKeyHeld() {
        return attackWhenKeyHeld;
    }

    public static ForgeConfigSpec.BooleanValue getEnchantmentDamageScalesWithModifiers() {
        return enchantmentDamageScalesWithModifiers;
    }

    public static ForgeConfigSpec.BooleanValue getEnchantmentDamageScalesWithCriticalHits() {
        return enchantmentDamageScalesWithCriticalHits;
    }

    public static ForgeConfigSpec.BooleanValue getAxeHitEnemyChange() {
        return axeHitEnemyChange;
    }

    public static ForgeConfigSpec.IntValue getAttackHeldDelayTicks() {
        return attackHeldDelayTicks;
    }

    public static ForgeConfigSpec.BooleanValue getAttackCooldownWhenSwitchingPrevented() {
        return attackCooldownWhenSwitchingPrevented;
    }

    public static ForgeConfigSpec.BooleanValue getAttackCooldownImpactOnDamage() {
        return attackCooldownImpactOnDamage;
    }

    public static ForgeConfigSpec.BooleanValue getSweepingEdgeOnAxes() {
        return sweepingEdgeOnAxes;
    }

    public static ForgeConfigSpec.BooleanValue getPlayerAttackDamageBaseChange() {
        return playerAttackDamageBaseChange;
    }

    public static ForgeConfigSpec.BooleanValue getPlayerAttackReachBaseChange() {
        return playerAttackReachBaseChange;
    }

    public static ForgeConfigSpec.BooleanValue getPlayerAttributeChangeFirstLogin() {
        return playerAttributeChangeFirstLogin;
    }

    public static ForgeConfigSpec.BooleanValue getPlayerAttributeChange() {
        return playerAttributeChange;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getWeaponAttackDamageEntries() {
        return weaponAttackDamageEntries;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getWeaponAttackRangeEntries() {
        return weaponAttackRangeEntries;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getWeaponAttackSpeedEntries() {
        return weaponAttackSpeedEntries;
    }

    public static ForgeConfigSpec.BooleanValue getiFramesByWeaponAttackCooldown() {
        return iFramesByWeaponAttackCooldown;
    }
}
