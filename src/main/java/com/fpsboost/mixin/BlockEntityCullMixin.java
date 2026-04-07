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

    // Use Object (not wildcard) for CallbackInfoReturnable to avoid Mixin runtime type errors
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    private void fpsboost$cullBlockEntity(BlockEntity blockEntity,
            CallbackInfoReturnable<Object> cir) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.blockEntityCulling) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        BlockPos pos = blockEntity.getBlockPos();
        double dx = pos.getX() + 0.5 - mc.player.getX();
        double dy = pos.getY() + 0.5 - mc.player.getY();
        double dz = pos.getZ() + 0.5 - mc.player.getZ();
        double dist2 = dx*dx + dy*dy + dz*dz;
        double cap = FPSBoostConfig.blockEntityRenderDist;

        if (dist2 > cap * cap) { cir.setReturnValue(null); return; }

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
