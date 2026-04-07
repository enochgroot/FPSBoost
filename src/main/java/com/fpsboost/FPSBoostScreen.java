package com.fpsboost;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FPSBoostScreen extends Screen {
    private final Screen parent;

    public FPSBoostScreen(Screen parent) {
        super(Component.literal("FPSBoost Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = 52;
        final int W = 220, H = 20, G = 23;

        row(cx, y, W, H, "FPSBoost",          () -> FPSBoostConfig.enabled,
            () -> FPSBoostConfig.enabled            = !FPSBoostConfig.enabled);           y+=G;
        row(cx, y, W, H, "Entity Culling",    () -> FPSBoostConfig.entityCulling,
            () -> FPSBoostConfig.entityCulling      = !FPSBoostConfig.entityCulling);      y+=G;
        row(cx, y, W, H, "Block Entity Cull", () -> FPSBoostConfig.blockEntityCulling,
            () -> FPSBoostConfig.blockEntityCulling = !FPSBoostConfig.blockEntityCulling); y+=G;
        row(cx, y, W, H, "Skip Underground Effects", () -> FPSBoostConfig.skipUnderground,
            () -> FPSBoostConfig.skipUnderground    = !FPSBoostConfig.skipUnderground);    y+=G;
        row(cx, y, W, H, "Particle Limit",    () -> FPSBoostConfig.particleLimit,
            () -> FPSBoostConfig.particleLimit      = !FPSBoostConfig.particleLimit);      y+=G;
        row(cx, y, W, H, "Save CPU Unfocused",() -> FPSBoostConfig.skipUnfocused,
            () -> FPSBoostConfig.skipUnfocused      = !FPSBoostConfig.skipUnfocused);      y+=G;
        row(cx, y, W, H, "Extended Render Dist", () -> FPSBoostConfig.extendedRender,
            () -> { FPSBoostConfig.extendedRender = !FPSBoostConfig.extendedRender;
                    FPSBoostConfig.applyRenderDistance(); });                               y+=G;
        row(cx, y, W, H, "Dynamic Render Dist", () -> FPSBoostConfig.dynamicRender,
            () -> FPSBoostConfig.dynamicRender      = !FPSBoostConfig.dynamicRender);      y+=G;

        // Value adjusters: << label >> pattern
        adj(cx, y, W, H, "Render Dist",
            () -> FPSBoostConfig.extendRenderDist + " chunks",
            () -> { FPSBoostConfig.extendRenderDist = Math.max(2,  FPSBoostConfig.extendRenderDist-2);
                    if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance(); },
            () -> { FPSBoostConfig.extendRenderDist = Math.min(64, FPSBoostConfig.extendRenderDist+2);
                    if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance(); }); y+=G;
        adj(cx, y, W, H, "BE Render Dist",
            () -> FPSBoostConfig.blockEntityRenderDist + " blocks",
            () -> FPSBoostConfig.blockEntityRenderDist = Math.max(8,   FPSBoostConfig.blockEntityRenderDist-8),
            () -> FPSBoostConfig.blockEntityRenderDist = Math.min(128, FPSBoostConfig.blockEntityRenderDist+8)); y+=G;
        adj(cx, y, W, H, "Particle Max",
            () -> String.valueOf(FPSBoostConfig.maxParticles),
            () -> FPSBoostConfig.maxParticles = Math.max(100,  FPSBoostConfig.maxParticles-100),
            () -> FPSBoostConfig.maxParticles = Math.min(5000, FPSBoostConfig.maxParticles+100));  y+=G;
        adj(cx, y, W, H, "Target FPS",
            () -> FPSBoostConfig.targetFps + " FPS",
            () -> FPSBoostConfig.targetFps = Math.max(20,  FPSBoostConfig.targetFps-10),
            () -> FPSBoostConfig.targetFps = Math.min(500, FPSBoostConfig.targetFps+10));          y+=G+4;

        addRenderableWidget(Button.builder(Component.literal("Save & Close"),
            b -> { FPSBoostConfig.save(); this.minecraft.setScreen(parent); })
            .bounds(cx - W/2, y, W, H).build());
    }

    // Toggle row: full-width button that shows current ON/OFF state
    private void row(int cx, int y, int w, int h, String label,
                     java.util.function.BooleanSupplier getter, Runnable toggle) {
        // Store label in a mutable holder so the button can refresh itself
        final String[] lbl = {label + ": " + onOff(getter.getAsBoolean())};
        addRenderableWidget(Button.builder(Component.literal(lbl[0]), b -> {
            toggle.run();
            b.setMessage(Component.literal(label + ": " + onOff(getter.getAsBoolean())));
        }).bounds(cx - w/2, y, w, h).build());
    }

    // Adjuster row: [<<] [label: value] [>>]
    private void adj(int cx, int y, int w, int h, String label,
                     java.util.function.Supplier<String> valStr, Runnable dec, Runnable inc) {
        int bw = 28;
        addRenderableWidget(Button.builder(Component.literal("<<"), b -> {
            dec.run();
            // find display button and update it — just reopen screen
            this.minecraft.setScreen(new FPSBoostScreen(parent));
        }).bounds(cx - w/2, y, bw, h).build());
        addRenderableWidget(Button.builder(
            Component.literal(label + ": " + valStr.get()), b -> {})
            .bounds(cx - w/2 + bw + 2, y, w - bw*2 - 4, h).build());
        addRenderableWidget(Button.builder(Component.literal(">>"), b -> {
            inc.run();
            this.minecraft.setScreen(new FPSBoostScreen(parent));
        }).bounds(cx + w/2 - bw, y, bw, h).build());
    }

    private static String onOff(boolean b) { return b ? "ON" : "OFF"; }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        renderBackground(g, mx, my, dt);
        int fps = Minecraft.getInstance().getFps();
        String col = fps >= 300 ? "§b" : fps >= 120 ? "§a" : fps >= 60 ? "§e" : "§c";
        g.drawCenteredString(font, "FPSBoost v3 — FPS: " + col + fps, width/2, 14, 0xFFFFFF);
        g.drawCenteredString(font, "Press K to close", width/2, 26, 0x888888);
        super.render(g, mx, my, dt);
    }

    @Override public boolean isPauseScreen() { return false; }
}
