package com.fpsboost;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FPSBoostMod implements ClientModInitializer {
    public static final String MOD_ID = "fpsboost";
    public static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        // KeyMapping in 1.21.11: (name, InputConstants.Type, keyCode, category)
        // GLFW key K = 75
        configKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.fpsboost.config",
            InputConstants.Type.KEYSYM,
            75,
            "key.categories.fpsboost"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.consumeClick()) {
                client.setScreen(new FPSBoostScreen(client.screen));
            }
            // Save CPU when window not focused
            if (FPSBoostConfig.skipUnfocused && client.level != null && !client.isWindowActive()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        });

        System.out.println("[FPSBoost] Loaded. Press K to open config.");
    }
}
