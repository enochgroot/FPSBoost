package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import com.fpsboost.FPSBoostMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LightTexture")
public class LightTextureMixin {

    private static int fpsboost$lightFrame = 0;

    @Inject(method = "updateLightTexture", at = @At("HEAD"), cancellable = true)
    private void fpsboost$throttleLight(CallbackInfo ci) {
        // VulkanMod handles GPU texture uploads via Vulkan — don't interfere
        if (FPSBoostMod.VULKAN_PRESENT) return;
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.lightThrottle) return;
        fpsboost$lightFrame++;
        if ((fpsboost$lightFrame & 1) == 1) ci.cancel();
    }
}
