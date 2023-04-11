package com.infamous.combat_next.config;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.mixin.ItemAccessor;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.*;

public class WeaponRebalancing {
    static final Map<Item, Map<Attribute, AttributeModifier>> WEAPON_ATTRIBUTE_MODIFIERS = new HashMap<>();
    private static final UUID ITEM_ATTACK_RANGE_MODIFIER_UUID = UUID.fromString("26cb07a3-209d-4110-8e10-1010243614c8");
    private static final String ATTACK_DAMAGE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_damage_modifier").toString();
    private static final String ATTACK_SPEED_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_speed_modifier").toString();
    private static final String ATTACK_RANGE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "weapon_attack_range_modifier").toString();

    public static void modifyWeaponAttributes(ItemAttributeModifierEvent event, Item item) {
        MeleeCombatValues.getAttackDamage(item).ifPresent(attackDamage -> {
            findAndRemoveVanillaModifier(event, Attributes.ATTACK_DAMAGE, ItemAccessor.getBASE_ATTACK_DAMAGE_UUID());
            event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(item, attackDamage - CombatUtil.getBaseAttackDamage()));
        });
        MeleeCombatValues.getAttackSpeed(item).ifPresent(attackSpeed -> {
            findAndRemoveVanillaModifier(event, Attributes.ATTACK_SPEED, ItemAccessor.getBASE_ATTACK_SPEED_UUID());
            event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(item, attackSpeed - CombatUtil.getBaseAttackSpeed()));
        });
        MeleeCombatValues.getAttackRange(item).ifPresent(attackRange -> {
            event.removeAttribute(ForgeMod.ENTITY_REACH.get());
            event.addModifier(ForgeMod.ENTITY_REACH.get(), getAttackRange(item, attackRange - CombatUtil.getBaseAttackRange()));
        });
    }

    private static void findAndRemoveVanillaModifier(ItemAttributeModifierEvent event, Attribute attribute, UUID baseUUID) {
        event.getOriginalModifiers()
                .get(attribute)
                .stream()
                .filter(modifier -> modifier.getId() == baseUUID) // we don't use "equals" because vanilla enforces a direct memory address comparison
                .findAny()
                .ifPresent(modifier -> event.removeModifier(attribute, modifier));
    }

    private static AttributeModifier getAttackDamage(Item item, double defaultValue) {
        return getModifier(item, Attributes.ATTACK_DAMAGE, ItemAccessor.getBASE_ATTACK_DAMAGE_UUID(), ATTACK_DAMAGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    private static AttributeModifier getAttackRange(Item item, double defaultValue) {
        return getModifier(item, ForgeMod.ENTITY_REACH.get(), ITEM_ATTACK_RANGE_MODIFIER_UUID, ATTACK_RANGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    private static AttributeModifier getAttackSpeed(Item item, double defaultValue) {
        return getModifier(item, Attributes.ATTACK_SPEED, ItemAccessor.getBASE_ATTACK_SPEED_UUID(), ATTACK_SPEED_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADDITION);
    }

    private static AttributeModifier getModifier(Item item, Attribute attribute, UUID uuid, String modifierName, double defaultValue, AttributeModifier.Operation operation) {
        return WEAPON_ATTRIBUTE_MODIFIERS
                .computeIfAbsent(item, k -> new HashMap<>())
                .computeIfAbsent(attribute, k ->
                        new AttributeModifier(uuid, modifierName, defaultValue, operation)
                );
    }

    public static void adjustItemTooltip(Player player, ItemStack stack, List<Component> toolTips) {
        if(MeleeCombatValues.getAttackRange(stack.getItem()).isPresent()){
            int modifierToolTipIndex = -1;
            Iterator<AttributeModifier> modifierIterator = null;
            for(int i = 0; i < toolTips.size(); i++){
                Component toolTip = toolTips.get(i);
                ComponentContents contents = toolTip.getContents();
                String toolTipString = contents.toString();

                if(i == modifierToolTipIndex){
                    if(toolTipString.contains(ForgeMod.ENTITY_REACH.get().getDescriptionId())){
                        if(modifierIterator.hasNext()){
                            AttributeModifier modifier = modifierIterator.next();
                            if(modifier.getId() == ITEM_ATTACK_RANGE_MODIFIER_UUID){
                                Component replacement = createEqualAttackRangeModifier(player, modifier);
                                toolTips.set(i, replacement);
                            }
                        }
                    }
                    modifierToolTipIndex++;
                }
                if(toolTipString.contains("item.modifiers.")){
                    for(EquipmentSlot slot : EquipmentSlot.values()){
                        if(!toolTipString.contains(slot.getName())) continue;

                        modifierToolTipIndex = i + 1;
                        modifierIterator = stack.getAttributeModifiers(slot).get(ForgeMod.ENTITY_REACH.get()).iterator();
                        break;
                    }
                }
            }
        }
    }

    private static MutableComponent createEqualAttackRangeModifier(Player player, AttributeModifier attackRangeModifier) {
        return Component.literal(" ")
                .append(Component.translatable("attribute.modifier.equals." + attackRangeModifier.getOperation().toValue(),
                        ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getTotalAttackRange(attackRangeModifier, player)),
                        Component.translatable(ForgeMod.ENTITY_REACH.get().getDescriptionId())).withStyle(ChatFormatting.DARK_GREEN));
    }

    private static double getTotalAttackRange(AttributeModifier modifier, Player player){
        double amount = modifier.getAmount();
        amount += player.getAttributeBaseValue(ForgeMod.ENTITY_REACH.get());
        return amount;
    }

    static void clearWeaponRebalancingCache() {
        WEAPON_ATTRIBUTE_MODIFIERS.clear();
        CombatNext.LOGGER.info("Cleared previously cached WEAPON_ATTRIBUTE_MODIFIERS!");
    }
}
