package com.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.game.alogrithm.GuideInput;
import com.game.io.NetWork;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuideScreen extends ScreenAdapter {
    public int id;
    private Skin skin;
    public Stage stage;
    private final RougerLike game;
    public final NetWork server;
    private AssetManager manager= new AssetManager();
    private List<Integer> caption;
    private Group videoGroup;
    public GuideScreen(int id, NetWork netWork, RougerLike rougerLike) {
        this.id = id;
        this.server = netWork;
        this.game = rougerLike;
    }
    @Override
    public void show() {
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
        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showMenu();
                try {
                    server.disconnect();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.top().right(); // 定位到舞台的右上角
        table.add(menuButton).pad(10).align(Align.topRight).uniformX().minWidth(120).minHeight(80).pad(10, 0, 10, 0);
        videoGroup = new Group();
        for(int i=0;i<GameScreen.col;i++){
            for(int j=0;j<GameScreen.row;j++){
                Base base = new Base(manager.get("pix/base.png", Texture.class),i,j,null);
                stage.addActor(base);
            }
        }
        stage.addActor(videoGroup);
        stage.addActor(table);
        stage.addListener(new GuideInput(this));
        try {
            server.send(id+"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
        String s;
        List<String> n_caption=null;
        while(true){
            try{
                s = server.receive();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(s==null)
                break;
            n_caption= Arrays.stream(s.split("\\s+"))
                    .collect(Collectors.toList());
            if(Integer.parseInt(n_caption.get(0))==0){
                caption= new ArrayList<>();
                for(String i:n_caption){
                   caption.add(Integer.parseInt(i));
                }
            }
            else if(Integer.parseInt(n_caption.get(0))==-1){
                try {
                    server.disconnect();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                game.showMenu();
                return;
            }
        }
        if(caption!=null&&n_caption!=null&&caption.size()==n_caption.size()) {
            videoGroup.clear();
            for (int i = 0; i < GameScreen.col; i++) {
                for (int j = 0; j < GameScreen.row; j++) {
                    if (caption.get(i * GameScreen.row + j + 1) == 1) {
                        Wall wall = new Wall(manager.get("pix/wall.png", Texture.class), i, j, null);
                        videoGroup.addActor(wall);
                    } else if (caption.get(i * GameScreen.row + j + 1) == 3) {
                        Enemy enemy = new Enemy(manager.get("pix/enemy.png", Texture.class), i, j, null);
                        videoGroup.addActor(enemy);
                    } else if (caption.get(i * GameScreen.row + j + 1) == 4) {
                        Bullet bullet = new Bullet(manager.get("pix/bullet.png", Texture.class), i, j, 0, null, null, null);
                        videoGroup.addActor(bullet);
                    } else if (caption.get(i * GameScreen.row + j + 1) == 5) {
                        Bullet bullet = new Bullet(manager.get("pix/f_bullet.png", Texture.class), i, j, 0, null, null, null);
                        videoGroup.addActor(bullet);
                    }
                }
            }
            for(int i=GameScreen.col*GameScreen.row+1;i<caption.size();i+=4){
                int id=Integer.parseInt(n_caption.get(i));
                int x=Integer.parseInt(n_caption.get(i+1));
                int y=Integer.parseInt(n_caption.get(i+2));
                int hp=Integer.parseInt(n_caption.get(i+3));
                if(id==this.id){
                    Player player = new Player(manager.get("pix/hero.png", Texture.class), x, y,true, null);
                    player.health.set(hp);
                    videoGroup.addActor(player);
                }
                else{
                    Player player = new Player(manager.get("pix/hero.png", Texture.class), x, y,false, null);
                    player.health.set(hp);
                    videoGroup.addActor(player);
                }
            }
        }
        stage.draw();
    }
}
