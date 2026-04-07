package com.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class FPSBoostMod implements ClientModInitializer {
    public static KeyMapping configKey;
    public static KeyMapping toggleKey;
    private boolean loaded = false;

    @Override
    public void onInitializeClient() {
        // K = open config menu
        configKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.fpsboost.config", 75, KeyMapping.Category.MISC)
        );
        // J = instant disable/enable all (rebindable in Options > Controls)
        toggleKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.fpsboost.toggle", 74, KeyMapping.Category.MISC)
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && !loaded) {
                FPSBoostConfig.load();
                if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance();
                loaded = true;
            }

            // K — open config screen
            if (configKey.consumeClick()) {
                client.setScreen(new FPSBoostScreen(client.screen));
            }

            // J — instant toggle all on/off, shows message above hotbar
            if (toggleKey.consumeClick()) {
                FPSBoostConfig.enabled = !FPSBoostConfig.enabled;
                FPSBoostConfig.save();
                if (client.player != null) {
                    String msg = FPSBoostConfig.enabled
                        ? "\u00a7aFPSBoost ON"
                        : "\u00a7cFPSBoost OFF";
                    client.player.displayClientMessage(
                        Component.literal(msg), true // true = action bar (above hotbar)
                    );
                }
            }

            if (FPSBoostConfig.skipUnfocused && client.level != null && !client.isWindowActive()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            FPSBoostConfig.tickDynamic();
        });

        System.out.println("[FPSBoost] v5.0.4 — K=menu, J=toggle all");
    }
}
