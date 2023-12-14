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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuideScreen extends ScreenAdapter {
    private int id;
    private Skin skin;
    private Stage stage;
    private final RougerLike game;
    public final NetWork server;
    private AssetManager manager= new AssetManager();
    private List<Integer> caption;
    private Group videoGroup;
    public GuideScreen(int id, NetWork netWork, RougerLike rougerLike) {
        this.id = id;
        this.server = netWork;
        this.game = rougerLike;
        manager.load("pix/hero.png", Texture.class);
        manager.load("pix/base.png", Texture.class);
        manager.load("pix/wall.png", Texture.class);
        manager.load("pix/bullet.png", Texture.class);
        manager.load("pix/f_bullet.png", Texture.class);
        manager.load("pix/enemy.png", Texture.class);
        manager.finishLoading();
        skin = VisUI.getSkin();
        stage = new Stage();
    }
    @Override
    public void show() {
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
        stage.addActor(videoGroup);
        stage.addActor(table);
        stage.addListener(new GuideInput(this));
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
        String s;
        List<Integer> n_caption=null;
        while(true){
            try{
                s = server.receive();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(s==null)
                break;
            n_caption= Arrays.stream(s.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            if(n_caption.get(0)==0){
                caption=n_caption;
                break;
            }
        }
        if(caption!=null&&n_caption!=null&&n_caption.get(0)==0) {
            videoGroup.clear();
            for (int i = 0; i < GameScreen.col; i++) {
                for (int j = 0; j < GameScreen.row; j++) {
                    if (caption.get(i * GameScreen.col + j + 1) == 0) {
                        Base base = new Base(manager.get("pix/base.png", Texture.class), i, j, null);
                        videoGroup.addActor(base);
                    } else if (caption.get(i * GameScreen.col + j + 1) == 1) {
                        Wall wall = new Wall(manager.get("pix/wall.png", Texture.class), i, j, null);
                        videoGroup.addActor(wall);
                    } else if (caption.get(i * GameScreen.col + j + 1) == 2) {
                        Player player = new Player(manager.get("pix/hero.png", Texture.class), i, j, null);
                        videoGroup.addActor(player);
                    } else if (caption.get(i * GameScreen.col + j + 1) == 3) {
                        Enemy enemy = new Enemy(manager.get("pix/enemy.png", Texture.class), i, j, null);
                        videoGroup.addActor(enemy);
                    } else if (caption.get(i * GameScreen.col + j + 1) == 4) {
                        Bullet bullet = new Bullet(manager.get("pix/bullet.png", Texture.class), i, j, 0, null, null, null);
                        videoGroup.addActor(bullet);
                    } else if (caption.get(i * GameScreen.col + j + 1) == 5) {
                        Bullet bullet = new Bullet(manager.get("pix/f_bullet.png", Texture.class), i, j, 0, null, null, null);
                        videoGroup.addActor(bullet);
                    } else {
                        throw new RuntimeException("Unknown type: " + caption.get(i * GameScreen.col + j + 1));
                    }
                }
            }
        }
        stage.draw();
    }
}
