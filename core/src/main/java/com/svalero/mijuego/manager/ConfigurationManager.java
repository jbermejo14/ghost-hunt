package com.svalero.mijuego.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import static com.svalero.mijuego.util.Constants.GAME_NAME;

public class ConfigurationManager {

    private static Preferences prefs;

    public static void loadPreferences() {
        prefs = Gdx.app.getPreferences(GAME_NAME);
    }
}
