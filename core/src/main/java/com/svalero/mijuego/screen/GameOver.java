package com.svalero.mijuego.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.mijuego.Mijuego;

public class GameOver implements Screen {
    private final Mijuego game;
    private final int level;
    private final int totalEnemiesKilled;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameOver(Mijuego game, int level, int totalEnemiesKilled) {
        this.level = level;
        this.totalEnemiesKilled = totalEnemiesKilled;
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2f - 120, Gdx.graphics.getHeight() - 100);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float baseY = Gdx.graphics.getHeight() - 160;
        float lineHeight = 30;

        font.draw(batch, "Level: " + this.level, centerX - 120, baseY);
        font.draw(batch, "Total Enemies Killed: " + this.totalEnemiesKilled, centerX - 120, baseY - lineHeight);
        font.draw(batch, "Press SPACE to Restart", centerX - 140, 100);
        font.draw(batch, "Press ENTER for Main Menu", centerX - 160, 60);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            game.pause = false;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

