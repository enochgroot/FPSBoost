package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LightTexture")
public class LightTextureMixin {

    private static int fpsboost$lightFrame = 0;

    /**
     * Throttle light texture GPU uploads.
     * updateLightTexture uploads a 16x16 texture every frame — at 500 FPS that's
     * 500 GPU uploads/sec. Reducing to every 2nd frame halves this with no visible impact
     * since light changes slowly (time-of-day, player movement).
     * Confirmed in 1.21.11 mappings: void updateLightTexture(float)
     */
    @Inject(method = "updateLightTexture", at = @At("HEAD"), cancellable = true)
    private void fpsboost$throttleLight(CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.lightThrottle) return;
        fpsboost$lightFrame++;
        // Skip every other frame
        if ((fpsboost$lightFrame & 1) == 1) ci.cancel();
    }
}
