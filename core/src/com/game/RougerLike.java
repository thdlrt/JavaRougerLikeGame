package com.game;

import com.badlogic.gdx.Game;
import com.game.screen.GameScreen;
import com.game.screen.MainMenuScreen;

public class RougerLike extends Game {

	@Override
	public void create () {
		showMenu();
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {

	}
	public void newGame(){
		setScreen(new GameScreen("test",false,this));
	}
	public void showMenu(){
		setScreen(new MainMenuScreen(this));
	}
}
