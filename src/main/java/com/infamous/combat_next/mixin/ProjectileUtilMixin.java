package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {

    @Redirect(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;"))
    private static AABB getEntityBoundingBoxForRenderer(Entity instance){
        return CombatUtil.getAttackableBoundingBox(instance);
    }

    @Redirect(method = "getEntityHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;F)Lnet/minecraft/world/phys/EntityHitResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;"))
    private static AABB getEntityBoundingBoxForProjectile(Entity instance){
        return CombatUtil.getAttackableBoundingBox(instance);
    }
}
