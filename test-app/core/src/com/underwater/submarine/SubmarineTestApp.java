package com.underwater.submarine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.submarine.gameservices.GameServices;
import com.underwater.submarine.screen.BillingScreen;
import com.underwater.submarine.screen.ScreenManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class SubmarineTestApp extends ApplicationAdapter {
	private ResourceManager rm;
	private FitViewport viewport;
	public SceneLoader sceneLoader;
	public Engine engine;
	private ScreenManager screenManager;

	public GameServices gameServices;

	@Override
	public void create () {
		this.rm = new ResourceManager();
		this.rm.initAllResources();

		this.viewport = new FitViewport(600, 800);
		this.sceneLoader = new SceneLoader(this.rm);
		this.engine = sceneLoader.getEngine();
		this.sceneLoader.loadScene("MainScene", viewport);

		sceneLoader.addComponentsByTagName("button", ButtonComponent.class);

		//init all screens
		screenManager = new ScreenManager();
		screenManager.addScreen(new BillingScreen(this), BillingScreen.class.getName());

		ItemWrapper rootItem = new ItemWrapper(sceneLoader.getRoot());

		ButtonComponent buttonComponent = rootItem.getChild("billingScreen").getEntity().getComponent(ButtonComponent.class);
		buttonComponent.addListener(new ButtonComponent.ButtonListener() {
			public void touchUp() {
				screenManager.setScreen(BillingScreen.class.getName());
			}
			public void touchDown() {

			}
			public void clicked() {
				// Do something
			}
		});
		//gameServices.login();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.engine.update(Gdx.graphics.getDeltaTime());
	}
}
