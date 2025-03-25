package com.svalero.mijuego.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Character {

    protected TextureRegion currentFrame;
    protected Vector2 position;
    protected Rectangle rect;

    public Character(TextureRegion image, Vector2 position) {
        this.currentFrame = image;
        this.position = position;
        rect = new Rectangle(position.x, position.y, image.getRegionWidth(), image.getRegionHeight());
    }

    public Character(TextureRegion image) {
        this.currentFrame = image;
        position = Vector2.Zero;
        rect = new Rectangle(position.x, position.y, image.getRegionWidth(), image.getRegionHeight());
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.rect.setPosition(position);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return currentFrame.getRegionWidth();
    }

    public float getHeight() {
        return currentFrame.getRegionWidth();
    }

    public void move(float x, float y) {
        position.x += x;
        position.y += y;
        rect.setPosition(position);
    }
}
