package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @Shadow protected abstract boolean isAcceptibleReturnOwner();

    @Shadow public int clientSideReturnTridentTickCount;

    @Shadow @Final private static EntityDataAccessor<Byte> ID_LOYALTY;

    @Shadow private ItemStack tridentItem;

    @Override
    public void outOfWorld() {
        if(!this.returnFromVoid()){
            super.outOfWorld();
        }
    }

    private boolean returnFromVoid() {
        int loyalty = this.entityData.get(ID_LOYALTY);
        if(loyalty > 0) {
            if (!this.isAcceptibleReturnOwner()) {
                return false;
            } else {
                this.setNoPhysics(true);
                Vec3 vectorToEye = this.position().vectorTo(this.getEyePosition());
                this.setPosRaw(this.getX(), this.getY() + vectorToEye.y * 0.015D * loyalty, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double loyaltyScale = 0.05D * loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vectorToEye.normalize().scale(loyaltyScale)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
                return true;
            }
        } else{
            return false;
        }
    }

    @ModifyVariable(method = "onHitEntity", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    private float modifyEnchantmentDamage(float original, EntityHitResult entityHitResult){
        return CombatUtil.recalculateDamageBonus(this.tridentItem, original, entityHitResult.getEntity());
    }
}
