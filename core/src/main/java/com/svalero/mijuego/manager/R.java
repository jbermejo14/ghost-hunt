package com.svalero.mijuego.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lombok.Data;

import java.io.File;

@Data
public class R {

    private static String TEXTURE_ATLAS = "astro_v2.atlas";
    private static String TEXTURE_ATLAS_BULLET = "bullet_Atlas/bullet.atlas";
    private static String TEXTURE_ATLAS_ENEMY = "astro_v2.atlas";
    private static String SOUNDS = "sounds";

    private static AssetManager assetManager = new AssetManager();

    public static boolean update() {
        return assetManager.update();
    }

    public static void loadAllResources() {
        assetManager.load(TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_BULLET, TextureAtlas.class);
        loadAllSounds();
    }

    private static void loadAllSounds() {
        assetManager.load(SOUNDS + File.separator + "bullet.mp3", Sound.class);
        assetManager.load(SOUNDS + File.separator + "death.mp3", Sound.class);
    }

    public static Sound getSound(String name) {
        return assetManager.get(SOUNDS + File.separator + name + ".mp3", Sound.class);
    }

    public static TextureRegion getTexture(String name) {
        return assetManager.get(TEXTURE_ATLAS, TextureAtlas.class).findRegion(name);
    }

    public static TextureRegion getTextureBullet(String name) {
        return assetManager.get(TEXTURE_ATLAS_BULLET, TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getRegions(String name) {
        return assetManager.get(TEXTURE_ATLAS, TextureAtlas.class).findRegions(name);
    }

    public static void dispose() {
        // TODO
    }
}
