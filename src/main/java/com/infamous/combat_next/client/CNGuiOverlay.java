package com.infamous.combat_next.client;

import com.infamous.combat_next.CombatNext;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import org.jetbrains.annotations.NotNull;

public enum CNGuiOverlay {
    SHIELD_INDICATOR_CROSSHAIR("shield_indicator_crosshair", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui)
        {
            gui.setupOverlayRenderState(true, false);
            //gui.setBlitOffset(-90);

            //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            //RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ShieldIndicatorRenderer.GUI_ICONS_LOCATION);
            //RenderSystem.enableBlend();
            ShieldIndicatorRenderer.renderCrosshair(gui, poseStack, screenWidth, screenHeight);
            RenderSystem.disableBlend();
        }
    }),
    SHIELD_INDICATOR_HOTBAR("shield_indicator_hotbar", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui)
        {
            gui.setupOverlayRenderState(true, false);
            if (gui.getMinecraft().gameMode.getPlayerMode() != GameType.SPECTATOR)
            {
                ShieldIndicatorRenderer.renderHotbar(gui, poseStack, screenWidth, screenHeight);
            }
        }
    });

    private final ResourceLocation id;
    final IGuiOverlay overlay;
    NamedGuiOverlay type;

    CNGuiOverlay(String id, IGuiOverlay overlay)
    {
        this.id = new ResourceLocation(CombatNext.MODID, id);
        this.overlay = overlay;
    }

    @NotNull
    public ResourceLocation id()
    {
        return id;
    }

    public NamedGuiOverlay type()
    {
        return type;
    }
}
