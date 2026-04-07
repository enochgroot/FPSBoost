package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.particle.ParticleEngine")
public class ParticleEngineMixin {

    private static int fpsboost$particleCount = 0;
    private static long fpsboost$lastReset = 0;

    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V",
            at = @At("HEAD"), cancellable = true)
    private void fpsboost$limitParticles(Particle particle, CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.particleLimit) return;

        long now = System.currentTimeMillis();
        // Reset counter every second
        if (now - fpsboost$lastReset > 1000) {
            fpsboost$particleCount = 0;
            fpsboost$lastReset = now;
        }

        if (fpsboost$particleCount >= FPSBoostConfig.maxParticles) {
            ci.cancel();
        } else {
            fpsboost$particleCount++;
        }
    }
}
