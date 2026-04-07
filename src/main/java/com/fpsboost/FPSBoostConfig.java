package com.fpsboost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import java.io.*;
import java.nio.file.*;

public class FPSBoostConfig {
    // Toggles
    public static boolean enabled         = true;
    public static boolean entityCulling   = true;
    public static boolean particleLimit   = true;
    public static boolean skipUnfocused   = true;
    public static boolean extendedRender  = false;
    public static boolean dynamicRender   = false;

    // Values
    public static int extendRenderDist = 48;
    public static int maxParticles     = 1000;
    public static int targetFps        = 60;

    private static int savedRenderDist = -1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // --- SAVE / LOAD ---
    private record Data(boolean enabled, boolean entityCulling, boolean particleLimit,
                        boolean skipUnfocused, boolean extendedRender, boolean dynamicRender,
                        int extendRenderDist, int maxParticles, int targetFps) {}

    public static void save() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            Path cfg = mc.gameDirectory.toPath().resolve("config/fpsboost.json");
            Files.createDirectories(cfg.getParent());
            Files.writeString(cfg, GSON.toJson(new Data(enabled, entityCulling, particleLimit,
                skipUnfocused, extendedRender, dynamicRender,
                extendRenderDist, maxParticles, targetFps)));
        } catch (Exception e) { System.err.println("[FPSBoost] Save failed: " + e.getMessage()); }
    }

    public static void load() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            Path cfg = mc.gameDirectory.toPath().resolve("config/fpsboost.json");
            if (!Files.exists(cfg)) return;
            Data d = GSON.fromJson(Files.readString(cfg), Data.class);
            if (d == null) return;
            enabled = d.enabled(); entityCulling = d.entityCulling();
            particleLimit = d.particleLimit(); skipUnfocused = d.skipUnfocused();
            extendedRender = d.extendedRender(); dynamicRender = d.dynamicRender();
            extendRenderDist = d.extendRenderDist(); maxParticles = d.maxParticles();
            targetFps = d.targetFps();
        } catch (Exception e) { System.err.println("[FPSBoost] Load failed: " + e.getMessage()); }
    }

    // --- RENDER DISTANCE ---
    public static void applyRenderDistance() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.options == null) return;
        if (extendedRender && savedRenderDist == -1) {
            savedRenderDist = mc.options.renderDistance().get();
            mc.options.renderDistance().set(extendRenderDist);
            if (mc.levelRenderer != null) mc.levelRenderer.needsUpdate();
        } else if (!extendedRender && savedRenderDist != -1) {
            mc.options.renderDistance().set(savedRenderDist);
            savedRenderDist = -1;
            if (mc.levelRenderer != null) mc.levelRenderer.needsUpdate();
        }
    }

    // Called each tick for dynamic render distance adjustment
    private static int dynamicCooldown = 0;
    public static void tickDynamic() {
        if (!enabled || !dynamicRender) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;
        if (--dynamicCooldown > 0) return;
        dynamicCooldown = 40; // check every 2 seconds
        int fps = mc.getFps();
        int current = mc.options.renderDistance().get();
        if (fps < targetFps - 5 && current > 2) {
            mc.options.renderDistance().set(current - 2);
            mc.levelRenderer.needsUpdate();
        } else if (fps > targetFps + 10) {
            int cap = extendedRender ? extendRenderDist : 32;
            if (current < cap) {
                mc.options.renderDistance().set(current + 2);
                mc.levelRenderer.needsUpdate();
            }
        }
    }
}
