package com.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.game.RougerLike;
import com.game.actor.*;
import com.game.alogrithm.Move;
import com.game.alogrithm.PlayerInput;
import com.game.io.GameVideo;
import com.game.io.NetWork;
import com.game.io.ReadMap;
import com.game.map.Map;
import com.game.util.Utils;
import com.kotcrab.vis.ui.VisUI;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class GameScreen extends ScreenAdapter {
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
    public String name;
    public boolean re;
    public final RougerLike game;
    private Skin skin;
    private GameVideo video;
    private boolean isOnline;
    public NetWork server;
    public HashMap<Integer,Player>players=new HashMap<>();
    //地图状态（用于网络联机）
    List<List<Integer>>caption=null;
    public GameScreen(String name, boolean re, boolean isOnline, RougerLike game){
        this.name=name;
        this.re=re;
        this.game=game;
        this.isOnline=isOnline;
    }
    @Override
    public void show () {
        manager.load("pix/hero.png", Texture.class);
        manager.load("pix/base.png", Texture.class);
        manager.load("pix/wall.png", Texture.class);
        manager.load("pix/bullet.png", Texture.class);
        manager.load("pix/f_bullet.png", Texture.class);
        manager.load("pix/enemy.png", Texture.class);
        manager.finishLoading();
        skin = VisUI.getSkin();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                video.stopCapture();
                map.detailCapture();
                game.showMenu();
                if(isOnline){
                    try {
                        server.disconnect();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.top().right(); // 定位到舞台的右上角
        table.add(menuButton).pad(10).align(Align.topRight).uniformX().minWidth(120).minHeight(80).pad(10, 0, 10, 0);;
        initGame();
        if(!re){
            try {
                newGame(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            loadGame();
        }
        stage.addActor(table);
        video = new GameVideo(map);
        video.startCapture();
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        generateTimer+=delta;
        if(generateTimer>=generateTime){
            generateTimer=0;
            //generateEnemy();
        }
        if(isOnline){
            String s;
            List<String> msg=null;
            while(true){
                try{
                    s = server.receive();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(s==null)
                    break;
                msg = Arrays.stream(s.split("\\s+"))
                        .collect(Collectors.toList());
                int id=Integer.parseInt(msg.get(0));
                if(id!=0){
                    if(!players.containsKey(id)){
                        sendMap();
                        int[] pos = generateEmptyPosition();
                        Player player = new Player(manager.get("pix/hero.png", Texture.class),pos[0],pos[1],this);
                        players.put(id,player);
                        map.setCell(player);
                        stage.addActor(player);
                    }
                    if(msg.size()>1){
                        int op = parseInt(msg.get(1));
                        if(op==1){
                            char c = msg.get(2).charAt(0);
                            if(c=='w')
                                move(players.get(id),Move.UP);
                            else if(c=='s')
                                move(players.get(id),Move.DOWN);
                            else if(c=='a')
                                move(players.get(id),Move.LEFT);
                            else if(c=='d')
                                move(players.get(id),Move.RIGHT);
                        }
                        else if(op==2){
                            float x = Float.parseFloat(msg.get(2));
                            float y = Float.parseFloat(msg.get(3));
                            Vector2 stageCoordinates = new Vector2(x,y);
                            Vector2 playerPosition = new Vector2(players.get(id).getX(), players.get(id).getY());
                            Vector2 direction = new Vector2(stageCoordinates.x - playerPosition.x, stageCoordinates.y - playerPosition.y);
                            direction.nor();
                            float angle = direction.angleDeg();
                            // 确定子弹的方向
                            Move bulletDirection = Utils.getBulletDirection(angle);
                            players.get(id).attack(bulletDirection);
                        }
                    }
                }
            }
            //地图状态
            List<List<Integer>>_caption=map.simpleCapture();
            //如果地图状态发生改变
            if(caption==null||!caption.equals(_caption)){
                caption=_caption;
                sendMap();
            }
        }
        stage.act(delta);
        stage.draw();
    }
    private void sendMap(){
        StringBuilder data= new StringBuilder("0 ");
        for(List<Integer>line:caption){
            for(int i:line){
                data.append(i).append(" ");
            }
        }
        try {
            server.send(data.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void dispose () {
        batch.dispose();
        stage.dispose();
        manager.dispose();
    }
    private int[] generateEmptyPosition(){
        int x,y;
        do{
            x=(int)(Math.random()*col);
            y=(int)(Math.random()*row);
        }while(!map.checkCell(x,y));
        return new int[]{x,y};
    }
    private void generateEnemy(){
        int[]pos=generateEmptyPosition();
        Enemy enemy = new Enemy(manager.get("pix/enemy.png", Texture.class),pos[0],pos[1],this);
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
    public void initGame(){
        if (stage != null)
            stage.dispose();
        stage = new Stage();
        map = new Map(row, col);
        stage.addActor(itemGroup = new Group());
        stage.addActor(enemyGroup = new Group());
        //初始化背景
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Base base = new Base(manager.get("pix/base.png", Texture.class), j, i, this);
                itemGroup.addActor(base);
            }
        }
        stage.addActor(bulletGroup=new Group());
        Gdx.input.setInputProcessor(stage);
    }
    public void newGame(String name) throws IOException {
        //初始化玩家
        player = new Player(manager.get("pix/hero.png", Texture.class),8,5,this);
        players.put(0,player);
        map.setCell(player);
        stage.addActor(player);
        PlayerInput playerInput = new PlayerInput(this);
        stage.addListener(playerInput);
        //初始化地图(障碍物)
        List<List<Integer>> res= ReadMap.readMap(name);
        for(List<Integer> i:res){
            Wall wall = new Wall(manager.get("pix/wall.png", Texture.class),i.get(1),i.get(0),this);
            map.setCell(wall);
            itemGroup.addActor(wall);
        }
    }
    public void loadGame(){
        Path path = Paths.get("history/resume.txt");
        List<List<Integer>> res = null;
        try {
            res = ReadMap.resumeMap(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(List<Integer>line:res){
            int x = line.get(0);
            int y = line.get(1);
            int type = line.get(2);
            if(type==1){
                Wall wall = new Wall(manager.get("pix/wall.png", Texture.class),x,y,this);
                map.setCell(wall);
                itemGroup.addActor(wall);
            }
            else if(type==2){
                player = new Player(manager.get("pix/hero.png", Texture.class),x,y,this);
                player.health.set(line.get(3));
                player.at=line.get(4);
                map.setCell(player);
                stage.addActor(player);
                Gdx.input.setInputProcessor(stage);
                PlayerInput playerInput = new PlayerInput(this);
                stage.addListener(playerInput);
            }
            else if(type==3){
                Enemy enemy = new Enemy(manager.get("pix/enemy.png", Texture.class),x,y,this);
                enemy.health.set(line.get(3));
                enemy.at=line.get(4);
                map.setCell(enemy);
                enemyGroup.addActor(enemy);
                Thread enemyThread = new Thread(enemy);
                enemyThread.start();
            }
            else if(type==4){
                Bullet bullet = new Bullet(manager.get("pix/bullet.png", Texture.class),x,y,line.get(3), Utils.generateMove(line.get(4),line.get(5)),this,Enemy.class);
                map.setCell(bullet);
                bulletGroup.addActor(bullet);
            }
            else if(type==5){
                Bullet bullet = new Bullet(manager.get("pix/f_bullet.png", Texture.class),x,y,line.get(3),Utils.generateMove(line.get(4),line.get(5)),this,Player.class);
                map.setCell(bullet);
                bulletGroup.addActor(bullet);
            }
        }
    }
}
