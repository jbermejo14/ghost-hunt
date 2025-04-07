package com.svalero.mijuego.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.svalero.mijuego.domain.Enemy;
import com.svalero.mijuego.domain.Projectile;

import static com.svalero.mijuego.util.Constants.TILE_HEIGHT;
import static com.svalero.mijuego.util.Constants.TILE_WIDTH;

public class RenderManager {
    private LogicManager logicManager;
    private Batch batch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private BitmapFont font = new BitmapFont();

    public RenderManager(LogicManager logicManager, TiledMap map) {
        this.logicManager = logicManager;
        updateMap(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 20 * TILE_WIDTH, 15 * TILE_HEIGHT);
        camera.update();
    }

    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        updateCamera();

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.draw(logicManager.player.getCurrentFrame(), logicManager.player.getX(), logicManager.player.getY());
        batch.draw(logicManager.santi.getCurrentFrame(), logicManager.santi.getX(), logicManager.santi.getY());

        // Draw enemies
        for (Enemy enemy : logicManager.enemies) {
            if (enemy.isAlive()) {
                batch.draw(enemy.getTexture(), enemy.getPosition().x, enemy.getPosition().y);
            }
        }

        batch.draw(logicManager.boss1.getTexture(), logicManager.boss1.getPosition().x, logicManager.boss1.getPosition().y);

        // Draw projectiles
        for (Projectile projectile : logicManager.player.getProjectiles()) {
            if (projectile.isActive()) {
                batch.draw(projectile.getTexture(), projectile.getPosition().x, projectile.getPosition().y);
            }
        }

        batch.end();
    }

    public void updateMap(TiledMap newMap) {
        if (mapRenderer != null) {
            mapRenderer.dispose(); // Dispose of the old renderer
        }
        mapRenderer = new OrthogonalTiledMapRenderer(newMap);
        batch = mapRenderer.getBatch();
        System.out.println("Map updated to: " + newMap);
    }

    public TiledMap getMap() {
        return mapRenderer.getMap();
    }



    private void updateCamera() {
        camera.position.set(
            logicManager.player.getX() + TILE_WIDTH / 2f,
            logicManager.player.getY() + TILE_HEIGHT / 2f,
            0
        );
    }

}
