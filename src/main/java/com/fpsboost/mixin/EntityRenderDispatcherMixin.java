package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderDispatcher")
public class EntityRenderDispatcherMixin {

    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private void fpsboost$shouldRender(
            Entity entity,
            Frustum frustum,
            double x, double y, double z,
            CallbackInfoReturnable<Boolean> cir) {

        if (!cir.getReturnValue()) return;
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.entityCulling) return;
        if (entity instanceof Player) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Use real captured frustum from LevelRenderer for accurate culling
        Frustum realFrustum = null;
        if (mc.levelRenderer != null) {
            realFrustum = mc.levelRenderer.getCapturedFrustum();
        }

        if (realFrustum != null) {
            AABB box = entity.getBoundingBox().inflate(0.1);
            if (!realFrustum.isVisible(box)) {
                cir.setReturnValue(false);
                return;
            }
        }

        // Extra: behind-camera cull at distance (beyond frustum check)
        double dx = entity.getX() - mc.player.getX();
        double dz = entity.getZ() - mc.player.getZ();
        double dist2 = dx * dx + dz * dz;
        if (dist2 > 64.0) {
            double yawRad = Math.toRadians(mc.player.getYRot());
            double len = Math.sqrt(dist2);
            double dot = (dx * -Math.sin(yawRad) + dz * Math.cos(yawRad)) / len;
            if (dot < -0.87) cir.setReturnValue(false);
        }
    }
}
