package com.svalero.mijuego.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Boss {
    private TextureRegion texture;
    private Vector2 position;
    private boolean alive;
    private float speed; // Speed of the enemy
    private float attackRange; // Range within which the enemy can attack
    private float detectionRange; // Range within which the enemy can detect the player
    private Player player; // Reference to the player

    public Boss(TextureRegion texture, float x, float y, float speed, float attackRange, float detectionRange) {
        this.texture = texture;
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
        this.player = player; // Set the player reference
    }

    public void update(float dt) {
        if (!alive || player == null) return; // If the enemy is not alive or player is not set, do nothing

        float distanceToPlayer = position.dst(player.getPosition());
        System.out.println("Distance to player: " + distanceToPlayer); // Debugging line

        // Check if the player is within detection range
        if (distanceToPlayer < detectionRange) {
            // Move towards the player if within attack range
            if (distanceToPlayer > attackRange) {
                moveTowardsPlayer(dt);
            } else {
                attackPlayer(); // Attack logic
            }
        }
    }

    private void moveTowardsPlayer(float dt) {
        System.out.println("Enemy position: " + position); // Debugging line
        Vector2 direction = player.getPosition().cpy().sub(position).nor(); // Normalize the direction vector
        position.add(direction.scl(speed * dt)); // Move towards the player
    }

    private void attackPlayer() {
        // Implement attack logic here
        System.out.println("Enemy is attacking the player!");
    }
}
