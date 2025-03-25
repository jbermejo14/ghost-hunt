package com.svalero.mijuego.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.svalero.mijuego.Mijuego;
import com.svalero.mijuego.manager.R;

public class SplashScreen implements Screen {

    private Mijuego game;
    private Texture splashTexture;
    private Image splashImage;
    private Stage stage;

    private boolean splashDone = false;

    public SplashScreen(Mijuego game) {
        this.game = game;

        splashTexture = new Texture(Gdx.files.internal("splash.png"));
        splashImage = new Image(splashTexture);

        stage = new Stage();
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f),
            Actions.delay(1.5f), Actions.run(() -> splashDone = true)));

        table.row().height(splashTexture.getHeight());
        table.add(splashImage).center();
        stage.addActor(table);

        R.loadAllResources();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE);

        stage.act();
        stage.draw();

        if (R.update()) {
            if (splashDone) {
                game.setScreen(new MainMenuScreen(game));
            }
        }
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
        splashTexture.dispose();
        stage.dispose();
    }
}
