package com.svalero.mijuego.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.mijuego.manager.R;

import static com.svalero.mijuego.util.Constants.TILE_HEIGHT;
import static com.svalero.mijuego.util.Constants.TILE_WIDTH;

public class Santi extends Character {
    private TiledMapTileLayer collisionLayer;
    private Animation<TextureRegion> rightAnimation, leftAnimation;
    private float stateTime;
    private float speed = 100; // Speed of Santi
    private float detectionRange = 400; // Range to detect the player

    public Santi(TextureRegion image, TiledMapTileLayer collisionLayer) {
        super(image);
        this.collisionLayer = collisionLayer;

        rightAnimation = new Animation<>(0.15f, R.getRegions("astro_run_right1"));
        leftAnimation = new Animation<>(0.15f, R.getRegions("astro_run_left"));

        if (rightAnimation.getKeyFrames().length == 0 || leftAnimation.getKeyFrames().length == 0) {
            throw new IllegalArgumentException("Animations must have at least one frame!");
        }

        currentFrame = R.getTexture("astro_idle_right");
        setPosition(new Vector2(0, TILE_HEIGHT * 2));


    }

    public void update(float dt, Player player) {
        stateTime += dt;

        if (position.dst(player.getPosition()) < detectionRange) {
            moveTowardsPlayer(player, dt);
        }

        // Update animation based on movement
        if (isMovingRight(player)) {
            currentFrame = rightAnimation.getKeyFrame(stateTime, true);
        } else if (isMovingLeft(player)) {
            currentFrame = leftAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = R.getTexture("astro_idle_right");
        }
    }

    private void moveTowardsPlayer(Player player, float dt) {
        // Calculate direction to the player
        Vector2 direction = player.getPosition().cpy().sub(position).nor(); // Normalize the direction vector

        // Move Santi towards the player
        float moveX = direction.x * speed * dt;
        float moveY = direction.y * speed * dt;

        // Only move in the direction of the player
        if (Math.abs(moveX) > Math.abs(moveY)) {
            move(moveX, 0); // Move horizontally
        } else {
            move(0, moveY); // Move vertically
        }
    }

    private boolean isMovingRight(Player player) {
        return player.getPosition().x > position.x; // Check if player is to the right
    }

    private boolean isMovingLeft(Player player) {
        return player.getPosition().x < position.x; // Check if player is to the left
    }

    public void move(float x, float y) {
        float newX = position.x + x;
        float newY = position.y + y;

        // Check collision before updating position
        if (!isColliding(newX, position.y)) {
            position.x = newX; // Move horizontally if no collision
        }

        if (!isColliding(position.x, newY)) {
            position.y = newY; // Move vertically if no collision
        }

        rect.setPosition(position);
    }

    private boolean isColliding(float x, float y) {
        // Similar collision detection logic as in Player
        int tileX = (int) (x / TILE_WIDTH);
        int tileY = (int) (y / TILE_HEIGHT);
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);

        if (cell != null && cell.getTile() != null) {
            MapProperties properties = cell.getTile().getProperties();
            return properties.containsKey("collision") && (boolean) properties.get("collision");
        }

        return false;
    }
}

