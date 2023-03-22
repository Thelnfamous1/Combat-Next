package com.infamous.combat_next.client;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.config.ShieldCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.Nullable;

public class ShieldIndicatorRenderer {
    /*
        private static final OptionInstance<ShieldIndicatorStatus> shieldIndicator = new OptionInstance<>(
                "options.shieldIndicator",
                OptionInstance.noTooltip(),
                OptionInstance.forOptionEnum(),
                new OptionInstance.Enum<>(Arrays.asList(ShieldIndicatorStatus.values()), Codec.INT.xmap(ShieldIndicatorStatus::byId, ShieldIndicatorStatus::getId)),
                ShieldIndicatorStatus.CROSSHAIR,
                (sis) -> {}
        );
         */
    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(CombatNext.MODID, "textures/gui/icons.png");
    public static final int HOTBAR_WIDTH = 182;
    public static final int HOTBAR_LEFT_OFFSET = 6;
    public static final int HOTBAR_RIGHT_OFFSET = 22;
    public static final int HOTBAR_ATTACK_INDICATOR_WIDTH = 18;
    public static final int HOTBAR_SHIELD_INDICATOR_X = 0;
    public static final int HOTBAR_SHIELD_INDICATOR_Y = 112;
    public static final int HOTBAR_SHIELD_INDICATOR_WIDTH = 18;
    public static final int HOTBAR_SHIELD_INDICATOR_HEIGHT = 18;
    public static final int HOTBAR_SHIELD_INDICATOR_DISABLED_X = 18;
    public static final int HOTBAR_SHIELD_INDICATOR_DISABLED_Y = 112;
    public static final int CROSSBAR_SHIELD_INDICATOR_X = 36;
    public static final int CROSSBAR_SHIELD_INDICATOR_Y = 112;
    public static final int CROSSBAR_SHIELD_INDICATOR_WIDTH = 16;
    public static final int CROSSBAR_SHIELD_INDICATOR_HEIGHT = 16;
    public static final int CROSSBAR_SHIELD_INDICATOR_DISABLED_X = 52;
    public static final int CROSSBAR_SHIELD_INDICATOR_DISABLED_Y = 112;
    public static final int CROSSHAIR_HEIGHT = 15;

    public static void renderCrosshair(ForgeGui gui, PoseStack poseStack, int screenWidth, int screenHeight) {
        Options options = Minecraft.getInstance().options;
        if (options.getCameraType().isFirstPerson()) {
            //noinspection ConstantConditions
            if (Minecraft.getInstance().gameMode.getPlayerMode() != GameType.SPECTATOR || canRenderCrosshairForSpectator(Minecraft.getInstance().hitResult)) {
                LocalPlayer player = Minecraft.getInstance().player;
                //noinspection ConstantConditions
                if (!options.renderDebug || options.hideGui || player.isReducedDebugInfo() || options.reducedDebugInfo().get()) {
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    if (ShieldCombatConfigs.getShieldIndicatorStatus().get() == ShieldIndicatorStatus.CROSSHAIR.getId()) {
                        //noinspection ConstantConditions
                        if(player.isHolding(CombatUtil::isShield)){
                            InteractionHand shieldHoldingHand = CombatUtil.getShieldHoldingHand(player);
                            int y = screenHeight / 2 + ShieldCombatConfigs.getShieldIndicatorCrosshairOffsetY().get();
                            int x = screenWidth / 2 - ShieldCombatConfigs.getShieldIndicatorCrosshairOffsetX().get();
                            if (player.isBlocking()) {
                                gui.blit(poseStack,
                                        x, y,
                                        CROSSBAR_SHIELD_INDICATOR_X, CROSSBAR_SHIELD_INDICATOR_Y,
                                        CROSSBAR_SHIELD_INDICATOR_WIDTH, CROSSBAR_SHIELD_INDICATOR_HEIGHT);
                            } else if (isShieldOnCooldown(player, shieldHoldingHand)) {
                                gui.blit(poseStack,
                                        x, y,
                                        CROSSBAR_SHIELD_INDICATOR_DISABLED_X, CROSSBAR_SHIELD_INDICATOR_DISABLED_Y,
                                        CROSSBAR_SHIELD_INDICATOR_WIDTH, CROSSBAR_SHIELD_INDICATOR_HEIGHT);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean canRenderCrosshairForSpectator(HitResult hitResult) {
        if (hitResult == null) {
            return false;
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)hitResult).getEntity() instanceof MenuProvider;
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitResult).getBlockPos();
            Level level = Minecraft.getInstance().level;
            //noinspection ConstantConditions
            return level.getBlockState(blockpos).getMenuProvider(level, blockpos) != null;
        } else {
            return false;
        }
    }

    private static boolean isShieldOnCooldown(Player player, InteractionHand shieldHoldingHand) {
        return player.getCooldowns().isOnCooldown(player.getItemInHand(shieldHoldingHand).getItem());
    }

    public static void renderHotbar(ForgeGui gui, PoseStack poseStack, int screenWidth, int screenHeight) {
        Player player = getCameraPlayer(Minecraft.getInstance());
        if (player != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            HumanoidArm mainArm = player.getMainArm();
            int midWidth = screenWidth / 2;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            if (ShieldCombatConfigs.getShieldIndicatorStatus().get() == ShieldIndicatorStatus.HOTBAR.getId()) {
                if(player.isHolding(CombatUtil::isShield)){
                    InteractionHand shieldHoldingHand = CombatUtil.getShieldHoldingHand(player);
                    int y = screenHeight - ShieldCombatConfigs.getShieldIndicatorHotbarOffsetY().get();
                    // render the indicator on the right of the hotbar
                    int x = midWidth + ShieldCombatConfigs.getShieldIndicatorHotbarRightOffsetX().get();
                    if (mainArm == HumanoidArm.LEFT) { // render the indicator on the left of the hotbar
                        x = midWidth - ShieldCombatConfigs.getShieldIndicatorHotbarLeftOffsetX().get();
                    }

                    RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    if(player.isBlocking()){
                        gui.blit(poseStack,
                                x, y,
                                HOTBAR_SHIELD_INDICATOR_X, HOTBAR_SHIELD_INDICATOR_Y,
                                HOTBAR_SHIELD_INDICATOR_WIDTH, HOTBAR_SHIELD_INDICATOR_HEIGHT);
                    } else if(isShieldOnCooldown(player, shieldHoldingHand)){
                        gui.blit(poseStack,
                                x, y,
                                HOTBAR_SHIELD_INDICATOR_DISABLED_X, HOTBAR_SHIELD_INDICATOR_DISABLED_Y,
                                HOTBAR_SHIELD_INDICATOR_WIDTH, HOTBAR_SHIELD_INDICATOR_HEIGHT);
                    }
                }
            }

            RenderSystem.disableBlend();
        }
    }

    @Nullable
    private static Player getCameraPlayer(Minecraft minecraft) {
        return !(minecraft.getCameraEntity() instanceof Player) ? null : (Player)minecraft.getCameraEntity();
    }
}
