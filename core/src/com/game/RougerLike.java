package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.actor.*;
import com.badlogic.gdx.Gdx;
import com.game.alogrithm.Move;
import com.game.alogrithm.PlayerInput;
import com.game.io.ReadMap;
import com.game.map.Map;

import javax.print.attribute.standard.Finishings;
import java.io.IOException;
import java.util.List;

public class RougerLike extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public AssetManager manager= new AssetManager();
	public Stage stage;
	private Map map;
	public static int row = 12;
	public static int col = 18;
	public static int CELL_SIZE = 100;
	public static int HEIGHT = row*CELL_SIZE;
	public static int WIDTH = col*CELL_SIZE;
	public Player player;
	public Group enemyGroup;
	public Group bulletGroup;
	public Group itemGroup;
	@Override
	public void create () {
		manager.load("pix/hero.png", Texture.class);
		manager.load("pix/base.png", Texture.class);
		manager.load("pix/wall.png", Texture.class);
		manager.load("pix/bullet.png", Texture.class);
		manager.finishLoading();
		batch = new SpriteBatch();
		try {
			initGame("test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
		manager.dispose();
	}

	private boolean generateEnemy(){

		return false;
	}

	public synchronized void move(Creature being, Move op){
		int x = being.x+op.getX();
		int y = being.y+op.getY();
		if(x<0||x>=col||y<0||y>=row||!map.checkCell(x,y))
			return;
		map.delCell(being);
		being.move(op);
		map.setCell(being);
	}
	public synchronized void shoot(Creature being, Move op){
		Bullet bullet = new Bullet(manager.get("pix/bullet.png", Texture.class),being.x+op.getX(),being.y+op.getY(),being.at,op,this);
		if(!map.checkCell(bullet.x,bullet.y)){
			if(map.getCell(bullet.x,bullet.y).getBeing() instanceof Creature)
				((Creature) map.getCell(bullet.x,bullet.y).getBeing()).underAttack(being.at);
			return;
		}
		map.setCell(bullet);
		bulletGroup.addActor(bullet);
	}
	//读取地图
	public void initGame(String name)
	throws IOException {
		if(stage!=null)
			stage.dispose();
		stage = new Stage();
		map = new Map(row,col);
		stage.addActor(itemGroup=new Group());
		stage.addActor(enemyGroup=new Group());
		//初始化背景
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				Base base = new Base(manager.get("pix/base.png", Texture.class),j,i,this);
				itemGroup.addActor(base);
			}
		}
		//初始化玩家
		player = new Player(manager.get("pix/hero.png", Texture.class),8,5,this);
		map.setCell(player);
		stage.addActor(player);
		Gdx.input.setInputProcessor(stage);
		PlayerInput playerInput = new PlayerInput(this);
		stage.addListener(playerInput);
		//初始化地图(障碍物)
		List<List<Integer>> res= ReadMap.readMap(name);
		for(List<Integer> i:res){
			Wall wall = new Wall(manager.get("pix/wall.png", Texture.class),i.get(1),i.get(0),this);
			map.setCell(wall);
			itemGroup.addActor(wall);
		}
		stage.addActor(bulletGroup=new Group());
	}
}
