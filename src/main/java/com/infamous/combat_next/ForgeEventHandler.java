package com.infamous.combat_next;

import com.infamous.combat_next.capability.PlayerCapability;
import com.infamous.combat_next.client.ClientCombatUtil;
import com.infamous.combat_next.config.*;
import com.infamous.combat_next.data.CNTags;
import com.infamous.combat_next.network.CNNetwork;
import com.infamous.combat_next.network.ClientboundConfigSyncPacket;
import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.config.WeaponRebalancing;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CombatNext.MODID)
public class ForgeEventHandler {

    public static final String ATTRIBUTE_MODIFIERS_TAG = "AttributeModifiers";

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onAttributeModification(ItemAttributeModifierEvent event){
        if(MeleeCombatConfigs.getWeaponRebalancing().get()){
            ItemStack stack = event.getItemStack();
            Item item = stack.getItem();

            EquipmentSlot slot = event.getSlotType();
            EquipmentSlot desired = LivingEntity.getEquipmentSlotForItem(stack);
            if(slot != desired) return;
            //noinspection ConstantConditions
            if(stack.hasTag() && stack.getTag().contains(ATTRIBUTE_MODIFIERS_TAG)) return;

            WeaponRebalancing.modifyWeaponAttributes(event, item);
        }
    }

    @SubscribeEvent
    static void onDamage(LivingDamageEvent event){
        DamageSource source = event.getSource();
        LivingEntity victim = event.getEntity();

        if(source.isProjectile() && RangedCombatConfigs.getProjectileNoIFrames().get()){
            victim.invulnerableTime = 0;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onShieldBlock(ShieldBlockEvent event){
        if(!event.isCanceled()){
            LivingEntity user = event.getEntity();
            ItemStack stack = user.getUseItem();
            float blockedDamage = event.getBlockedDamage();
            DamageSource source = event.getDamageSource();

            if(CombatUtil.isShield(stack) && ShieldCombatConfigs.getShieldReduceDamageBlocked().get()){
                if(!source.isProjectile() && !source.isExplosion()){
                    ShieldCombatValues.getShieldStrength(stack.getItem()).ifPresent(
                            shieldStrength -> event.setBlockedDamage(Mth.clamp(shieldStrength, 0, blockedDamage))
                    );
                }
            }
        }
    }


    @SubscribeEvent
    static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
        if(event.isCanceled()) return;

        ItemStack itemStack = event.getItemStack();
        if(itemStack.is(CNTags.SLOW_THROWABLES)){
            ItemCooldowns cooldowns = event.getEntity().getCooldowns();
            if(!cooldowns.isOnCooldown(itemStack.getItem())){
                cooldowns.addCooldown(itemStack.getItem(), RangedCombatConfigs.getThrowableItemCooldown().get());
            }
        }
    }

    @SubscribeEvent
    static void onLivingHurt(LivingHurtEvent event){
        if(!event.isCanceled()){
            LivingEntity victim = event.getEntity();
            if(event.getSource().getDirectEntity() instanceof Player player){
                victim.invulnerableTime = (int) player.getCurrentItemAttackStrengthDelay();
            }
        }
    }

    @SubscribeEvent
    static void onLivingAttackEvent(LivingAttackEvent event){
        if(!event.isCanceled()){
            if(CombatUtil.canInterruptConsumption(event.getSource()) && GeneralCombatConfigs.getAttacksInterruptConsumption().get()){
                LivingEntity victim = event.getEntity();
                ItemStack useItem = victim.getUseItem();
                UseAnim useAnimation = useItem.getUseAnimation();
                if(victim.isUsingItem() && CombatUtil.isEatOrDrink(useAnimation)){
                    victim.stopUsingItem();
                }
            }
        }
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        List<Component> toolTips = event.getToolTip();
        if(MeleeCombatConfigs.getWeaponRebalancing().get()){
            Player player = event.getEntity();
            if(player == null) return;
            WeaponRebalancing.adjustItemTooltip(player, stack, toolTips);
        }
        if(CombatUtil.isShield(stack)){
            if(ShieldCombatConfigs.getShieldReduceDamageBlocked().get()){
                ShieldCombatValues.getShieldStrength(stack.getItem()).filter(shieldStrength -> shieldStrength > 0).ifPresent(
                        shieldStrength -> toolTips.add(CombatUtil.getShieldModifierTooltip(AttributeModifier.Operation.ADDITION, (double) shieldStrength, CombatUtil.SHIELD_STRENGTH_DESCRIPTION_ID))
                );
            }
            if(ShieldCombatConfigs.getShieldReduceKnockback().get()){
                ShieldCombatValues.getKnockbackResistance(stack.getItem()).filter(knockbackResistance -> knockbackResistance > 0.0F).ifPresent(
                        knockbackResistance -> toolTips.add(CombatUtil.getShieldModifierTooltip(AttributeModifier.Operation.ADDITION, (double)knockbackResistance * 10.0D, Attributes.KNOCKBACK_RESISTANCE.getDescriptionId()))
                );
            }
        }
    }

    @SubscribeEvent
    static void onEmptyLeftClick(PlayerInteractEvent.LeftClickEmpty event){
        CombatUtil.attackEmpty(event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onPlayerAttack(AttackEntityEvent event){
        if(!event.isCanceled()){
            Player player = event.getEntity();
            if(CombatUtil.onAttackCooldown(player, 0.5F)){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        if(!event.isCanceled()){
            if(MeleeCombatConfigs.getAttackThroughNonSolidBlocks().get()){
                Player player = event.getEntity();
                boolean hitThroughBlock = CombatUtil.hitThroughBlock(event.getLevel(), event.getPos(), player,
                        player.isLocalPlayer() ? ClientCombatUtil::hitEntity : CombatUtil::hitEntity);
                event.setCanceled(hitThroughBlock);
                if(hitThroughBlock) event.setUseBlock(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    static void onCriticalHit(CriticalHitEvent event){
        if(!event.isVanillaCritical()){
            if(MeleeCombatConfigs.getAttackCriticalWhenSprinting().get()){
                Player player = event.getEntity();
                boolean sprintCritical = CombatUtil.isSprintCritical(player, event.getTarget());
                if(sprintCritical){
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }

    @SubscribeEvent
    static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.END){
            if(MeleeCombatConfigs.getAttackSupercharge().get()){
                boolean supercharged = CombatUtil.isSupercharged(event.player, 0.5F);
                CombatUtil.handleBonusAttackReach(event.player, supercharged && !event.player.isCrouching());
            }
            if(ShieldCombatConfigs.getShieldReduceKnockback().get()){
                CombatUtil.handleShieldKnockbackResistance(event.player, event.player.isBlocking());
            }
        }
    }

    @SubscribeEvent
    static void onServerStarting(ServerAboutToStartEvent event){
        CombatUtil.applySyncedConfigs();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        ServerPlayer player = (ServerPlayer) event.getEntity();
        CNNetwork.syncToPlayer(player, ClientboundConfigSyncPacket.createConfigSyncPacket());
        LazyOptional<PlayerCapability> playerCap = player.getCapability(PlayerCapability.INSTANCE);
        boolean playedBefore = playerCap.map(PlayerCapability::getPlayedBeforeAndUpdate).orElse(false);
        CombatNext.LOGGER.info("{} Player {} detected, {}", playedBefore ? "Returning" : "New", player.getName().getString(), playedBefore ? "welcome back!" : "welcome to Combat Next!");
        if(MeleeCombatConfigs.getPlayerAttributeChange().get()){
            if(!playedBefore || !MeleeCombatConfigs.getPlayerAttributeChangeFirstLogin().get()){
                CombatUtil.adjustAttributeBaseValue(player, Attributes.ATTACK_DAMAGE, CombatUtil.getBaseAttackDamage());
                CombatUtil.adjustAttributeBaseValue(player, ForgeMod.ATTACK_RANGE.get(), CombatUtil.getBaseAttackRange());
            }
        }
    }

    @SubscribeEvent
    static void onPlayerClone(PlayerEvent.Clone event){
        if(MeleeCombatConfigs.getPlayerAttributeChange().get()){
            ServerPlayer player = (ServerPlayer) event.getEntity();
            CombatUtil.adjustAttributeBaseValue(player, Attributes.ATTACK_DAMAGE, CombatUtil.getBaseAttackDamage());
            CombatUtil.adjustAttributeBaseValue(player, ForgeMod.ATTACK_RANGE.get(), CombatUtil.getBaseAttackRange());
        }
    }

}
