package com.svalero.mijuego.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.svalero.mijuego.Mijuego;
import com.svalero.mijuego.manager.*;

public class GameScreen2 implements Screen {

    private LogicManager logicManager;
    private RenderManager renderManager;
    private LevelManager levelManager;
    private Mijuego game;
    private int remainingEnemies;

    private BitmapFont font;
    private SpriteBatch batch;
    TiledMap map = new TmxMapLoader().load("level2.tmx");

    public GameScreen2(Mijuego game, int level) {
        this.game = game;
        ConfigurationManager.loadPreferences();
        loadManagers();
        this.logicManager = new LogicManager(game, level);
        this.renderManager = new RenderManager(logicManager, map);

        remainingEnemies = 5;
        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();
    }

    private void loadManagers() {
        levelManager = new LevelManager(logicManager);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float dt) {
        if (!game.pause) {
            logicManager.update(dt);
        }
        renderManager.render2();
        batch.begin();
        font.draw(batch, "Enemies Remaining: " + LogicManager.getRemainingEnemies(), 20, 460);
        font.draw(batch, "Level 2 ", 20, 400);

        batch.end();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        R.dispose();
        font.dispose(); // Dispose font to avoid memory leaks
        batch.dispose(); // Dispose batch
    }
}
