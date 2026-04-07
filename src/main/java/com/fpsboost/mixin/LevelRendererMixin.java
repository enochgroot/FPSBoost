package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import com.fpsboost.FPSBoostMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LevelRenderer")
public class LevelRendererMixin {

    @Inject(method = "addWeatherPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipWeather(CallbackInfo ci) {
        // VulkanMod replaces the OpenGL frame-graph render passes — skip
        if (FPSBoostMod.VULKAN_PRESENT) return;
        if (!FPSBoostConfig.enabled) return;
        if (FPSBoostConfig.skipUnderground && FPSBoostConfig.isUnderground()) ci.cancel();
    }

    @Inject(method = "addSkyPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipSky(CallbackInfo ci) {
        if (FPSBoostMod.VULKAN_PRESENT) return;
        if (!FPSBoostConfig.enabled) return;
        if (FPSBoostConfig.skipUnderground && FPSBoostConfig.isUnderground()) { ci.cancel(); return; }
        if (FPSBoostConfig.smartSky && FPSBoostConfig.isLookingDown()) ci.cancel();
    }

    @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
    private void fpsboost$skipClouds(CallbackInfo ci) {
        if (FPSBoostMod.VULKAN_PRESENT) return;
        if (!FPSBoostConfig.enabled) return;
        if (FPSBoostConfig.noClouds) { ci.cancel(); return; }
        if (FPSBoostConfig.skipUnderground && FPSBoostConfig.isUnderground()) ci.cancel();
    }
}
