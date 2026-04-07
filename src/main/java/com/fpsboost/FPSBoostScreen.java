package com.fpsboost;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class FPSBoostScreen extends Screen {
    private final Screen parent;

    public FPSBoostScreen(Screen parent) {
        super(Component.literal("FPSBoost v4 Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2, y = 50;
        final int W = 220, H = 18, G = 21;

        // --- CORE ---
        tog(cx,y,W,H,"FPSBoost Master",()->FPSBoostConfig.enabled,
            ()->FPSBoostConfig.enabled=!FPSBoostConfig.enabled); y+=G;

        // --- ENTITY ---
        tog(cx,y,W,H,"Entity Culling (Frustum)",()->FPSBoostConfig.entityCulling,
            ()->FPSBoostConfig.entityCulling=!FPSBoostConfig.entityCulling); y+=G;
        tog(cx,y,W,H,"No Entity Shadows",()->FPSBoostConfig.noShadows,
            ()->FPSBoostConfig.noShadows=!FPSBoostConfig.noShadows); y+=G;
        tog(cx,y,W,H,"No Nametags",()->FPSBoostConfig.noNameTags,
            ()->FPSBoostConfig.noNameTags=!FPSBoostConfig.noNameTags); y+=G;

        // --- WORLD ---
        tog(cx,y,W,H,"Block Entity Culling",()->FPSBoostConfig.blockEntityCulling,
            ()->FPSBoostConfig.blockEntityCulling=!FPSBoostConfig.blockEntityCulling); y+=G;
        tog(cx,y,W,H,"Skip Underground Sky/Weather",()->FPSBoostConfig.skipUnderground,
            ()->FPSBoostConfig.skipUnderground=!FPSBoostConfig.skipUnderground); y+=G;
        tog(cx,y,W,H,"No Clouds (Always)",()->FPSBoostConfig.noClouds,
            ()->FPSBoostConfig.noClouds=!FPSBoostConfig.noClouds); y+=G;
        tog(cx,y,W,H,"No View Bob",()->FPSBoostConfig.noViewBob,
            ()->FPSBoostConfig.noViewBob=!FPSBoostConfig.noViewBob); y+=G;

        // --- PARTICLES & RENDER DIST ---
        tog(cx,y,W,H,"Particle Limit",()->FPSBoostConfig.particleLimit,
            ()->FPSBoostConfig.particleLimit=!FPSBoostConfig.particleLimit); y+=G;
        tog(cx,y,W,H,"Extended Render Dist",()->FPSBoostConfig.extendedRender,
            ()->{FPSBoostConfig.extendedRender=!FPSBoostConfig.extendedRender;
                 FPSBoostConfig.applyRenderDistance();}); y+=G;
        tog(cx,y,W,H,"Dynamic Render Dist",()->FPSBoostConfig.dynamicRender,
            ()->FPSBoostConfig.dynamicRender=!FPSBoostConfig.dynamicRender); y+=G;
        tog(cx,y,W,H,"Save CPU Unfocused",()->FPSBoostConfig.skipUnfocused,
            ()->FPSBoostConfig.skipUnfocused=!FPSBoostConfig.skipUnfocused); y+=G;

        // Value rows
        val(cx,y,W,H,"Render Dist",()->FPSBoostConfig.extendRenderDist+"ch",
            ()->{FPSBoostConfig.extendRenderDist=Math.max(2,FPSBoostConfig.extendRenderDist-2);
                 if(FPSBoostConfig.extendedRender)FPSBoostConfig.applyRenderDistance();},
            ()->{FPSBoostConfig.extendRenderDist=Math.min(64,FPSBoostConfig.extendRenderDist+2);
                 if(FPSBoostConfig.extendedRender)FPSBoostConfig.applyRenderDistance();}); y+=G;
        val(cx,y,W,H,"BE Dist",()->FPSBoostConfig.blockEntityRenderDist+"b",
            ()->FPSBoostConfig.blockEntityRenderDist=Math.max(8,FPSBoostConfig.blockEntityRenderDist-8),
            ()->FPSBoostConfig.blockEntityRenderDist=Math.min(128,FPSBoostConfig.blockEntityRenderDist+8)); y+=G;
        val(cx,y,W,H,"Particles Max",()->String.valueOf(FPSBoostConfig.maxParticles),
            ()->FPSBoostConfig.maxParticles=Math.max(100,FPSBoostConfig.maxParticles-100),
            ()->FPSBoostConfig.maxParticles=Math.min(5000,FPSBoostConfig.maxParticles+100)); y+=G;
        val(cx,y,W,H,"Target FPS",()->FPSBoostConfig.targetFps+"fps",
            ()->FPSBoostConfig.targetFps=Math.max(20,FPSBoostConfig.targetFps-10),
            ()->FPSBoostConfig.targetFps=Math.min(500,FPSBoostConfig.targetFps+10)); y+=G+4;

        // MAX FPS preset button
        addRenderableWidget(Button.builder(Component.literal("MAX FPS PRESET"),b->{
            FPSBoostConfig.noShadows=true; FPSBoostConfig.noNameTags=true;
            FPSBoostConfig.noClouds=true; FPSBoostConfig.noViewBob=true;
            FPSBoostConfig.entityCulling=true; FPSBoostConfig.blockEntityCulling=true;
            FPSBoostConfig.skipUnderground=true; FPSBoostConfig.particleLimit=true;
            FPSBoostConfig.maxParticles=200;
            FPSBoostConfig.save();
            minecraft.setScreen(new FPSBoostScreen(parent));
        }).bounds(cx-W/2,y,W/2-2,H).build());
        addRenderableWidget(Button.builder(Component.literal("Save & Close"),
            b->{FPSBoostConfig.save();minecraft.setScreen(parent);})
            .bounds(cx+2,y,W/2-2,H).build());
    }

    private void tog(int cx,int y,int w,int h,String lbl,BooleanSupplier get,Runnable run){
        addRenderableWidget(Button.builder(Component.literal(lbl+": "+(get.getAsBoolean()?"ON":"OFF")),
            b->{run.run();b.setMessage(Component.literal(lbl+": "+(get.getAsBoolean()?"ON":"OFF")));})
            .bounds(cx-w/2,y,w,h).build());
    }
    private void val(int cx,int y,int w,int h,String lbl,Supplier<String> v,Runnable dec,Runnable inc){
        int bw=26;
        addRenderableWidget(Button.builder(Component.literal("<<"),b->{dec.run();minecraft.setScreen(new FPSBoostScreen(parent));}).bounds(cx-w/2,y,bw,h).build());
        addRenderableWidget(Button.builder(Component.literal(lbl+": "+v.get()),b->{}).bounds(cx-w/2+bw+2,y,w-bw*2-4,h).build());
        addRenderableWidget(Button.builder(Component.literal(">>"),b->{inc.run();minecraft.setScreen(new FPSBoostScreen(parent));}).bounds(cx+w/2-bw,y,bw,h).build());
    }

    @Override
    public void render(GuiGraphics g,int mx,int my,float dt){
        renderBackground(g,mx,my,dt);
        int fps=Minecraft.getInstance().getFps();
        String c=fps>=400?"§d":fps>=200?"§b":fps>=120?"§a":fps>=60?"§e":"§c";
        g.drawCenteredString(font,"FPSBoost v4  |  "+c+fps+" FPS",width/2,14,0xFFFFFF);
        g.drawCenteredString(font,"K = close  |  MAX FPS PRESET = enable all boosts",width/2,26,0x888888);
        super.render(g,mx,my,dt);
    }
    @Override public boolean isPauseScreen(){return false;}
}
