package com.game;

import com.badlogic.gdx.Game;
import com.game.screen.GameScreen;
import com.game.screen.MainMenuScreen;
import com.game.screen.VideoSCreen;

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
		super.dispose();
	}
	public void newGame(){
		setScreen(new GameScreen("test",false,this,false));
	}
	public void loadGame(){
		setScreen(new GameScreen("test",true,this,false));
	}
	public void showMenu(){
		setScreen(new MainMenuScreen(this));
	}
	public void showVideo(){
		setScreen(new VideoSCreen(this));
	}
	public void onlineGame(){
		setScreen(new GameScreen("test",false,this,true));
	}
}
