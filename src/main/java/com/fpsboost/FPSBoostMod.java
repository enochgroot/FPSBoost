package com.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FPSBoostMod implements ClientModInitializer {
    public static final String MOD_ID = "fpsboost";
    public static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        configKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.fpsboost.config", 75, KeyMapping.Category.MISC)
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Load config on first world join
            if (client.level != null && !configLoaded) {
                FPSBoostConfig.load();
                if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance();
                configLoaded = true;
            }
            // Open config screen
            if (configKey.consumeClick()) {
                client.setScreen(new FPSBoostScreen(client.screen));
            }
            // Save CPU when unfocused
            if (FPSBoostConfig.skipUnfocused && client.level != null && !client.isWindowActive()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            // Dynamic render distance
            FPSBoostConfig.tickDynamic();
        });

        System.out.println("[FPSBoost] v2.0.0 Loaded — K to open config");
    }

    private boolean configLoaded = false;
}
