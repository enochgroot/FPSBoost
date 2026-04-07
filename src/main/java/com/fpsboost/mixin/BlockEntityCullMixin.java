package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher")
public class BlockEntityCullMixin {

    /**
     * Cull block entities (chests, furnaces, signs, etc.) that are:
     * 1. Beyond blockEntityRenderDist blocks, OR
     * 2. Outside the camera frustum
     * Returning null from tryExtractRenderState skips rendering (expected by vanilla).
     * Confirmed in 1.21.11 mappings: tryExtractRenderState(BlockEntity, ...)
     */
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    private void fpsboost$cullBlockEntity(BlockEntity blockEntity,
            CallbackInfoReturnable<?> cir) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.blockEntityCulling) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        BlockPos pos = blockEntity.getBlockPos();
        double dx = pos.getX() + 0.5 - mc.player.getX();
        double dy = pos.getY() + 0.5 - mc.player.getY();
        double dz = pos.getZ() + 0.5 - mc.player.getZ();
        double dist2 = dx*dx + dy*dy + dz*dz;
        double cap = FPSBoostConfig.blockEntityRenderDist;

        // Distance cull
        if (dist2 > cap * cap) { cir.setReturnValue(null); return; }

        // Frustum cull (if within distance but out of view)
        if (mc.levelRenderer != null) {
            Frustum frustum = mc.levelRenderer.getCapturedFrustum();
            if (frustum != null) {
                AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(),
                                   pos.getX()+1, pos.getY()+1, pos.getZ()+1);
                if (!frustum.isVisible(box)) cir.setReturnValue(null);
            }
        }
    }
}
