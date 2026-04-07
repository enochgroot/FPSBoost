package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LevelRenderer")
public class LevelRendererMixin {

    @Inject(method = "addWeatherPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipWeather(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled) return;
        if (FPSBoostConfig.skipUnderground && FPSBoostConfig.isUnderground()) ci.cancel();
    }

    /**
     * Smart sky: skip sky rendering when:
     * - Underground (skipUnderground), OR
     * - Player is looking down >30 degrees (smartSky)
     * Sun/moon/star geometry is irrelevant when you can't see the sky.
     */
    @Inject(method = "addSkyPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipSky(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled) return;
        if (FPSBoostConfig.skipUnderground && FPSBoostConfig.isUnderground()) { ci.cancel(); return; }
        if (FPSBoostConfig.smartSky && FPSBoostConfig.isLookingDown()) ci.cancel();
    }

    @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipClouds(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled) return;
        if (FPSBoostConfig.noClouds) { ci.cancel(); return; }
        if (FPSBoostConfig.skipUnderground && FPSBoostConfig.isUnderground()) ci.cancel();
    }
}
