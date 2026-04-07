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
        super(Component.literal("FPSBoost v5"));
        this.parent = parent;
        this.page = page;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = 46;
        final int W = 220, H = 20, G = 22;

        if (page == 0) {
            addRenderableWidget(Button.builder(
                Component.literal("FPSBoost: " + on(FPSBoostConfig.enabled)),
                b -> { FPSBoostConfig.enabled = !FPSBoostConfig.enabled;
                       b.setMessage(Component.literal("FPSBoost: " + on(FPSBoostConfig.enabled))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Entity Culling: " + on(FPSBoostConfig.entityCulling)),
                b -> { FPSBoostConfig.entityCulling = !FPSBoostConfig.entityCulling;
                       b.setMessage(Component.literal("Entity Culling: " + on(FPSBoostConfig.entityCulling))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Entity LOD: " + on(FPSBoostConfig.entityLOD)),
                b -> { FPSBoostConfig.entityLOD = !FPSBoostConfig.entityLOD;
                       b.setMessage(Component.literal("Entity LOD: " + on(FPSBoostConfig.entityLOD))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("No Shadows: " + on(FPSBoostConfig.noShadows)),
                b -> { FPSBoostConfig.noShadows = !FPSBoostConfig.noShadows;
                       b.setMessage(Component.literal("No Shadows: " + on(FPSBoostConfig.noShadows))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("No Nametags: " + on(FPSBoostConfig.noNameTags)),
                b -> { FPSBoostConfig.noNameTags = !FPSBoostConfig.noNameTags;
                       b.setMessage(Component.literal("No Nametags: " + on(FPSBoostConfig.noNameTags))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Block Entity Culling: " + on(FPSBoostConfig.blockEntityCulling)),
                b -> { FPSBoostConfig.blockEntityCulling = !FPSBoostConfig.blockEntityCulling;
                       b.setMessage(Component.literal("Block Entity Culling: " + on(FPSBoostConfig.blockEntityCulling))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Skip Underground Sky: " + on(FPSBoostConfig.skipUnderground)),
                b -> { FPSBoostConfig.skipUnderground = !FPSBoostConfig.skipUnderground;
                       b.setMessage(Component.literal("Skip Underground Sky: " + on(FPSBoostConfig.skipUnderground))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("No Clouds: " + on(FPSBoostConfig.noClouds)),
                b -> { FPSBoostConfig.noClouds = !FPSBoostConfig.noClouds;
                       b.setMessage(Component.literal("No Clouds: " + on(FPSBoostConfig.noClouds))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Smart Sky: " + on(FPSBoostConfig.smartSky)),
                b -> { FPSBoostConfig.smartSky = !FPSBoostConfig.smartSky;
                       b.setMessage(Component.literal("Smart Sky: " + on(FPSBoostConfig.smartSky))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(Component.literal("Next Page >>"),
                b -> this.minecraft.setScreen(new FPSBoostScreen(parent, 1)))
                .bounds(cx - W/2, y, W, H).build());

        } else {
            addRenderableWidget(Button.builder(
                Component.literal("No View Bob: " + on(FPSBoostConfig.noViewBob)),
                b -> { FPSBoostConfig.noViewBob = !FPSBoostConfig.noViewBob;
                       b.setMessage(Component.literal("No View Bob: " + on(FPSBoostConfig.noViewBob))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("No Hand Model: " + on(FPSBoostConfig.noHand)),
                b -> { FPSBoostConfig.noHand = !FPSBoostConfig.noHand;
                       b.setMessage(Component.literal("No Hand Model: " + on(FPSBoostConfig.noHand))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Light Throttle: " + on(FPSBoostConfig.lightThrottle)),
                b -> { FPSBoostConfig.lightThrottle = !FPSBoostConfig.lightThrottle;
                       b.setMessage(Component.literal("Light Throttle: " + on(FPSBoostConfig.lightThrottle))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Particle Limit: " + on(FPSBoostConfig.particleLimit)),
                b -> { FPSBoostConfig.particleLimit = !FPSBoostConfig.particleLimit;
                       b.setMessage(Component.literal("Particle Limit: " + on(FPSBoostConfig.particleLimit))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Save CPU Unfocused: " + on(FPSBoostConfig.skipUnfocused)),
                b -> { FPSBoostConfig.skipUnfocused = !FPSBoostConfig.skipUnfocused;
                       b.setMessage(Component.literal("Save CPU Unfocused: " + on(FPSBoostConfig.skipUnfocused))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Extended Render Dist: " + on(FPSBoostConfig.extendedRender)),
                b -> { FPSBoostConfig.extendedRender = !FPSBoostConfig.extendedRender;
                       FPSBoostConfig.applyRenderDistance();
                       b.setMessage(Component.literal("Extended Render Dist: " + on(FPSBoostConfig.extendedRender))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(
                Component.literal("Dynamic Render Dist: " + on(FPSBoostConfig.dynamicRender)),
                b -> { FPSBoostConfig.dynamicRender = !FPSBoostConfig.dynamicRender;
                       b.setMessage(Component.literal("Dynamic Render Dist: " + on(FPSBoostConfig.dynamicRender))); })
                .bounds(cx - W/2, y, W, H).build()); y += G;

            addRenderableWidget(Button.builder(Component.literal("*** MAX FPS PRESET ***"),
                b -> {
                    FPSBoostConfig.enabled = true;
                    FPSBoostConfig.entityCulling = true;
                    FPSBoostConfig.entityLOD = true;
                    FPSBoostConfig.noShadows = true;
                    FPSBoostConfig.noNameTags = true;
                    FPSBoostConfig.blockEntityCulling = true;
                    FPSBoostConfig.skipUnderground = true;
                    FPSBoostConfig.noClouds = true;
                    FPSBoostConfig.smartSky = true;
                    FPSBoostConfig.noViewBob = true;
                    FPSBoostConfig.noHand = true;
                    FPSBoostConfig.lightThrottle = true;
                    FPSBoostConfig.particleLimit = true;
                    FPSBoostConfig.maxParticles = 100;
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

    private static String on(boolean b) { return b ? "ON" : "OFF"; }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        // DO NOT call renderBackground() here!
        // In MC 1.21.11 the framework calls it automatically before render().
        // Calling it again = "Can only blur once per frame" crash.
        int fps = this.minecraft.getFps();
        String c = fps >= 500 ? "§5" : fps >= 300 ? "§d" : fps >= 200 ? "§b" : fps >= 120 ? "§a" : fps >= 60 ? "§e" : "§c";
        g.drawCenteredString(font, "FPSBoost v5  |  " + c + fps + " FPS  |  Page " + (page + 1) + "/2",
            width / 2, 16, 0xFFFFFF);
        g.drawCenteredString(font, "K = close  |  MAX FPS PRESET on page 2", width / 2, 30, 0x888888);
        super.render(g, mx, my, dt);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
