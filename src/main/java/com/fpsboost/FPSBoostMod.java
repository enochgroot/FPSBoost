package com.fpsboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class FPSBoostMod implements ClientModInitializer {
    public static KeyMapping configKey;
    public static KeyMapping toggleKey;
    private boolean loaded = false;

    /**
     * True if VulkanMod is installed.
     * VulkanMod replaces LevelRenderer's OpenGL frame-graph render passes
     * (addWeatherPass, addSkyPass, addCloudsPass) and LightTexture GPU uploads.
     * We skip those specific mixins when Vulkan is present so they don't
     * crash or conflict — entity culling, particle cap, shadows etc. are
     * fully compatible and still run.
     */
    public static final boolean VULKAN_PRESENT =
        FabricLoader.getInstance().isModLoaded("vulkanmod");

    @Override
    public void onInitializeClient() {
        configKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.fpsboost.config", 75, KeyMapping.Category.MISC)
        );
        toggleKey = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.fpsboost.toggle", 74, KeyMapping.Category.MISC)
        );

        if (VULKAN_PRESENT) {
            System.out.println("[FPSBoost] VulkanMod detected — renderer mixins disabled, entity/particle optimizations active");
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && !loaded) {
                FPSBoostConfig.load();
                if (FPSBoostConfig.extendedRender) FPSBoostConfig.applyRenderDistance();
                loaded = true;
            }
            if (configKey.consumeClick()) client.setScreen(new FPSBoostScreen(client.screen));
            if (toggleKey.consumeClick()) {
                FPSBoostConfig.enabled = !FPSBoostConfig.enabled;
                FPSBoostConfig.save();
                if (client.player != null) {
                    client.player.displayClientMessage(
                        Component.literal(FPSBoostConfig.enabled ? "\u00a7aFPSBoost ON" : "\u00a7cFPSBoost OFF"),
                        true);
                }
            }
            if (FPSBoostConfig.skipUnfocused && client.level != null && !client.isWindowActive()) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            FPSBoostConfig.tickDynamic();
        });

        System.out.println("[FPSBoost] v5.1.0 — K=menu, J=toggle" +
            (VULKAN_PRESENT ? " [Vulkan mode]" : ""));
    }
}
