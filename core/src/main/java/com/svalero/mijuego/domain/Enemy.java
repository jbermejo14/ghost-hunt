package com.svalero.mijuego.domain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.mijuego.Mijuego;
import com.svalero.mijuego.manager.LogicManager;
import com.svalero.mijuego.screen.GameScreen;

public class Enemy {
    private TextureRegion texture;
    private Vector2 position;
    public boolean alive;
    private float speed; // Speed of the enemy
    private float attackRange; // Range within which the enemy can attack
    private float detectionRange; // Range within which the enemy can detect the player
    private Player player; // Reference to the player
    private Mijuego game;
    private LogicManager logicManager;

    public Enemy(TextureRegion texture, float x, float y, float speed, float attackRange, float detectionRange, LogicManager logicManager) {
        this.texture = texture;
        this.logicManager = logicManager;
        this.position = new Vector2(x, y);
        this.alive = true;
        this.speed = speed;
        this.attackRange = attackRange;
        this.detectionRange = detectionRange;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void update(float dt) {
        if (!alive || player == null) return;

        float distanceToPlayer = position.dst(player.getPosition());

        if (distanceToPlayer < detectionRange) {
            if (distanceToPlayer > attackRange) {
                moveTowardsPlayer(dt);
            } else {
                attackPlayer();
            }
        }
    }

    private void moveTowardsPlayer(float dt) {
        Vector2 direction = player.getPosition().cpy().sub(position).nor();
        position.add(direction.scl(speed * dt));
    }

    private void attackPlayer() {
        this.logicManager.gameOver = true;
        this.logicManager.endGame();
    }
}
