package com.fpsboost.mixin;

import com.fpsboost.FPSBoostConfig;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.particle.ParticleEngine")
public class ParticleEngineMixin {
    private static int fpsboost$count = 0;
    private static long fpsboost$resetAt = 0L;

    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V",
            at = @At("HEAD"), cancellable = true)
    private void fpsboost$limitParticles(Particle particle, CallbackInfo ci) {
        if (!FPSBoostConfig.enabled || !FPSBoostConfig.particleLimit) return;
        long now = System.currentTimeMillis();
        if (now - fpsboost$resetAt >= 1000L) { fpsboost$count = 0; fpsboost$resetAt = now; }
        if (fpsboost$count >= FPSBoostConfig.maxParticles) ci.cancel();
        else fpsboost$count++;
    }
}
