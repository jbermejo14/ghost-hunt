package com.svalero.mijuego.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.mijuego.Mijuego;
import com.svalero.mijuego.domain.*;

import static com.svalero.mijuego.domain.Player.State.*;
import static com.svalero.mijuego.util.Constants.*;

import static com.svalero.mijuego.util.Constants.PLAYER_RUNNING_SPEED;

import com.svalero.mijuego.screen.GameScreen;
import com.svalero.mijuego.screen.GameScreen2;

public class LogicManager {
    public Player player;
    private Mijuego game;
    Array<Enemy> enemies = new Array<>();

    private static int remainingEnemies = 0;
    public Santi santi;
    private LevelManager levelManager;
    private RenderManager renderManager;
    public static int currentLevel = 1;
    Boss boss1;

    public LogicManager(Mijuego game) {
        this.game = game;
        this.renderManager = new RenderManager(this, loadMap("level1.tmx"));
        initializeGameObjects();
    }


    private void managePlayerInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.state = RIGHT;
            player.move(PLAYER_RUNNING_SPEED * dt, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.state = LEFT;
            player.move(-PLAYER_RUNNING_SPEED * dt, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.state = UP;
            player.move(0, PLAYER_RUNNING_SPEED * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.state = DOWN;
            player.move(0, -PLAYER_RUNNING_SPEED * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.shoot();
        }
    }

    private TiledMap loadMap(String mapPath) {
        return new TmxMapLoader().load(mapPath);
    }

    private void initializeGameObjects() {
        TiledMap map = renderManager.getMap();
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("ground");
        player = new Player(R.getTexture("astro_idle_right"), collisionLayer);
        santi = new Santi(R.getTexture("astro_idle_right"), collisionLayer);
        createEnemies();
    }

    private void createEnemies() {
        Enemy enemy1 = new Enemy(R.getTextureEnemy("ghost-idle"), 300, 100, 100, 100, 300);
        Enemy enemy2 = new Enemy(R.getTextureEnemy("ghost-idle"), 350, 600, 100, 100, 300);
        Enemy enemy3 = new Enemy(R.getTextureEnemy("ghost-idle"), 100, 1000, 100, 100, 300);
        Enemy enemy4 = new Enemy(R.getTextureEnemy("ghost-idle"), 350, 400, 100, 100, 300);

        Boss boss1 = new Boss(R.getTextureBoss("boss-idle"), 600, 400, 100, 100, 300);

        this.boss1 = boss1;

        enemy1.setPlayer(player);
        enemy2.setPlayer(player);
        enemy3.setPlayer(player);
        enemy4.setPlayer(player);
        boss1.setPlayer(player);

        enemies.add(enemy1);
        enemies.add(enemy2);
        enemies.add(enemy3);
        enemies.add(enemy4);

        remainingEnemies = enemies.size;
    }
    private void updateProjectiles(float dt) {
        Array<Projectile> projectiles = player.getProjectiles();
        Array<Enemy> enemiesToRemove = new Array<>();

        for (Projectile projectile : projectiles) {
            projectile.update(dt);

            // Check if projectile hits an enemy
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && projectile.getBounds().overlaps(new Rectangle(enemy.getPosition().x, enemy.getPosition().y, TILE_WIDTH, TILE_HEIGHT))) {
                    projectile.setActive(false);
                    R.getSound("death").play();
                    enemy.kill();
                    enemiesToRemove.add(enemy);

                }
            }
        }

        for (Enemy enemy : enemiesToRemove) {
            enemies.removeValue(enemy, true);
        }

        updateRemainingEnemies();
    }

    private boolean levelChanged = false;

    private void updateRemainingEnemies() {
        remainingEnemies = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                remainingEnemies++;
            }
        }

        if (remainingEnemies == 0 && !levelChanged) {
            levelChanged = true;
            changeToNextLevel();
        } else if (remainingEnemies > 0) {
            levelChanged = false;
        }
    }

    private void changeToNextLevel() {
        System.out.println("Loading Level 2...");

        TiledMap newMap = loadMap("level2.tmx");
        if (newMap == null) {
            System.out.println("Failed to load level2.tmx");
            return; // Exit if the map failed to load
        }
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen2(game));
        renderManager.updateMap(newMap);

        initializeGameObjects();
    }

    public static int getRemainingEnemies() {
        return remainingEnemies;
    }

    public void update(float dt) {
        managePlayerInput(dt);
        player.update(dt);
        updateProjectiles(dt);
        santi.update(dt, player); // Update Santi
        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }
        this.boss1.update(dt);
        if (remainingEnemies <= 0) {
            changeToNextLevel();
        }

    }
}
