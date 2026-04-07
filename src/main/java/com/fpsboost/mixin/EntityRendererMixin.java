package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderer")
public class EntityRendererMixin {

    /**
     * Cancel shadow extraction when noShadows is ON.
     * extractShadow does raycasting per entity to find ground — very expensive.
     * Confirmed in 1.21.11 mappings:
     *   void extractShadow(EntityRenderState, Minecraft, Level)
     */
    @Inject(method = "extractShadow", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noShadow(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noShadows) ci.cancel();
    }

    /**
     * Suppress nametag rendering when noNameTags is ON.
     * Confirmed in 1.21.11 mappings:
     *   boolean shouldShowName(Entity, double)
     */
    @Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noNametag(Entity entity, double dist,
            CallbackInfoReturnable<Boolean> cir) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noNameTags) {
            cir.setReturnValue(false);
        }
    }
}
