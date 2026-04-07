package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderDispatcher")
public class EntityRenderDispatcherMixin {

    /**
     * Inject at RETURN of shouldRender() — vanilla frustum check runs first.
     * We add extra behind-camera culling on top without breaking vanilla behavior.
     * Confirmed method signature from Mojang 1.21.11 mappings:
     *   boolean shouldRender(Entity, Frustum, double, double, double)
     */
    @Inject(method = "shouldRender",
            at = @At("RETURN"),
            cancellable = true)
    private void fpsboost$shouldRender(
            Entity entity,
            Frustum frustum,
            double x, double y, double z,
            CallbackInfoReturnable<Boolean> cir) {

        // If vanilla already culled it, leave that decision in place
        if (!cir.getReturnValue()) return;
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.entityCulling) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Never cull players (local or others)
        if (entity instanceof Player) return;

        double dx = entity.getX() - mc.player.getX();
        double dz = entity.getZ() - mc.player.getZ();
        double dist2 = dx * dx + dz * dz;

        // Only apply behind-camera cull at >8 blocks
        if (dist2 < 64.0) return;

        // dot product of entity direction vs player look direction
        double yawRad = Math.toRadians(mc.player.getYRot());
        double lookX = -Math.sin(yawRad);
        double lookZ =  Math.cos(yawRad);
        double len = Math.sqrt(dist2);
        double dot = (dx * lookX + dz * lookZ) / len;

        // cos(150°) ≈ -0.87 — cull entities more than 150° behind the player
        if (dot < -0.87) {
            cir.setReturnValue(false);
        }
    }
}
