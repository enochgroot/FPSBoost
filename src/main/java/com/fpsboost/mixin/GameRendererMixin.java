package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.GameRenderer")
public class GameRendererMixin {

    /**
     * Cancel view bobbing when noViewBob is ON.
     * bobView calculates walking camera sway per frame — minor but free.
     * Confirmed in 1.21.11 mappings: void bobView(PoseStack, float)
     * Note: bobHurt (damage shake) is a different method and is NOT cancelled.
     */
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void fpsboost$noViewBob(CallbackInfo ci) {
        if (FPSBoostConfig.enabled && FPSBoostConfig.noViewBob) ci.cancel();
    }
}
