package com.underwater.submarine.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.HashMap;

/**
 * Created by Gev on 2/10/2016.
 */
public class ScreenManager {
    protected HashMap<String,Screen> screenMap = new HashMap<String,Screen>();
    protected Screen curScreen;

    public void addScreen(Screen screen, String uid){
        screenMap.put(uid,screen);
    }

    public void removeScreen(String uid){
        screenMap.get(uid).dispose();
    }

    public void setScreen(String uid) {
        if (this.curScreen != null) this.curScreen.hide();
        this.curScreen = screenMap.get(uid);
        if (this.curScreen != null) {
            this.curScreen.show();
            this.curScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /** @return the currently active {@link Screen}. */
    public Screen getCurScreen() {
        return curScreen;
    }
}
