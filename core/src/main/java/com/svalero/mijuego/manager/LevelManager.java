package com.svalero.mijuego.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class LevelManager {
    public TiledMap map;
    private TiledMapTileLayer groundLayer;
    private MapLayer toolLayer;
    public Batch batch;
    OrthogonalTiledMapRenderer mapRenderer;

    private LogicManager logicManager;

    public LevelManager(LogicManager logicManager) {
        this.logicManager = logicManager;

        loadCurrentLevel();
    }

    public void loadCurrentLevel() {
        map = new TmxMapLoader().load("level1.tmx");
        groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");
        toolLayer = map.getLayers().get("tool");

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        batch = mapRenderer.getBatch();
    }
}
