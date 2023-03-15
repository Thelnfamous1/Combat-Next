package com.infamous.combat_next.util;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.mixin.ItemAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeaponRebalancing {
    private static final EnumMap<WeaponType, Map<Tier, Map<Attribute, AttributeModifier>>> TIERED_WEAPON_ATTRIBUTES = new EnumMap<>(WeaponType.class);
    private static final UUID ITEM_ATTACK_RANGE_MODIFIER_UUID = UUID.fromString("26cb07a3-209d-4110-8e10-1010243614c8");
    private static final String ATTACK_DAMAGE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_damage_modifier").toString();
    private static final AttributeModifier TRIDENT_ATTACK_DAMAGE_MODIFIER = new AttributeModifier(getAttackDamageUUID(), ATTACK_DAMAGE_MODIFIER_NAME, 6.0D, AttributeModifier.Operation.ADDITION);
    private static final String ATTACK_SPEED_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_speed_modifier").toString();
    private static final AttributeModifier TRIDENT_ATTACK_SPEED_MODIFIER = new AttributeModifier(getAttackSpeedUUID(), ATTACK_SPEED_MODIFIER_NAME, -2.0D, AttributeModifier.Operation.ADDITION);
    private static final String ATTACK_RANGE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_range_modifier").toString();
    private static final AttributeModifier TRIDENT_ATTACK_RANGE_MODIFIER = new AttributeModifier(ITEM_ATTACK_RANGE_MODIFIER_UUID, ATTACK_RANGE_MODIFIER_NAME, 1.0D, AttributeModifier.Operation.ADDITION);
    private static final double AXE_ATTACK_SPEED = -2.0D;
    private static final double HOE_ATTACK_RANGE = 1.0D;
    private static final double PICKAXE_ATTACK_DAMAGE = 2.0D;
    private static final double PICKAXE_ATTACK_SPEED = -1.5D;
    private static final double SHOVEL_ATTACK_SPEED = -2.0D;
    private static final double SWORD_ATTACK_SPEED = -1.0D;
    private static final double SWORD_ATTACK_RANGE = 0.5D;

    public static void modifyWeaponAttributes(ItemAttributeModifierEvent event, Item item) {
        if(item instanceof TieredItem tieredItem && tieredItem.getTier() instanceof Tiers tier){
            if(item instanceof AxeItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.AXE, tier, getAxeAttackDamage(tier)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.AXE, tier, AXE_ATTACK_SPEED));
            } else if(item instanceof HoeItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.HOE, tier, getHoeAttackDamage(tier)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.HOE, tier, getHoeAttackSpeed(tier)));
                event.addModifier(ForgeMod.ATTACK_RANGE.get(), getAttackRange(WeaponType.HOE, tier, HOE_ATTACK_RANGE));
            } else if(item instanceof PickaxeItem){
                if(tier == Tiers.WOOD || tier == Tiers.GOLD){
                    event.removeAttribute(Attributes.ATTACK_DAMAGE);
                    event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.PICKAXE, tier, PICKAXE_ATTACK_DAMAGE));
                }
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.PICKAXE, tier, PICKAXE_ATTACK_SPEED));
            } else if(item instanceof ShovelItem){
                event.removeAttribute(Attributes.ATTACK_DAMAGE);
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.SHOVEL, tier, getShovelAttackDamage(tier)));
                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.SHOVEL, tier, SHOVEL_ATTACK_SPEED));

            } else if(item instanceof SwordItem){
                if(tier != Tiers.WOOD && tier != Tiers.GOLD){
                    event.removeAttribute(Attributes.ATTACK_DAMAGE);
                    event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(WeaponType.SWORD, tier, getSwordAttackDamage(tier)));
                }
                event.removeAttribute(Attributes.ATTACK_SPEED);

                event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(WeaponType.SWORD, tier, SWORD_ATTACK_SPEED));
                event.addModifier(ForgeMod.ATTACK_RANGE.get(), getAttackRange(WeaponType.SWORD, tier, SWORD_ATTACK_RANGE));
            }
        } else if(item == Items.TRIDENT){
            event.removeAttribute(Attributes.ATTACK_DAMAGE);
            event.removeAttribute(Attributes.ATTACK_SPEED);
            event.addModifier(Attributes.ATTACK_DAMAGE, TRIDENT_ATTACK_DAMAGE_MODIFIER);
            event.addModifier(Attributes.ATTACK_SPEED, TRIDENT_ATTACK_SPEED_MODIFIER);
            event.addModifier(ForgeMod.ATTACK_RANGE.get(), TRIDENT_ATTACK_RANGE_MODIFIER);
        }
    }

    @NotNull
    private static AttributeModifier getAttackRange(WeaponType weaponType, Tiers tier, double defaultValue) {
        return getModifier(weaponType, tier, ForgeMod.ATTACK_RANGE.get(), ITEM_ATTACK_RANGE_MODIFIER_UUID, ATTACK_RANGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    @NotNull
    private static AttributeModifier getAttackSpeed(WeaponType weaponType, Tiers tier, double defaultValue) {
        return getModifier(weaponType, tier, Attributes.ATTACK_SPEED, getAttackSpeedUUID(), ATTACK_SPEED_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    @NotNull
    private static AttributeModifier getAttackDamage(WeaponType weaponType, Tiers tier, double defaultValue) {
        return getModifier(weaponType, tier, Attributes.ATTACK_DAMAGE, getAttackDamageUUID(), ATTACK_DAMAGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    private static double getSwordAttackDamage(Tiers tier) {
        switch (tier){
            case STONE -> {
                return 3.0D;
            }
            case DIAMOND -> {
                return 5.0D;
            }
            case NETHERITE -> {
                return 6.0D;
            }
            default -> {
                return 4.0D;
            }
        }
    }

    private static double getShovelAttackDamage(Tiers tier) {
        switch (tier){
            case WOOD, GOLD, STONE -> {
                return 1.0D;
            }
            case DIAMOND -> {
                return 3.0D;
            }
            case NETHERITE -> {
                return 4.0D;
            }
            default -> {
                return 2.0D;
            }
        }
    }

    private static double getAxeAttackDamage(Tiers tier) {
        switch (tier){
            case WOOD, GOLD, STONE -> {
                return 4.0D;
            }
            case DIAMOND -> {
                return 6.0D;
            }
            case NETHERITE -> {
                return 7.0D;
            }
            default -> {
                return 5.0D;
            }
        }
    }

    private static double getHoeAttackDamage(Tiers tier) {
        switch (tier){
            case WOOD, GOLD, STONE -> {
                return 1.0D;
            }
            case NETHERITE -> {
                return 3.0D;
            }
            default -> {
                return 2.0D;
            }
        }
    }

    private static double getHoeAttackSpeed(Tiers tier) {
        switch (tier){
            case DIAMOND, NETHERITE, GOLD -> {
                return -0.5D;
            }
            case STONE -> {
                return -1.5D;
            }
            case WOOD -> {
                return -2.0D;
            }
            default -> {
                return -1.0D;
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

    private static UUID getAttackDamageUUID(){
        return ItemAccessor.getBASE_ATTACK_DAMAGE_UUID();
    }

    private static UUID getAttackSpeedUUID(){
        return ItemAccessor.getBASE_ATTACK_SPEED_UUID();
    }

    private enum WeaponType {
        AXE,
        HOE,
        PICKAXE,
        SHOVEL,
        SWORD
    }
}
