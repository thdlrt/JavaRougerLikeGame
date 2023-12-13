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
	public Map map;
	public static int row = 12;
	public static int col = 18;
	public static int CELL_SIZE = 100;
	public static int HEIGHT = row*CELL_SIZE;
	public static int WIDTH = col*CELL_SIZE;
	public Player player;
	public Group enemyGroup;
	public Group bulletGroup;
	public Group itemGroup;
	//敌人生成频率
	public float generateTime=3f;
	public float generateTimer=0f;
	@Override
	public void create () {
		manager.load("pix/hero.png", Texture.class);
		manager.load("pix/base.png", Texture.class);
		manager.load("pix/wall.png", Texture.class);
		manager.load("pix/bullet.png", Texture.class);
		manager.load("pix/f_bullet.png", Texture.class);
		manager.load("pix/enemy.png", Texture.class);
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
		generateTimer+=delta;
		if(generateTimer>=generateTime){
			generateTimer=0;
			generateEnemy();
		}
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
		manager.dispose();
	}

	private void generateEnemy(){
		int x,y;
		do{
			x=(int)(Math.random()*col);
			y=(int)(Math.random()*row);
		}while(!map.checkCell(x,y));
		Enemy enemy = new Enemy(manager.get("pix/enemy.png", Texture.class),x,y,this);
		map.setCell(enemy);
		enemyGroup.addActor(enemy);
		Thread enemyThread = new Thread(enemy);
		enemyThread.start();
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
	//子弹的移动
	public synchronized void move(Bullet being, Move op){
		int x = being.x+op.getX();
		int y = being.y+op.getY();
		if(x<0||x>=col||y<0||y>=row||!map.checkCell(x,y)) {
			//子弹碰到敌人
			if(x>=0&&x<col&&y>=0&&y<row&&being.target.isInstance(map.getCell(x,y).getBeing()))
				((Creature) map.getCell(x,y).getBeing()).underAttack(being.at);
			bulletGroup.removeActor(being);
			map.delCell(being);
			return;
		}
		map.delCell(being);
		being.move(op);
		map.setCell(being);
	}
	public synchronized void shoot(Creature being, Move op,Class<?extends Creature>target,String pix){
		Bullet bullet = new Bullet(manager.get(pix, Texture.class),being.x+op.getX(),being.y+op.getY(),being.at,op,this,target);
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
