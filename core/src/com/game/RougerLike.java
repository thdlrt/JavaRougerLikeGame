package com.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.game.io.NetWork;
import com.game.screen.*;

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
		setScreen(new GameScreen("test",false,false,this));
	}
	public void loadGame(){
		setScreen(new GameScreen("test",true,false,this));
	}
	public void showMenu(){
		setScreen(new MainMenuScreen(this));
	}
	public void showVideo(){
		setScreen(new VideoSCreen(this));
	}
	public void onlineGame(){
		NetWork server = new NetWork();
		int id=0;
		try {
			id=server.connect("localhost", 12345);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(id==0){
			GameScreen gameScreen = new GameScreen("test",false,true,this);
			gameScreen.server=server;
			setScreen(gameScreen);
		}
		else{
			setScreen(new GuideScreen(id,server,this));
		}
	}
}
