package com.infamous.combat_next;

import com.infamous.combat_next.client.ClientCombatUtil;
import com.infamous.combat_next.config.ConfigUtil;
import com.infamous.combat_next.network.CNNetwork;
import com.infamous.combat_next.network.ClientboundConfigSyncPacket;
import com.infamous.combat_next.util.CombatExtensions;
import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.util.WeaponRebalancing;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;

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
        //noinspection ConstantConditions
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onShieldBlock(ShieldBlockEvent event){
        if(!event.isCanceled()){
            LivingEntity user = event.getEntity();
            ItemStack stack = user.getUseItem();
            float amount = event.getBlockedDamage();
            DamageSource source = event.getDamageSource();
            CombatExtensions.cast(user).setLastBlockedDamageSource(source);

            if(stack.is(Items.SHIELD)){
                if(!source.isProjectile() && (!source.isExplosion() || source.getEntity() == user)){
                    event.setBlockedDamage(Math.min(ConfigUtil.getShieldMaxBlockedDamage(), amount));
                }
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
                cooldowns.addCooldown(itemStack.getItem(), ConfigUtil.getThrowableItemCooldown());
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

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event){
        Player player = event.getEntity();
        if(player == null) return;
        ItemStack stack = event.getItemStack();

        List<Component> toolTips = event.getToolTip();
        int modifierToolTipIndex = -1;
        Iterator<AttributeModifier> modifierIterator = null;
        for(int i = 0; i < toolTips.size(); i++){
            Component toolTip = toolTips.get(i);
            ComponentContents contents = toolTip.getContents();
            String toolTipString = contents.toString();

            if(i == modifierToolTipIndex){
                if(toolTipString.contains(ForgeMod.ATTACK_RANGE.get().getDescriptionId())){
                    if(modifierIterator.hasNext()){
                        AttributeModifier modifier = modifierIterator.next();
                        if(modifier.getId() == WeaponRebalancing.ITEM_ATTACK_RANGE_MODIFIER_UUID){
                            Component replacement = createEqualRangeModifier(player, modifier);
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
                    modifierIterator = stack.getAttributeModifiers(slot).get(ForgeMod.ATTACK_RANGE.get()).iterator();
                    break;
                }
            }
        }
    }

    private static MutableComponent createEqualRangeModifier(Player player, AttributeModifier modifier) {
        return Component.literal(" ")
                .append(Component.translatable("attribute.modifier.equals." + modifier.getOperation().toValue(),
                        ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getRangeModifierValue(modifier, player)),
                        Component.translatable(ForgeMod.ATTACK_RANGE.get().getDescriptionId())).withStyle(ChatFormatting.DARK_GREEN));
    }

    private static double getRangeModifierValue(AttributeModifier modifier, Player player){
        double amount = modifier.getAmount();
        amount += player.getAttributeBaseValue(ForgeMod.ATTACK_RANGE.get());
        return amount;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onKnockback(LivingKnockBackEvent event){
        if(!event.isCanceled()){
            LivingEntity victim = event.getEntity();
            DamageSource lastBlockedDamageSource = CombatExtensions.cast(victim).getLastBlockedDamageSource();
            if(lastBlockedDamageSource != null && victim.isDamageSourceBlocked(lastBlockedDamageSource)){
                event.setStrength(event.getStrength() * ConfigUtil.getShieldKnockbackScale());
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
            Player player = event.getEntity();
            boolean hitThroughBlock = CombatUtil.hitThroughBlock(event.getLevel(), event.getPos(), player,
                    player.isLocalPlayer() ? ClientCombatUtil::hitEntity : CombatUtil::hitEntity);
            event.setCanceled(hitThroughBlock);
            if(hitThroughBlock) event.setUseBlock(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    static void onCriticalHit(CriticalHitEvent event){
        if(!event.isVanillaCritical()){
            Player player = event.getEntity();
            boolean sprintCritical = CombatUtil.isSprintCritical(player, event.getTarget());
            if(sprintCritical){
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.END){
            boolean supercharged = CombatUtil.isSupercharged(event.player, 0.5F);
            CombatUtil.handleBonusReach(event.player, supercharged && !event.player.isCrouching());
        }
    }

    @SubscribeEvent
    static void onServerStarting(ServerAboutToStartEvent event){
        CombatUtil.applySyncedConfigs();
    }

    @SubscribeEvent
    static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        CNNetwork.syncToPlayer((ServerPlayer) event.getEntity(), ClientboundConfigSyncPacket.createConfigSyncPacket());
    }

}
