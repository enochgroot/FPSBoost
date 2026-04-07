package com.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FPSBoostMod implements ClientModInitializer {
    public static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        configKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.fpsboost.config", 75, KeyMapping.Category.MISC)
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.consumeClick()) {
                client.setScreen(new FPSBoostScreen(client.screen));
            }
        });
        System.out.println("[FPSBoost] DIAGNOSTIC build loaded - press K");
    }
}
