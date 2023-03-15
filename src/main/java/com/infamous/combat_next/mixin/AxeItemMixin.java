package com.infamous.combat_next.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends DiggerItem {
    public AxeItemMixin(float attackDamage, float attackSpeed, Tier tier, TagKey<Block> blocks, Properties properties) {
        super(attackDamage, attackSpeed, tier, blocks, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity player) {
        stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }
}
