package com.svalero.mijuego.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class LevelManager {
    public TiledMap map;
    private TiledMapTileLayer groundLayer;
    private MapLayer toolLayer;
    public Batch batch;
    public int currentLevel;
    OrthogonalTiledMapRenderer mapRenderer;

    private LogicManager logicManager;

    public LevelManager(LogicManager logicManager) {
        this.logicManager = logicManager;

    }

    public TiledMap getMap() {
        return map;
    }

}
