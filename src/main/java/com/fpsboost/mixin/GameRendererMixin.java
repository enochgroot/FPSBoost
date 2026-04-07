package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// These are CPU-side calculations — fully compatible with VulkanMod
@Mixin(targets = "net.minecraft.client.renderer.GameRenderer")
public class GameRendererMixin {

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noViewBob(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noViewBob) ci.cancel();
    }

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noHand(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noHand) ci.cancel();
    }
}
