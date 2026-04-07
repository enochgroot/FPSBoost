package com.fpsboost;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FPSBoostScreen extends Screen {
    private final Screen parent;

    public FPSBoostScreen(Screen parent) {
        super(Component.literal("FPSBoost v2 Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = 55;
        final int BW = 220, BH = 20, GAP = 24, HBW = 105;

        // Master toggle
        addRenderableWidget(Button.builder(
            Component.literal("FPSBoost: " + on(FPSBoostConfig.enabled)),
            b -> { FPSBoostConfig.enabled = !FPSBoostConfig.enabled;
                   b.setMessage(Component.literal("FPSBoost: " + on(FPSBoostConfig.enabled))); })
            .bounds(cx - BW/2, y, BW, BH).build()); y += GAP;

        // Entity culling
        addRenderableWidget(Button.builder(
            Component.literal("Entity Culling: " + on(FPSBoostConfig.entityCulling)),
            b -> { FPSBoostConfig.entityCulling = !FPSBoostConfig.entityCulling;
                   b.setMessage(Component.literal("Entity Culling: " + on(FPSBoostConfig.entityCulling))); })
            .bounds(cx - BW/2, y, BW, BH).build()); y += GAP;

        // Particle limit toggle + slider
        addRenderableWidget(Button.builder(
            Component.literal("Particle Limit: " + on(FPSBoostConfig.particleLimit)),
            b -> { FPSBoostConfig.particleLimit = !FPSBoostConfig.particleLimit;
                   b.setMessage(Component.literal("Particle Limit: " + on(FPSBoostConfig.particleLimit))); })
            .bounds(cx - BW/2, y, HBW, BH).build());
        addRenderableWidget(new AbstractSliderButton(cx - BW/2 + HBW + 10, y, HBW, BH,
            Component.literal("Max: " + FPSBoostConfig.maxParticles),
            (FPSBoostConfig.maxParticles - 100.0) / (5000.0 - 100.0)) {
            @Override protected void updateMessage() {
                int v = (int)(100 + this.value * (5000 - 100));
                FPSBoostConfig.maxParticles = v;
                setMessage(Component.literal("Max: " + v));
            }
            @Override protected void applyValue() { updateMessage(); }
        }); y += GAP;

        // Save CPU when unfocused
        addRenderableWidget(Button.builder(
            Component.literal("Save CPU Unfocused: " + on(FPSBoostConfig.skipUnfocused)),
            b -> { FPSBoostConfig.skipUnfocused = !FPSBoostConfig.skipUnfocused;
                   b.setMessage(Component.literal("Save CPU Unfocused: " + on(FPSBoostConfig.skipUnfocused))); })
            .bounds(cx - BW/2, y, BW, BH).build()); y += GAP;

        // Extended render distance toggle
        addRenderableWidget(Button.builder(
            Component.literal("Extended Render Dist: " + on(FPSBoostConfig.extendedRender)),
            b -> { FPSBoostConfig.extendedRender = !FPSBoostConfig.extendedRender;
                   FPSBoostConfig.applyRenderDistance();
                   b.setMessage(Component.literal("Extended Render Dist: " + on(FPSBoostConfig.extendedRender))); })
            .bounds(cx - BW/2, y, BW, BH).build()); y += GAP;

        // Render distance slider (2–64 chunks)
        addRenderableWidget(new AbstractSliderButton(cx - BW/2, y, BW, BH,
            Component.literal("Render Dist: " + FPSBoostConfig.extendRenderDist + " chunks"),
            (FPSBoostConfig.extendRenderDist - 2.0) / (64.0 - 2.0)) {
            @Override protected void updateMessage() {
                int v = (int)(2 + this.value * (64 - 2));
                FPSBoostConfig.extendRenderDist = v;
                setMessage(Component.literal("Render Dist: " + v + " chunks"));
            }
            @Override protected void applyValue() {
                updateMessage();
                if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance();
            }
        }); y += GAP;

        // Dynamic render distance toggle + target FPS slider
        addRenderableWidget(Button.builder(
            Component.literal("Dynamic RD: " + on(FPSBoostConfig.dynamicRender)),
            b -> { FPSBoostConfig.dynamicRender = !FPSBoostConfig.dynamicRender;
                   b.setMessage(Component.literal("Dynamic RD: " + on(FPSBoostConfig.dynamicRender))); })
            .bounds(cx - BW/2, y, HBW, BH).build());
        addRenderableWidget(new AbstractSliderButton(cx - BW/2 + HBW + 10, y, HBW, BH,
            Component.literal("Target: " + FPSBoostConfig.targetFps + " FPS"),
            (FPSBoostConfig.targetFps - 20.0) / (240.0 - 20.0)) {
            @Override protected void updateMessage() {
                int v = (int)(20 + this.value * (240 - 20));
                FPSBoostConfig.targetFps = v;
                setMessage(Component.literal("Target: " + v + " FPS"));
            }
            @Override protected void applyValue() { updateMessage(); }
        }); y += GAP + 4;

        // Save + Done
        addRenderableWidget(Button.builder(Component.literal("Save & Close"),
            b -> { FPSBoostConfig.save(); this.minecraft.setScreen(parent); })
            .bounds(cx - BW/2, y, BW, BH).build());
    }

    private static String on(boolean b) { return b ? "§aON§r" : "§cOFF§r"; }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        renderBackground(g, mx, my, dt);
        g.drawCenteredString(font, title, width/2, 16, 0xFFFFFF);
        int fps = Minecraft.getInstance().getFps();
        String fpsColor = fps >= 60 ? "§a" : fps >= 30 ? "§e" : "§c";
        g.drawCenteredString(font,
            Component.literal("Current FPS: " + fpsColor + fps + "§r  |  Press K to toggle"),
            width/2, 30, 0xAAAAAA);
        super.render(g, mx, my, dt);
    }

    @Override public boolean isPauseScreen() { return false; }
}
