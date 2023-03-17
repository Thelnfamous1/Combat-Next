package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatMinecraft;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "pick", at = @At("STORE"))
    private EntityHitResult getEntityHitResult(EntityHitResult entityHitResult){
        Entity cameraEntity = this.minecraft.getCameraEntity();
        if (entityHitResult != null && cameraEntity != null) {
            //noinspection ConstantConditions
            double blockReach = this.minecraft.gameMode.getPickRange();
            Vec3 eyePosition = cameraEntity.getEyePosition(1.0F);
            double reach = blockReach;
            //noinspection ConstantConditions
            double entityReach = this.minecraft.player.getAttackRange();
            if (this.minecraft.gameMode.hasFarPickRange()) {
                reach = Math.max(blockReach, entityReach); // Pick entities with the max of the reach distance and attack range.
            }

            if(CombatUtil.isValidHit(entityHitResult, eyePosition, Mth.square(reach), Mth.square(entityReach))){
                CombatMinecraft.cast(this.minecraft).setEntityHitResult(entityHitResult);
            }
        }
        return entityHitResult;
    }
}
