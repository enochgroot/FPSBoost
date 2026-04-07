package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void fpsboost$cull(T entity, float yaw, float partialTick,
            PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
            CallbackInfo ci) {

        if (!FPSBoostConfig.enabled || !FPSBoostConfig.entityCulling) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Never cull the local player or other players
        if (entity instanceof Player) return;

        double dx = entity.getX() - mc.player.getX();
        double dz = entity.getZ() - mc.player.getZ();
        double dist2 = dx * dx + dz * dz;

        // Cull if beyond render distance + 2 chunks
        double rdBlocks = (mc.options.renderDistance().get() + 2) * 16.0;
        if (dist2 > rdBlocks * rdBlocks) { ci.cancel(); return; }

        // Cull entities directly behind the player at distance > 8 blocks
        if (dist2 > 64.0) {
            double yawRad = Math.toRadians(mc.player.getYRot());
            double lx = -Math.sin(yawRad), lz = Math.cos(yawRad);
            double len = Math.sqrt(dist2);
            double dot = (dx * lx + dz * lz) / len;
            // dot < -0.87 means > ~150 degrees behind
            if (dot < -0.87) ci.cancel();
        }
    }
}
