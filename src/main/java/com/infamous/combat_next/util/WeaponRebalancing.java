package com.infamous.combat_next.util;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.config.ConfigUtil;
import com.infamous.combat_next.mixin.ItemAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeaponRebalancing {
    private static final EnumMap<WeaponType, Map<Tier, Map<Attribute, AttributeModifier>>> TIERED_WEAPON_ATTRIBUTES = new EnumMap<>(WeaponType.class);
    @SuppressWarnings("ConstantConditions")
    private static final ForgeTier TRIDENT_DUMMY_TIER = new ForgeTier(0, 0, 0, 0, 0, null, null);
    public static final UUID ITEM_ATTACK_RANGE_MODIFIER_UUID = UUID.fromString("26cb07a3-209d-4110-8e10-1010243614c8");
    private static final String ATTACK_DAMAGE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_damage_modifier").toString();
    private static final String ATTACK_SPEED_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_speed_modifier").toString();
    private static final String ATTACK_RANGE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_range_modifier").toString();
    private static final double AXE_ATTACK_DAMAGE_BASE = 5.0D;
    private static final double AXE_ATTACK_SPEED = 2.0D;
    private static final double HOE_ATTACK_RANGE = 3.5D;
    private static final double HOE_ATTACK_DAMAGE_BASE = 2.0D;
    private static final double PICKAXE_ATTACK_DAMAGE_BASE = 3.0D;
    private static final double PICKAXE_ATTACK_SPEED = 2.5D;
    private static final double SHOVEL_ATTACK_DAMAGE_BASE = 2.0D;
    private static final double SHOVEL_ATTACK_SPEED = 2.0D;
    private static final double SWORD_ATTACK_DAMAGE_BASE = 4.0D;
    private static final double SWORD_ATTACK_SPEED = 3.0D;
    private static final double SWORD_ATTACK_RANGE = 3.0D;
    private static final double TRIDENT_ATTACK_DAMAGE = 7.0D;
    private static final double TRIDENT_ATTACK_SPEED = 2.0D;
    private static final double TRIDENT_ATTACK_RANGE = 3.5D;

    public static void modifyWeaponAttributes(ItemAttributeModifierEvent event, Item item) {
        if(item instanceof TieredItem tieredItem && tieredItem.getTier() instanceof Tiers tier){
            if(item instanceof AxeItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.AXE, tier, getWeaponAttackDamage(tier, AXE_ATTACK_DAMAGE_BASE)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.AXE, tier, AXE_ATTACK_SPEED - ConfigUtil.getBaseAttackSpeed()));
            } else if(item instanceof HoeItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.HOE, tier, getWeaponAttackDamage(tier, HOE_ATTACK_DAMAGE_BASE)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.HOE, tier, getHoeAttackSpeed(tier)));
                event.addModifier(ForgeMod.ATTACK_RANGE.get(), getAttackRange(WeaponType.HOE, tier, HOE_ATTACK_RANGE - ConfigUtil.getBaseAttackRange()));
            } else if(item instanceof PickaxeItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.PICKAXE, tier, getWeaponAttackDamage(tier, PICKAXE_ATTACK_DAMAGE_BASE)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.PICKAXE, tier, PICKAXE_ATTACK_SPEED - ConfigUtil.getBaseAttackSpeed()));
            } else if(item instanceof ShovelItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.SHOVEL, tier, getWeaponAttackDamage(tier, SHOVEL_ATTACK_DAMAGE_BASE)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.SHOVEL, tier, SHOVEL_ATTACK_SPEED - ConfigUtil.getBaseAttackSpeed()));

            } else if(item instanceof SwordItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.SWORD, tier, getWeaponAttackDamage(tier, SWORD_ATTACK_DAMAGE_BASE)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.SWORD, tier, SWORD_ATTACK_SPEED - ConfigUtil.getBaseAttackSpeed()));
                event.addModifier(ForgeMod.ATTACK_RANGE.get(), getAttackRange(WeaponType.SWORD, tier, SWORD_ATTACK_RANGE - ConfigUtil.getBaseAttackRange()));
            }
        } else if(item == Items.TRIDENT){
            event.removeAttribute(Attributes.ATTACK_DAMAGE);
            event.removeAttribute(Attributes.ATTACK_SPEED);
            event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.TRIDENT, TRIDENT_DUMMY_TIER, TRIDENT_ATTACK_DAMAGE - ConfigUtil.getBaseAttackDamage()));
            event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.TRIDENT, TRIDENT_DUMMY_TIER, TRIDENT_ATTACK_SPEED - ConfigUtil.getBaseAttackSpeed()));
            event.addModifier(ForgeMod.ATTACK_RANGE.get(), getAttackSpeed(WeaponType.TRIDENT, TRIDENT_DUMMY_TIER, TRIDENT_ATTACK_RANGE - ConfigUtil.getBaseAttackRange()));
        }
    }

    @NotNull
    private static AttributeModifier getAttackDamage(WeaponType weaponType, Tier tier, double defaultValue) {
        return getModifier(weaponType, tier, Attributes.ATTACK_DAMAGE, getAttackDamageUUID(), ATTACK_DAMAGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    @NotNull
    private static AttributeModifier getAttackRange(WeaponType weaponType, Tier tier, double defaultValue) {
        return getModifier(weaponType, tier, ForgeMod.ATTACK_RANGE.get(), ITEM_ATTACK_RANGE_MODIFIER_UUID, ATTACK_RANGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    @NotNull
    private static AttributeModifier getAttackSpeed(WeaponType weaponType, Tier tier, double defaultValue) {
        return getModifier(weaponType, tier, Attributes.ATTACK_SPEED, getAttackSpeedUUID(), ATTACK_SPEED_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    private static UUID getAttackDamageUUID(){
        return ItemAccessor.getBASE_ATTACK_DAMAGE_UUID();
    }

    private static UUID getAttackSpeedUUID(){
        return ItemAccessor.getBASE_ATTACK_SPEED_UUID();
    }

    private static double getWeaponAttackDamage(Tiers tier, double base) {
        double attackDamage = ConfigUtil.getBaseAttackDamage();
        double weaponDamage = WeaponRebalancing.calculateAttackDamage(tier, base);
        return weaponDamage - attackDamage;
    }

    private static double calculateAttackDamage(Tiers tier, double base) {
        switch (tier){
            case IRON -> {
                return base + 1.0D;
            }
            case DIAMOND -> {
                return base + 2.0D;
            }
            case NETHERITE -> {
                return base + 3.0D;
            }
            default -> { // wood, gold, stone
                return base;
            }
        }
    }

    private static double getHoeAttackSpeed(Tiers tier) {
        double attackSpeed = ConfigUtil.getBaseAttackSpeed();
        double weaponSpeed = calculateAttackSpeed(tier);
        return weaponSpeed - attackSpeed;
    }

    private static double calculateAttackSpeed(Tiers tier) {
        switch (tier){
            case STONE -> {
                return 2.5D;
            }
            case IRON -> {
                return 3.0D;
            }
            case GOLD, DIAMOND, NETHERITE -> {
                return 3.5D;
            }
            default -> { // wood
                return 2.0;
            }
        }
    }

    private static AttributeModifier getModifier(WeaponType weaponType, Tier tier, Attribute attribute, UUID uuid, String modifierName, double defaultValue, AttributeModifier.Operation operation) {
        return TIERED_WEAPON_ATTRIBUTES
                .computeIfAbsent(weaponType, k -> new HashMap<>())
                .computeIfAbsent(tier, k -> new HashMap<>())
                .computeIfAbsent(attribute, k ->
                        new AttributeModifier(uuid, modifierName, defaultValue, operation)
                );
    }

    private enum WeaponType {
        AXE,
        HOE,
        PICKAXE,
        SHOVEL,
        SWORD,
        TRIDENT
    }
}
