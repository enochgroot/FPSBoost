package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderer")
public class EntityRendererMixin {

    private static int fpsboost$lodTick = 0;

    /**
     * LOD: reduce animation update frequency for distant entities.
     * extractRenderState is called every frame per entity — very expensive with many mobs.
     * By skipping updates for distant entities we save CPU without culling visibility.
     * Mixin allows omitting trailing params — (Entity, CallbackInfo) matches
     *   void extractRenderState(Entity, EntityRenderState, float)
     */
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void fpsboost$lodExtract(Entity entity, CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.entityLOD) return;
        if (entity instanceof Player) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        fpsboost$lodTick++;
        double dist2 = mc.player.distanceToSqr(entity);
        int id = entity.getId();

        // 32+ blocks: update only 1/3 frames (67% reduction)
        if (dist2 > 1024.0 && (fpsboost$lodTick + id) % 3 != 0) { ci.cancel(); return; }
        // 16-32 blocks: update only 1/2 frames (50% reduction)
        if (dist2 > 256.0 && (fpsboost$lodTick + id) % 2 != 0) ci.cancel();
    }

    /** No entity shadows — cancel raycast per entity */
    @Inject(method = "extractShadow", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noShadow(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noShadows) ci.cancel();
    }

    /** No nametags */
    @Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noNametag(Entity entity, double dist,
            CallbackInfoReturnable<Boolean> cir) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noNameTags) cir.setReturnValue(false);
    }
}
