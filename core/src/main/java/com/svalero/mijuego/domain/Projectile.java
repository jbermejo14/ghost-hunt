package com.svalero.mijuego.domain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private float velocityX;
    private float velocityY;
    private TextureRegion texture;
    private Vector2 position;
    private boolean active;
    private Rectangle bounds;

    public Projectile(TextureRegion texture, float x, float y, float velocityX, float velocityY) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.active = true; // Set active to true when created
        this.bounds = new Rectangle(x, y, texture.getRegionWidth(), texture.getRegionHeight());
    }

    public void update(float dt) {
        // Update position based on velocity
        position.x += velocityX * dt;
        position.y += velocityY * dt; // Update Y position if needed
        bounds.setPosition(position.x, position.y);

        // Check if the projectile is out of bounds
        if (isOutOfBounds()) {
            active = false; // Set inactive if out of bounds
        }
    }

    public boolean isOutOfBounds() {
        return position.x < 0 || position.x > 1200 || position.y < 0 || position.y > 1200; // Change 800 and 600 to screen width and height
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            batch.draw(texture, position.x, position.y);
        }
    }
}
