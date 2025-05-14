package com.svalero.mijuego.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.mijuego.manager.R;
import com.svalero.mijuego.screen.GameScreen;
import lombok.Data;
import static com.svalero.mijuego.screen.ConfigurationScreen.sound;

import static com.svalero.mijuego.util.Constants.TILE_HEIGHT;
import static com.svalero.mijuego.util.Constants.TILE_WIDTH;

@Data
public class Player extends Character {

    public enum State {
        RIGHT, LEFT, IDLE_RIGHT, IDLE_LEFT, UP, DOWN
    }
    private TiledMapTileLayer collisionLayer;
    private int score;
    private int lives;
    private int currentLevel;
    private Animation<TextureRegion> rightAnimation, leftAnimation;
    public State state;
    private float stateTime;
    public TiledMap map;
    private Array<Projectile> projectiles = new Array<>();
    private GameScreen gameScreen;

    public Player(TextureRegion image, TiledMapTileLayer collisionLayer) {
        super(image);
        this.collisionLayer = collisionLayer;
        score = 0;
        lives = 3;
        currentLevel = 1;
        map = new TmxMapLoader().load("level1.tmx");

        rightAnimation = new Animation<>(0.15f, R.getRegions("astro_run_right"));
        leftAnimation = new Animation<>(0.15f, R.getRegions("astro_run_left"));

        if (rightAnimation.getKeyFrames().length == 0 || leftAnimation.getKeyFrames().length == 0) {
            throw new IllegalArgumentException("Animations must have at least one frame!");
        }

        currentFrame = R.getTexture("astro_idle_right");
        state = State.IDLE_RIGHT;

        setPosition(new Vector2(0, TILE_HEIGHT * 2));
    }

    public void setCollisionLayer(TiledMapTileLayer layer) {
        this.collisionLayer = layer;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    public void shoot() {


        if (sound) {
            R.getSound("bullet").play();
        }

        float bulletSpeed = 600; // Adjust as needed
        float bulletX = position.x;
        float bulletY = position.y + TILE_HEIGHT / 2f; // Center the bullet vertically

        float bulletVelocityX = 0;
        float bulletVelocityY = 0;

        switch (state) {
            case RIGHT:
                bulletX += TILE_WIDTH; // Position the bullet to the right of the player
                bulletVelocityX = bulletSpeed; // Move right
                break;
            case LEFT:
                bulletX -= TILE_WIDTH; // Position the bullet to the left of the player
                bulletVelocityX = -bulletSpeed; // Move left
                break;
            case IDLE_RIGHT:
                bulletX += TILE_WIDTH; // Position the bullet to the right of the player
                bulletVelocityX = bulletSpeed; // Move right
                break;
            case IDLE_LEFT:
                bulletX -= TILE_WIDTH; // Position the bullet to the left of the player
                bulletVelocityX = -bulletSpeed; // Move left
                break;
            case UP:
                bulletY += TILE_HEIGHT; // Position the bullet above the player
                bulletVelocityY = bulletSpeed; // Move up
                break;
            case DOWN:
                bulletY -= TILE_HEIGHT; // Position the bullet below the player
                bulletVelocityY = -bulletSpeed; // Move down
                break;
        }

        // Create the bullet with the calculated position and velocity
        projectiles.add(new Projectile(R.getTextureBullet("bullet"), bulletX, bulletY, bulletVelocityX, bulletVelocityY));
    }

    public void update(float dt) {
        stateTime += dt;

        switch (state) {
            case RIGHT:
                currentFrame = rightAnimation.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                currentFrame = leftAnimation.getKeyFrame(stateTime, true);
                break;
            case IDLE_LEFT:
                currentFrame = R.getTexture("astro_idle_left");
                break;
            case IDLE_RIGHT:
                currentFrame = R.getTexture("astro_idle_right");
        }
    }

    public Vector2 getPosition() {
        return position; // This method returns the player's position
    }

    public void move(float x, float y) {
        float newX = position.x + x;
        float newY = position.y + y;

        // Check collision before updating position
        if (!isColliding(newX, position.y)) {
            position.x = newX;
        }

        if (!isColliding(position.x, newY)) {
            position.y = newY;
        }

        rect.setPosition(position);
    }


    private boolean isColliding(float x, float y) {
        // Calculate the tile coordinates based on the player's position
        int tileX = (int) (x / TILE_WIDTH);
        int tileY = (int) (y / TILE_HEIGHT);

        // Get the cell at the calculated tile coordinates
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);

        // If there is a cell, check its properties for collision
        if (cell != null && cell.getTile() != null) {
            MapProperties properties = cell.getTile().getProperties();
            // Check if the tile has a collision property set to true and is not a floor
            boolean isColliding = properties.containsKey("collision") && (boolean) properties.get("collision")
                && !properties.containsKey("floor");
            return isColliding;
        }

        return false;
    }
}
