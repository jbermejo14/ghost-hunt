package com.svalero.mijuego.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.svalero.mijuego.Mijuego;

import static com.svalero.mijuego.util.Constants.GAME_NAME;

public class ConfigurationScreen implements Screen {

    private Mijuego game;
    private Stage stage;
    private Preferences prefs;
    private Screen backScreen;

    public ConfigurationScreen(Mijuego game, Screen backScreen) {
        this.game = game;
        this.backScreen = backScreen;
        loadPreferences();
    }

    private void loadPreferences() {
        prefs = Gdx.app.getPreferences(GAME_NAME);
    }

    private void loadStage() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        VisLabel title = new VisLabel("Settings");
        title.setFontScale(2.5f);

        VisCheckBox checkSound = new VisCheckBox("SOUND");
        checkSound.setChecked(prefs.getBoolean("sound", true));
        checkSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("sound", checkSound.isChecked());
                prefs.flush();
            }
        });

        VisTextButton backButton = new VisTextButton("BACK");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(backScreen);
            }
        });


        table.row();
        table.add(title).center();
        table.row();
        table.add(checkSound).center();
        table.row();
        table.add(backButton).center();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        loadStage();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
        stage.draw();
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
        stage.dispose();
    }
}
