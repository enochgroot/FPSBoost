package com.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;

public class FPSBoostMod implements ClientModInitializer {
    public static final String MOD_ID = "fpsboost";
    public static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        configKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.fpsboost.config",
            InputConstants.KEY_K,
            "key.categories.fpsboost"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.consumeClick()) {
                client.setScreen(new FPSBoostScreen(client.screen));
            }
            // Skip rendering heavy stuff when window is not focused
            if (FPSBoostConfig.skipUnfocused && !client.isWindowActive()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        });

        System.out.println("[FPSBoost] Loaded — press K to open config");
    }
}
