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
        super(Component.literal("FPSBoost v3 Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2, y = 52;
        final int BW = 220, BH = 20, GAP = 23, HW = 105;

        addToggle("FPSBoost Master", FPSBoostConfig.enabled,
            v -> FPSBoostConfig.enabled = v, cx, y, BW, BH); y += GAP;

        addToggle("Entity Culling (Real Frustum)", FPSBoostConfig.entityCulling,
            v -> FPSBoostConfig.entityCulling = v, cx, y, BW, BH); y += GAP;

        addToggle("Block Entity Culling", FPSBoostConfig.blockEntityCulling,
            v -> FPSBoostConfig.blockEntityCulling = v, cx, y, HW, BH);
        addRenderableWidget(new AbstractSliderButton(cx - BW/2 + HW + 10, y, HW, BH,
            Component.literal("BE Dist: " + FPSBoostConfig.blockEntityRenderDist + "b"),
            (FPSBoostConfig.blockEntityRenderDist - 8.0) / (128.0 - 8.0)) {
            @Override protected void updateMessage() {
                int v = (int)(8 + value * (128-8));
                FPSBoostConfig.blockEntityRenderDist = v;
                setMessage(Component.literal("BE Dist: " + v + "b"));
            }
            @Override protected void applyValue() { updateMessage(); }
        }); y += GAP;

        addToggle("Skip Underground Sky/Weather/Clouds", FPSBoostConfig.skipUnderground,
            v -> FPSBoostConfig.skipUnderground = v, cx, y, BW, BH); y += GAP;

        addToggle("Particle Limit", FPSBoostConfig.particleLimit,
            v -> FPSBoostConfig.particleLimit = v, cx, y, HW, BH);
        addRenderableWidget(new AbstractSliderButton(cx - BW/2 + HW + 10, y, HW, BH,
            Component.literal("Max: " + FPSBoostConfig.maxParticles),
            (FPSBoostConfig.maxParticles - 100.0) / (5000.0 - 100.0)) {
            @Override protected void updateMessage() {
                int v = (int)(100 + value * (5000-100));
                FPSBoostConfig.maxParticles = v;
                setMessage(Component.literal("Max: " + v));
            }
            @Override protected void applyValue() { updateMessage(); }
        }); y += GAP;

        addToggle("Save CPU Unfocused", FPSBoostConfig.skipUnfocused,
            v -> FPSBoostConfig.skipUnfocused = v, cx, y, BW, BH); y += GAP;

        addToggle("Extended Render Distance", FPSBoostConfig.extendedRender, v -> {
            FPSBoostConfig.extendedRender = v;
            FPSBoostConfig.applyRenderDistance();
        }, cx, y, BW, BH); y += GAP;

        addRenderableWidget(new AbstractSliderButton(cx - BW/2, y, BW, BH,
            Component.literal("Render Dist: " + FPSBoostConfig.extendRenderDist + " chunks"),
            (FPSBoostConfig.extendRenderDist - 2.0) / (64.0 - 2.0)) {
            @Override protected void updateMessage() {
                int v = (int)(2 + value * (64-2));
                FPSBoostConfig.extendRenderDist = v;
                setMessage(Component.literal("Render Dist: " + v + " chunks"));
            }
            @Override protected void applyValue() {
                updateMessage();
                if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance();
            }
        }); y += GAP;

        addToggle("Dynamic Render Distance", FPSBoostConfig.dynamicRender,
            v -> FPSBoostConfig.dynamicRender = v, cx, y, HW, BH);
        addRenderableWidget(new AbstractSliderButton(cx - BW/2 + HW + 10, y, HW, BH,
            Component.literal("Target: " + FPSBoostConfig.targetFps + " FPS"),
            (FPSBoostConfig.targetFps - 20.0) / (240.0 - 20.0)) {
            @Override protected void updateMessage() {
                int v = (int)(20 + value * (240-20));
                FPSBoostConfig.targetFps = v;
                setMessage(Component.literal("Target: " + v + " FPS"));
            }
            @Override protected void applyValue() { updateMessage(); }
        }); y += GAP + 4;

        addRenderableWidget(Button.builder(Component.literal("Save & Close"),
            b -> { FPSBoostConfig.save(); minecraft.setScreen(parent); })
            .bounds(cx - BW/2, y, BW, BH).build());
    }

    private interface BoolConsumer { void accept(boolean v); }
    private void addToggle(String label, boolean initial, BoolConsumer onChange,
                           int cx, int y, int bw, int bh) {
        addRenderableWidget(Button.builder(
            Component.literal(label + ": " + on(initial)), b -> {
                boolean nv = !b.getMessage().getString().endsWith("ON");
                onChange.accept(nv);
                b.setMessage(Component.literal(label + ": " + on(nv)));
            }).bounds(cx - 110, y, bw, bh).build());
    }

    private static String on(boolean b) { return b ? "§aON§r" : "§cOFF§r"; }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        renderBackground(g, mx, my, dt);
        g.drawCenteredString(font, title, width/2, 12, 0xFFFFFF);
        int fps = Minecraft.getInstance().getFps();
        String col = fps >= 300 ? "§b" : fps >= 120 ? "§a" : fps >= 60 ? "§e" : "§c";
        g.drawCenteredString(font,
            Component.literal("FPS: " + col + fps + "§r  |  K = toggle  |  v3.0"),
            width/2, 27, 0xAAAAAA);
        boolean ug = FPSBoostConfig.isUnderground();
        g.drawCenteredString(font,
            Component.literal(ug ? "§7Underground — sky/weather/clouds skipped§r"
                                 : "§7Above ground§r"),
            width/2, 40, 0xAAAAAA);
        super.render(g, mx, my, dt);
    }

    @Override public boolean isPauseScreen() { return false; }
}
