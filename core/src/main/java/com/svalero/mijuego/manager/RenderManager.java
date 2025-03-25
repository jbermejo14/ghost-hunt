package com.svalero.mijuego.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
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

    public RenderManager(LogicManager logicManager, TiledMap map) {
        this.logicManager = logicManager;

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        batch = mapRenderer.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 20 * TILE_WIDTH, 15 * TILE_HEIGHT);
        camera.update();
    }

    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        updateCamera();  // Move the camera to follow the player

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.draw(logicManager.player.getCurrentFrame(), logicManager.player.getX(), logicManager.player.getY());

        // Draw enemies
        for (Enemy enemy : logicManager.enemies) {
            if (enemy.isAlive()) {
                batch.draw(enemy.getTexture(), enemy.getPosition().x, enemy.getPosition().y);
            }
        }

        // Draw bullets
        for (Projectile projectile : logicManager.player.getProjectiles()) {
            if (projectile.isActive()) {
                batch.draw(projectile.getTexture(), projectile.getPosition().x, projectile.getPosition().y);
            }
        }
        batch.end();
    }

    private void updateCamera() {
        camera.position.set(
            logicManager.player.getX() + TILE_WIDTH / 2f,
            logicManager.player.getY() + TILE_HEIGHT / 2f,
            0
        );
    }
}
