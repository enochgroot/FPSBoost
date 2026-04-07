package com.fpsboost;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
public class FPSBoostMod implements ClientModInitializer {
    public static KeyMapping configKey;
    private boolean loaded=false;
    @Override public void onInitializeClient(){
        configKey=KeyBindingHelper.registerKeyBinding(new KeyMapping("key.fpsboost.config",75,KeyMapping.Category.MISC));
        ClientTickEvents.END_CLIENT_TICK.register(c->{
            if(c.level!=null&&!loaded){FPSBoostConfig.load();if(FPSBoostConfig.extendedRender)FPSBoostConfig.applyRenderDistance();loaded=true;}
            if(configKey.consumeClick())c.setScreen(new FPSBoostScreen(c.screen));
            if(FPSBoostConfig.skipUnfocused&&c.level!=null&&!c.isWindowActive()){try{Thread.sleep(100);}catch(InterruptedException i){}}
            FPSBoostConfig.tickDynamic();
        });
        System.out.println("[FPSBoost] v5.0.3 loaded");
    }
}
