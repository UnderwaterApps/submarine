package com.underwater.submarine.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.underwater.submarine.SubmarineTestApp;
import com.uwsoft.editor.renderer.SceneLoader;

/**
 * Created by Gev on 2/10/2016.
 */
public class BillingScreen implements Screen {

    private SceneLoader sceneLoader;
    private Object engine;
    private SubmarineTestApp app;

    public BillingScreen(SubmarineTestApp app) {
        this.app = app;
        this.sceneLoader = app.sceneLoader;
        this.engine = sceneLoader.getEngine();
        create();
    }

    private void create() {

    }

    @Override
    public void show() {
        FitViewport viewport = new FitViewport(600, 800);
        this.sceneLoader.loadScene("Billing", viewport);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
