package com.svalero.mijuego.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.mijuego.Mijuego;
import com.svalero.mijuego.domain.Enemy;
import com.svalero.mijuego.domain.Player;
import com.svalero.mijuego.domain.Projectile;

import static com.svalero.mijuego.domain.Player.State.*;
import static com.svalero.mijuego.util.Constants.*;

import static com.svalero.mijuego.util.Constants.PLAYER_RUNNING_SPEED;

public class LogicManager {
    public Player player;
    private Mijuego game;
    TiledMap map = new TmxMapLoader().load("level1.tmx");
    Array<Enemy> enemies = new Array<>();

    public LogicManager(Mijuego game) {
        this.game = game;
        load();
    }

    private void load() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
        player = new Player(R.getTexture("astro_idle_right"), collisionLayer);
        Enemy enemy = new Enemy(R.getTexture("astro_idle_right"), 400, 600, 100, 100, 300);
        enemy.setPlayer(player);
        enemies.add(enemy);

        for (int x = 0; x < collisionLayer.getWidth(); x++) {
            for (int y = 0; y < collisionLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
            }
        }
    }

    private void managePlayerInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.state = RIGHT;
            player.move(PLAYER_RUNNING_SPEED * dt, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.state = LEFT;
            player.move(-PLAYER_RUNNING_SPEED * dt, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.state = LEFT;
            player.move(0, PLAYER_RUNNING_SPEED * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.state = LEFT;
            player.move(0, -PLAYER_RUNNING_SPEED * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.shoot();
        } else {
            if ((player.state == RIGHT) || (player.state == IDLE_RIGHT)) {
                player.state = IDLE_RIGHT;
            } else {
                player.state = IDLE_LEFT;
            }
        }
    }

    private void updateProjectiles(float dt) {
        Array<Projectile> projectiles = player.getProjectiles();

        for (Projectile projectile : projectiles) {
            projectile.update(dt);

            // Check if projectile hits an enemy
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && projectile.getBounds().overlaps(new Rectangle(enemy.getPosition().x, enemy.getPosition().y, TILE_WIDTH, TILE_HEIGHT))) {
                    projectile.setActive(false);
                    R.getSound("death").play();
                    enemy.kill();

                }
            }
        }

        // Remove inactive projectiles

        // Remove dead enemies

    }

    public void update(float dt) {
        managePlayerInput(dt);
        player.update(dt);
        updateProjectiles(dt);
        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }
    }
}
