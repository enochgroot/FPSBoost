package com.fpsboost;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FPSBoostScreen extends Screen {
    private final Screen parent;

    public FPSBoostScreen(Screen parent) {
        super(Component.literal("FPSBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addRenderableWidget(
            Button.builder(Component.literal("Close"),
                b -> this.minecraft.setScreen(parent))
            .bounds(this.width / 2 - 50, this.height / 2, 100, 20)
            .build()
        );
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float dt) {
        renderBackground(g, mx, my, dt);
        g.drawCenteredString(font, Component.literal("FPSBoost - If you see this it works!"),
            width / 2, height / 2 - 30, 0xFFFFFF);
        super.render(g, mx, my, dt);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
