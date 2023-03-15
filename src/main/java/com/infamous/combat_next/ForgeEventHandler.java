package com.infamous.combat_next;

import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.util.WeaponRebalancing;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CombatNext.MODID)
public class ForgeEventHandler {

    public static final String ATTRIBUTE_MODIFIERS_TAG = "AttributeModifiers";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onAttributeModification(ItemAttributeModifierEvent event){
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        EquipmentSlot slot = event.getSlotType();
        EquipmentSlot desired = LivingEntity.getEquipmentSlotForItem(stack);
        if(slot != desired) return;
        if(stack.hasTag() && stack.getTag().contains(ATTRIBUTE_MODIFIERS_TAG)) return;

        WeaponRebalancing.modifyWeaponAttributes(event, item);
    }

    @SubscribeEvent
    static void onDamage(LivingDamageEvent event){
        DamageSource source = event.getSource();
        LivingEntity victim = event.getEntity();

        if(source.isProjectile()){
            victim.invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    static void onShieldBlock(ShieldBlockEvent event){
        LivingEntity user = event.getEntity();
        ItemStack stack = user.getUseItem();
        float amount = event.getBlockedDamage();
        DamageSource source = event.getDamageSource();

        if(stack.is(Items.SHIELD)){
            if(!source.isProjectile() && (!source.isExplosion() || source.getEntity() == user)){
                event.setBlockedDamage(Math.min(5.0F, amount));
            }
        }
    }


    @SubscribeEvent
    static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        if(event.isCanceled()) return;

        ItemStack itemStack = event.getItemStack();
        if(itemStack.is(Items.EGG) || itemStack.is(Items.SNOWBALL)){
            ItemCooldowns cooldowns = event.getEntity().getCooldowns();
            if(!cooldowns.isOnCooldown(itemStack.getItem())){
                cooldowns.addCooldown(itemStack.getItem(), 4);
            }
        }
    }

    @SubscribeEvent
    static void onLivingAttackEvent(LivingAttackEvent event){
        if(!event.isCanceled()){
            if(CombatUtil.canInterruptConsumption(event.getSource())){
                LivingEntity victim = event.getEntity();
                ItemStack useItem = victim.getUseItem();
                UseAnim useAnimation = useItem.getUseAnimation();
                if(victim.isUsingItem() && useAnimation == UseAnim.EAT || useAnimation == UseAnim.DRINK){
                    victim.stopUsingItem();
                }
            }
        }
    }
}
