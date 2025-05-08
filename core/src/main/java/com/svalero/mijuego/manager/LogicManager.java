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
import static com.svalero.mijuego.screen.ConfigurationScreen.sound;
import static com.svalero.mijuego.util.Constants.PLAYER_RUNNING_SPEED;

import com.svalero.mijuego.screen.GameOver;
import com.svalero.mijuego.screen.GameScreen;
import com.svalero.mijuego.screen.GameScreen2;

public class LogicManager {
    int level;
    public Player player;
    private Mijuego game;
    Array<Enemy> enemies = new Array<>();
    public Boolean gameOver = false;
    private static int remainingEnemies = 0;
    public Santi santi;
    private RenderManager renderManager;
    private int totalEnemiesKilled;
    public Boss boss1;

    public LogicManager(Mijuego game, int level, String map_string, String ground) {
        this.game = game;
        this.level = level;
        this.renderManager = new RenderManager(this, loadMap(map_string));
        this.boss1 = new Boss(R.getTextureBoss("boss-idle"), 600, 400, 50, 20, 300, this);

        this.gameOver = false;
        TiledMap map = renderManager.getMap();
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get(ground);
        initializeGameObjects(collisionLayer);
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
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.shoot();
        }
    }

    private TiledMap loadMap(String mapPath) {
        return new TmxMapLoader().load(mapPath);
    }

    private void initializeGameObjects(TiledMapTileLayer collisionLayer) {
        System.out.println(collisionLayer);
        player = new Player(R.getTexture("astro_idle_right"), collisionLayer);
        santi = new Santi(R.getTexture("astro_idle_right"), collisionLayer);
        createEnemies();
    }

    private void createEnemies() {
        Enemy enemy1 = new Enemy(R.getTextureEnemy("ghost-idle"), 300, 100, 75, 75, 400, this);
        Enemy enemy2 = new Enemy(R.getTextureEnemy("ghost-idle"), 350, 600, 75, 75, 400, this);
        Enemy enemy3 = new Enemy(R.getTextureEnemy("ghost-idle"), 100, 1000, 75, 75, 400, this);
        Enemy enemy4 = new Enemy(R.getTextureEnemy("ghost-idle"), 350, 400, 75, 75, 400, this);


        enemy1.setPlayer(player);
        enemy2.setPlayer(player);
        enemy3.setPlayer(player);
        enemy4.setPlayer(player);

        this.boss1.setPlayer(player);

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

            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && projectile.getBounds().overlaps(new Rectangle(enemy.getPosition().x, enemy.getPosition().y, TILE_WIDTH, TILE_HEIGHT))) {
                    projectile.setActive(false);
                    if (sound) {
                        R.getSound("death").play();
                    }
                    enemy.kill();
                    enemiesToRemove.add(enemy);
                }
            }
            if (projectile.getBounds().overlaps(new Rectangle(this.boss1.getPosition().x, this.boss1.getPosition().y, TILE_WIDTH, TILE_HEIGHT))) {
                this.boss1.lives = this.boss1.lives - 1;
                System.out.println("lives: " + this.boss1.lives);
                this.boss1.kill();
                projectiles.clear();
            }
        }

        for (Enemy enemy : enemiesToRemove) {
            enemies.removeValue(enemy, true);
        }

        updateRemainingEnemies();
    }

    private boolean levelChanged = false;

    private int getTotalEnemiesKilled() {
        if (this.level == 2) {
            return totalEnemiesKilled = 8 - remainingEnemies;
        } else if (this.level == 1) {
            return totalEnemiesKilled = 4 - remainingEnemies;
        } else {
            return 0;
        }
    }

    private void updateRemainingEnemies() {
        remainingEnemies = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                remainingEnemies++;
            }
        }
        System.out.println(this.level);
        if (this.level == 2) {
            System.out.println("lere");
            if (this.boss1.isAlive()) {
                remainingEnemies++;
            }
        }

        if (remainingEnemies == 0 && !levelChanged) {
            levelChanged = true;
            if (!this.boss1.isAlive()) {
                endGame();
            } else {
                changeToNextLevel();
            }
        } else if (remainingEnemies > 0) {
            levelChanged = false;
        }
    }

    private void changeToNextLevel() {
        System.out.println("Loading Level 2...");
        this.level = 2;

        TiledMap newMap = loadMap("level2.tmx");
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) newMap.getLayers().get("ground2");

        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen2(game, this.level));
        renderManager.updateMap(newMap);

        initializeGameObjects(collisionLayer);
    }

    public static int getRemainingEnemies() {
        return remainingEnemies;
    }

    public void endGame() {
        this.game.pause = true;
        System.out.println(this.level);
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOver(game, this.level, getTotalEnemiesKilled()));
    }

    public void update(float dt) {
        managePlayerInput(dt);
        player.update(dt);
        updateProjectiles(dt);
        santi.update(dt, player);

        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }

        if (this.level == 2) {
            this.boss1.update(dt);
        }

        if (remainingEnemies <= 0) {
            if (!this.boss1.isAlive()) {
                endGame();
                System.out.println("executed");
            } else {
                System.out.println("executed2");
                changeToNextLevel();
            }        }
    }
}
