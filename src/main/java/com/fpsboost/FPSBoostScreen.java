package com.fpsboost;

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
        int y = 60;
        int bw = 200, bh = 20, gap = 24;

        addRenderableWidget(Button.builder(
            Component.literal("FPSBoost: " + onOff(FPSBoostConfig.enabled)),
            btn -> {
                FPSBoostConfig.enabled = !FPSBoostConfig.enabled;
                btn.setMessage(Component.literal("FPSBoost: " + onOff(FPSBoostConfig.enabled)));
            }).bounds(cx - bw/2, y, bw, bh).build());
        y += gap;

        addRenderableWidget(Button.builder(
            Component.literal("Entity Culling: " + onOff(FPSBoostConfig.entityCulling)),
            btn -> {
                FPSBoostConfig.entityCulling = !FPSBoostConfig.entityCulling;
                btn.setMessage(Component.literal("Entity Culling: " + onOff(FPSBoostConfig.entityCulling)));
            }).bounds(cx - bw/2, y, bw, bh).build());
        y += gap;

        addRenderableWidget(Button.builder(
            Component.literal("Particle Limit: " + onOff(FPSBoostConfig.particleLimit)),
            btn -> {
                FPSBoostConfig.particleLimit = !FPSBoostConfig.particleLimit;
                btn.setMessage(Component.literal("Particle Limit: " + onOff(FPSBoostConfig.particleLimit)));
            }).bounds(cx - bw/2, y, bw, bh).build());
        y += gap;

        addRenderableWidget(Button.builder(
            Component.literal("Save CPU When Unfocused: " + onOff(FPSBoostConfig.skipUnfocused)),
            btn -> {
                FPSBoostConfig.skipUnfocused = !FPSBoostConfig.skipUnfocused;
                btn.setMessage(Component.literal("Save CPU When Unfocused: " + onOff(FPSBoostConfig.skipUnfocused)));
            }).bounds(cx - bw/2, y, bw, bh).build());
        y += gap;

        addRenderableWidget(Button.builder(
            Component.literal("Extended Render Distance: " + onOff(FPSBoostConfig.extendedRender)
                + " (" + FPSBoostConfig.extendRenderDist + " chunks)"),
            btn -> {
                FPSBoostConfig.extendedRender = !FPSBoostConfig.extendedRender;
                FPSBoostConfig.applyRenderDistance();
                btn.setMessage(Component.literal("Extended Render Distance: " + onOff(FPSBoostConfig.extendedRender)
                    + " (" + FPSBoostConfig.extendRenderDist + " chunks)"));
            }).bounds(cx - bw/2, y, bw, bh).build());
        y += gap;

        // Render distance up/down
        addRenderableWidget(Button.builder(Component.literal("< Render Dist"),
            btn -> {
                FPSBoostConfig.extendRenderDist = Math.max(2, FPSBoostConfig.extendRenderDist - 2);
                if (FPSBoostConfig.extendedRender) {
                    FPSBoostConfig.applyRenderDistance();
                }
                this.minecraft.setScreen(new FPSBoostScreen(parent));
            }).bounds(cx - bw/2, y, 90, bh).build());

        addRenderableWidget(Button.builder(Component.literal("Render Dist >"),
            btn -> {
                FPSBoostConfig.extendRenderDist = Math.min(64, FPSBoostConfig.extendRenderDist + 2);
                if (FPSBoostConfig.extendedRender) {
                    FPSBoostConfig.applyRenderDistance();
                }
                this.minecraft.setScreen(new FPSBoostScreen(parent));
            }).bounds(cx + 10, y, 90, bh).build());
        y += gap + 8;

        addRenderableWidget(Button.builder(Component.literal("Done"),
            btn -> this.minecraft.setScreen(parent))
            .bounds(cx - bw/2, y, bw, bh).build());
    }

    private static String onOff(boolean b) { return b ? "ON" : "OFF"; }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        graphics.drawCenteredString(this.font,
            Component.literal("Press K to toggle this menu"), this.width / 2, 35, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
