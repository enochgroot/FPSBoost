package com.fpsboost;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FPSBoostScreen extends Screen {
    private final Screen parent;
    private final int page;

    public FPSBoostScreen(Screen parent) { this(parent, 0); }
    public FPSBoostScreen(Screen parent, int page) {
        super(Component.literal("FPSBoost v5.1"));
        this.parent = parent; this.page = page;
    }

    @Override
    protected void init() {
        int cx = this.width / 2, y = 46;
        final int W = 220, H = 20, G = 22;

        if (page == 0) {
            addBtn(cx, y, W, H, "FPSBoost", FPSBoostConfig.enabled,
                () -> FPSBoostConfig.enabled = !FPSBoostConfig.enabled); y += G;
            addBtn(cx, y, W, H, "Entity Culling", FPSBoostConfig.entityCulling,
                () -> FPSBoostConfig.entityCulling = !FPSBoostConfig.entityCulling); y += G;
            addBtn(cx, y, W, H, "Entity LOD", FPSBoostConfig.entityLOD,
                () -> FPSBoostConfig.entityLOD = !FPSBoostConfig.entityLOD); y += G;
            addBtn(cx, y, W, H, "No Shadows", FPSBoostConfig.noShadows,
                () -> FPSBoostConfig.noShadows = !FPSBoostConfig.noShadows); y += G;
            addBtn(cx, y, W, H, "No Nametags", FPSBoostConfig.noNameTags,
                () -> FPSBoostConfig.noNameTags = !FPSBoostConfig.noNameTags); y += G;
            addBtn(cx, y, W, H, "Block Entity Culling", FPSBoostConfig.blockEntityCulling,
                () -> FPSBoostConfig.blockEntityCulling = !FPSBoostConfig.blockEntityCulling); y += G;
            addBtn(cx, y, W, H, "Skip Underground Sky", FPSBoostConfig.skipUnderground,
                () -> FPSBoostConfig.skipUnderground = !FPSBoostConfig.skipUnderground); y += G;
            addBtn(cx, y, W, H, "No Clouds", FPSBoostConfig.noClouds,
                () -> FPSBoostConfig.noClouds = !FPSBoostConfig.noClouds); y += G;
            addBtn(cx, y, W, H, "Smart Sky", FPSBoostConfig.smartSky,
                () -> FPSBoostConfig.smartSky = !FPSBoostConfig.smartSky); y += G;
            addRenderableWidget(Button.builder(Component.literal("Next Page >>"),
                b -> this.minecraft.setScreen(new FPSBoostScreen(parent, 1)))
                .bounds(cx - W/2, y, W, H).build());
        } else {
            addBtn(cx, y, W, H, "No View Bob", FPSBoostConfig.noViewBob,
                () -> FPSBoostConfig.noViewBob = !FPSBoostConfig.noViewBob); y += G;
            addBtn(cx, y, W, H, "No Hand Model", FPSBoostConfig.noHand,
                () -> FPSBoostConfig.noHand = !FPSBoostConfig.noHand); y += G;
            addBtn(cx, y, W, H, "Light Throttle" + (FPSBoostMod.VULKAN_PRESENT ? " [N/A-Vulkan]" : ""),
                FPSBoostConfig.lightThrottle,
                () -> FPSBoostConfig.lightThrottle = !FPSBoostConfig.lightThrottle); y += G;
            addBtn(cx, y, W, H, "Particle Limit", FPSBoostConfig.particleLimit,
                () -> FPSBoostConfig.particleLimit = !FPSBoostConfig.particleLimit); y += G;
            addBtn(cx, y, W, H, "Save CPU Unfocused", FPSBoostConfig.skipUnfocused,
                () -> FPSBoostConfig.skipUnfocused = !FPSBoostConfig.skipUnfocused); y += G;
            addBtn(cx, y, W, H, "Extended Render Dist", FPSBoostConfig.extendedRender,
                () -> { FPSBoostConfig.extendedRender = !FPSBoostConfig.extendedRender;
                        FPSBoostConfig.applyRenderDistance(); }); y += G;
            addBtn(cx, y, W, H, "Dynamic Render Dist", FPSBoostConfig.dynamicRender,
                () -> FPSBoostConfig.dynamicRender = !FPSBoostConfig.dynamicRender); y += G;
            addRenderableWidget(Button.builder(Component.literal("*** MAX FPS PRESET ***"), b -> {
                FPSBoostConfig.enabled=true; FPSBoostConfig.entityCulling=true;
                FPSBoostConfig.entityLOD=true; FPSBoostConfig.noShadows=true;
                FPSBoostConfig.noNameTags=true; FPSBoostConfig.blockEntityCulling=true;
                FPSBoostConfig.skipUnderground=true; FPSBoostConfig.noClouds=true;
                FPSBoostConfig.smartSky=true; FPSBoostConfig.noViewBob=true;
                FPSBoostConfig.noHand=true; FPSBoostConfig.lightThrottle=true;
                FPSBoostConfig.particleLimit=true; FPSBoostConfig.maxParticles=100;
                FPSBoostConfig.save();
                this.minecraft.setScreen(new FPSBoostScreen(parent, 1));
            }).bounds(cx - W/2, y, W, H).build()); y += G;
            addRenderableWidget(Button.builder(Component.literal("<< Back"),
                b -> this.minecraft.setScreen(new FPSBoostScreen(parent, 0)))
                .bounds(cx - W/2, y, (W/2) - 2, H).build());
            addRenderableWidget(Button.builder(Component.literal("Save & Close"),
                b -> { FPSBoostConfig.save(); this.minecraft.setScreen(parent); })
                .bounds(cx + 2, y, (W/2) - 2, H).build());
        }
    }

    private void addBtn(int cx, int y, int w, int h, String lbl, boolean cur, Runnable run) {
        addRenderableWidget(Button.builder(
            Component.literal(lbl + ": " + (cur ? "ON" : "OFF")), b -> {
                run.run();
                // re-read the field to get updated value
                b.setMessage(Component.literal(lbl + ": " + (b.getMessage().getString().endsWith("OFF") ? "ON" : "OFF")));
            }).bounds(cx - w/2, y, w, h).build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        int fps = this.minecraft.getFps();
        String c = fps>=500?"\u00a75":fps>=300?"\u00a7d":fps>=200?"\u00a7b":fps>=120?"\u00a7a":fps>=60?"\u00a7e":"\u00a7c";
        g.drawCenteredString(font, "FPSBoost v5.1  |  " + c + fps + " FPS  |  pg " + (page+1) + "/2", width/2, 14, 0xFFFFFF);
        String mode = FPSBoostMod.VULKAN_PRESENT ? "\u00a7dVulkan Mode\u00a7r — entity/particle opts active" : "K=menu  J=toggle  MAX FPS on pg2";
        g.drawCenteredString(font, mode, width/2, 28, 0x888888);
        super.render(g, mx, my, dt);
    }

    @Override public boolean isPauseScreen() { return false; }
}
