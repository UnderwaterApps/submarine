package com.underwater.submarine;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.submarine.gameservices.AndroidGameServices;

public class AndroidLauncher extends AndroidApplication {
	private AndroidGameServices androidGameServices;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		SubmarineTestApp submarineTestApp = new SubmarineTestApp();
		androidGameServices = new AndroidGameServices(this, (AndroidGameServices.CLIENT_GAMES | AndroidGameServices.CLIENT_SNAPSHOT));
		submarineTestApp.gameServices = androidGameServices;
		initialize(submarineTestApp, config);
		androidGameServices.login();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		androidGameServices.onActivityResult(requestCode, resultCode, data);
		//GameHelp
	}
}
