package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderDispatcher")
public class EntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void fpsboost$cull(
            T entity,
            double x, double y, double z,
            float yaw, float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            CallbackInfo ci) {

        if (!FPSBoostConfig.enabled || !FPSBoostConfig.entityCulling) return;

        // Skip rendering entities that are behind the player (simple dot-product cull)
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Don't cull the local player or players
        if (entity instanceof Player) return;

        double dx = entity.getX() - mc.player.getX();
        double dz = entity.getZ() - mc.player.getZ();
        double dist2 = dx * dx + dz * dz;

        // Cull entities more than 2 chunks beyond render distance
        double rdBlocks = (mc.options.renderDistance().get() + 2) * 16.0;
        if (dist2 > rdBlocks * rdBlocks) {
            ci.cancel();
            return;
        }

        // Simple angle cull: skip entities directly behind (> 160 degree angle from view)
        double yawRad = Math.toRadians(mc.player.getYRot());
        double lookX = -Math.sin(yawRad);
        double lookZ =  Math.cos(yawRad);
        double dot = dx * lookX + dz * lookZ;
        double len = Math.sqrt(dist2);
        // If entity is far and behind: cos(angle) < -0.8 means > ~143 degrees behind
        if (len > 8.0 && dist2 > 64 && dot / len < -0.8) {
            ci.cancel();
        }
    }
}
