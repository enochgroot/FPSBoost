package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.GameRenderer")
public class GameRendererMixin {

    /** No view bob — skip camera sway calculation every frame */
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noViewBob(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noViewBob) ci.cancel();
    }

    /**
     * No hand model — skip the entire hand+item render pass.
     * renderItemInHand renders the held item model with lighting/transforms.
     * Confirmed in 1.21.11 mappings: void renderItemInHand(float, boolean, Matrix4f)
     * Using just (CallbackInfo ci) — Mixin allows omitting trailing params.
     */
    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noHand(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noHand) ci.cancel();
    }
}
