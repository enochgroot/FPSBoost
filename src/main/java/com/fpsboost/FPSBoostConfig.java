package com.fpsboost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import java.nio.file.*;

public class FPSBoostConfig {
    // --- CORE ---
    public static boolean enabled               = true;
    // --- ENTITY ---
    public static boolean entityCulling         = true;
    public static boolean noShadows             = false;
    public static boolean noNameTags            = false;
    // --- WORLD ---
    public static boolean blockEntityCulling    = true;
    public static boolean skipUnderground       = true;  // sky/weather/clouds underground
    public static boolean noClouds              = false; // always skip clouds
    public static boolean noViewBob             = false;
    // --- PARTICLES ---
    public static boolean particleLimit         = true;
    public static int     maxParticles          = 1000;
    // --- RENDER DISTANCE ---
    public static boolean extendedRender        = false;
    public static boolean dynamicRender         = false;
    public static int     extendRenderDist      = 48;
    public static int     targetFps             = 60;
    public static int     blockEntityRenderDist = 64;
    // --- MISC ---
    public static boolean skipUnfocused         = true;

    private static int savedRenderDist = -1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private record Data(boolean enabled,boolean entityCulling,boolean noShadows,boolean noNameTags,
        boolean blockEntityCulling,boolean skipUnderground,boolean noClouds,boolean noViewBob,
        boolean particleLimit,int maxParticles,boolean extendedRender,boolean dynamicRender,
        int extendRenderDist,int targetFps,int blockEntityRenderDist,boolean skipUnfocused){}

    public static void save(){
        try{
            Minecraft mc=Minecraft.getInstance(); if(mc==null)return;
            Path cfg=mc.gameDirectory.toPath().resolve("config/fpsboost.json");
            Files.createDirectories(cfg.getParent());
            Files.writeString(cfg,GSON.toJson(new Data(enabled,entityCulling,noShadows,noNameTags,
                blockEntityCulling,skipUnderground,noClouds,noViewBob,
                particleLimit,maxParticles,extendedRender,dynamicRender,
                extendRenderDist,targetFps,blockEntityRenderDist,skipUnfocused)));
        }catch(Exception e){System.err.println("[FPSBoost] Save failed: "+e.getMessage());}
    }
    public static void load(){
        try{
            Minecraft mc=Minecraft.getInstance(); if(mc==null)return;
            Path cfg=mc.gameDirectory.toPath().resolve("config/fpsboost.json");
            if(!Files.exists(cfg))return;
            Data d=GSON.fromJson(Files.readString(cfg),Data.class); if(d==null)return;
            enabled=d.enabled();entityCulling=d.entityCulling();noShadows=d.noShadows();
            noNameTags=d.noNameTags();blockEntityCulling=d.blockEntityCulling();
            skipUnderground=d.skipUnderground();noClouds=d.noClouds();noViewBob=d.noViewBob();
            particleLimit=d.particleLimit();maxParticles=d.maxParticles();
            extendedRender=d.extendedRender();dynamicRender=d.dynamicRender();
            extendRenderDist=d.extendRenderDist();targetFps=d.targetFps();
            blockEntityRenderDist=d.blockEntityRenderDist();skipUnfocused=d.skipUnfocused();
        }catch(Exception e){System.err.println("[FPSBoost] Load failed: "+e.getMessage());}
    }
    public static void applyRenderDistance(){
        Minecraft mc=Minecraft.getInstance(); if(mc==null||mc.options==null)return;
        if(extendedRender&&savedRenderDist==-1){
            savedRenderDist=mc.options.renderDistance().get();
            mc.options.renderDistance().set(extendRenderDist);
            if(mc.levelRenderer!=null)mc.levelRenderer.needsUpdate();
        }else if(!extendedRender&&savedRenderDist!=-1){
            mc.options.renderDistance().set(savedRenderDist);
            savedRenderDist=-1;
            if(mc.levelRenderer!=null)mc.levelRenderer.needsUpdate();
        }
    }
    private static int dynCd=0;
    public static void tickDynamic(){
        if(!enabled||!dynamicRender)return;
        Minecraft mc=Minecraft.getInstance(); if(mc==null||mc.level==null)return;
        if(--dynCd>0)return; dynCd=40;
        int fps=mc.getFps(),cur=mc.options.renderDistance().get();
        if(fps<targetFps-5&&cur>2){mc.options.renderDistance().set(cur-2);mc.levelRenderer.needsUpdate();}
        else if(fps>targetFps+10){int cap=extendedRender?extendRenderDist:32;
            if(cur<cap){mc.options.renderDistance().set(cur+2);mc.levelRenderer.needsUpdate();}}
    }
    public static boolean isUnderground(){
        Minecraft mc=Minecraft.getInstance();
        if(mc==null||mc.level==null||mc.player==null)return false;
        return mc.level.getBrightness(net.minecraft.world.level.LightLayer.SKY,
            mc.player.blockPosition())==0;
    }
}
