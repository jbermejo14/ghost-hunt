package com.svalero.mijuego.screen;

import com.badlogic.gdx.Screen;
import com.svalero.mijuego.Mijuego;
import com.svalero.mijuego.manager.*;

public class GameScreen implements Screen {

    private LogicManager logicManager;
    private RenderManager renderManager;
    private LevelManager levelManager;
    private Mijuego game;

    public GameScreen(Mijuego game) {
        this.game = game;

        ConfigurationManager.loadPreferences();

        loadManagers();
    }

    private void loadManagers() {
        logicManager = new LogicManager(game);
        levelManager = new LevelManager(logicManager);

        renderManager = new RenderManager(logicManager, levelManager.map);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        if (!game.pause) {
            logicManager.update(dt);
        }
        renderManager.render();
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
    }
}
