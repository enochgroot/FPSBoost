package com.fpsboost;

public class FPSBoostConfig {
    public static boolean enabled         = true;
    public static boolean entityCulling   = true;
    public static boolean particleLimit   = true;
    public static boolean skipUnfocused   = true;
    public static boolean extendedRender  = false;
    public static int     extendRenderDist = 48;

    private static int savedRenderDist = -1;

    public static void applyRenderDistance() {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc == null || mc.options == null) return;
        if (extendedRender && savedRenderDist == -1) {
            savedRenderDist = mc.options.renderDistance().get();
            mc.options.renderDistance().set(extendRenderDist);
            mc.levelRenderer.needsUpdate();
        } else if (!extendedRender && savedRenderDist != -1) {
            mc.options.renderDistance().set(savedRenderDist);
            savedRenderDist = -1;
            mc.levelRenderer.needsUpdate();
        }
    }

    public static void applyAll() {
        applyRenderDistance();
    }
}
