package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.VoidReturn;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "outOfWorld", at = @At("HEAD"), cancellable = true)
    private void handleOutOfWorld(CallbackInfo ci){
        if(this instanceof VoidReturn voidReturn && voidReturn.returnFromVoid()){
            ci.cancel();
        }
    }
}
