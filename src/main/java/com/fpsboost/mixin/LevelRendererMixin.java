package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LevelRenderer")
public class LevelRendererMixin {

    /**
     * Skip weather (rain/snow) when player is underground.
     * Rain/snow is expensive: per-column geometry + particle spawning.
     * Confirmed method from 1.21.11 mappings: addWeatherPass(FrameGraphBuilder, GpuBufferSlice)
     */
    @Inject(method = "addWeatherPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipWeather(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.skipUnderground) return;
        if (FPSBoostConfig.isUnderground()) ci.cancel();
    }

    /**
     * Skip sky rendering when underground (no sky visible).
     * Sky geometry + sun/moon/star calculations.
     * Confirmed method from 1.21.11 mappings: addSkyPass(FrameGraphBuilder, Camera, GpuBufferSlice)
     */
    @Inject(method = "addSkyPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipSky(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.skipUnderground) return;
        if (FPSBoostConfig.isUnderground()) ci.cancel();
    }

    /**
     * Skip cloud rendering when underground.
     * Confirmed method from 1.21.11 mappings: addCloudsPass(FrameGraphBuilder, CloudStatus, Vec3)
     */
    @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipClouds(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.skipUnderground) return;
        if (FPSBoostConfig.isUnderground()) ci.cancel();
    }
}
