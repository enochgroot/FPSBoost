package com.fpsboost;

import com.fpsboost.mixin.EntityRenderDispatcherMixin;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class FPSBoostEvents {
    public static void register() {
        WorldRenderEvents.AFTER_SETUP.register(ctx -> {
            EntityRenderDispatcherMixin.currentFrustum = ctx.frustum();
        });
    }
}
