package com.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;

public class FPSBoostMod implements ClientModInitializer {
    public static final String MOD_ID = "fpsboost";
    public static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        // Register keybind: K to open config screen
        configKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.fpsboost.config",
            InputConstants.KEY_K,
            "key.categories.fpsboost"
        ));

        // Register world render event for frustum capture
        FPSBoostEvents.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.consumeClick()) {
                client.setScreen(new FPSBoostScreen(client.screen));
            }
            // Save CPU when window is not focused
            if (FPSBoostConfig.skipUnfocused && client.level != null && !client.isWindowActive()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        });

        System.out.println("[FPSBoost] Loaded. Press K to open config.");
    }
}
