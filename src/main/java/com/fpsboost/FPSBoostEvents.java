package com.fpsboost;

// Frustum capture is handled directly in EntityRenderDispatcherMixin
// This class reserved for future event registrations
public class FPSBoostEvents {
    public static void register() {
        // No-op for now — entity culling uses mixin-internal frustum capture
    }
}
