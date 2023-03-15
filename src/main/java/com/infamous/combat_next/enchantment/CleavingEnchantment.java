package com.infamous.combat_next.enchantment;

import com.infamous.combat_next.CombatNext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CleavingEnchantment extends Enchantment {
    public static EnchantmentCategory AXE = EnchantmentCategory.create(
            new ResourceLocation(CombatNext.MODID, "axe").toString(),
            i -> i instanceof AxeItem);

    public CleavingEnchantment(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
        super(rarity, category, slots);
        CombatNext.LOGGER.info("Created CleavingEnchantment with EnchantmentCategory {}", category);
    }

    @Override
    public int getMinCost(int level) {
        return 1 + (level - 1) * 11;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public float getDamageBonus(int level, MobType type) {
        return 1.0F + level;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return !(enchantment instanceof DamageEnchantment);
    }
}
