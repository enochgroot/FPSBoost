package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderDispatcher")
public class EntityRenderDispatcherMixin {

    /** Static frustum set each frame by WorldRenderEvents.AFTER_SETUP */
    public static Frustum currentFrustum = null;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void fpsboost$cull(
            T entity, double x, double y, double z,
            float yaw, float partialTick,
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            net.minecraft.client.renderer.MultiBufferSource bufferSource,
            int packedLight,
            org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci2) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.entityCulling) return;
        if (currentFrustum == null) return;
        AABB box = entity.getBoundingBox().inflate(0.5);
        if (!currentFrustum.isVisible(box)) {
            ci2.cancel();
        }
    }
}
