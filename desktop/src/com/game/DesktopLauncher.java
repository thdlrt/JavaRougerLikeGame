package com.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.RougerLike;
import com.game.screen.GameScreen;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(GameScreen.WIDTH, GameScreen.HEIGHT);
		config.setForegroundFPS(60);
		config.setTitle("Java RougerLike Game");
		new Lwjgl3Application(new RougerLike(), config);
	}
}
