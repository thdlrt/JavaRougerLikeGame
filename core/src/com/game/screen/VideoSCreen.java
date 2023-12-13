package com.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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
import com.game.io.GameVideo;
import com.kotcrab.vis.ui.VisUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VideoSCreen extends ScreenAdapter {
    public Stage stage;
    public float time;
    public int cnt;
    List<List<List<Integer>>> video;
    public AssetManager manager= new AssetManager();
    RougerLike game;
    Group videoGroup;
    public VideoSCreen(RougerLike game) {
        manager.load("pix/hero.png", Texture.class);
        manager.load("pix/base.png", Texture.class);
        manager.load("pix/wall.png", Texture.class);
        manager.load("pix/bullet.png", Texture.class);
        manager.load("pix/f_bullet.png", Texture.class);
        manager.load("pix/enemy.png", Texture.class);
        manager.finishLoading();
        stage = new Stage();
        time = 0;
        cnt = 0;
        this.game=game;
    }
    @Override
    public void show() {
        // 读取地图
        Gdx.input.setInputProcessor(stage);
        video = new ArrayList<>();
        List<List<Integer>> frame = new ArrayList<>();
        List<String> allLines = null;
        try {
            allLines = Files.readAllLines(Paths.get("video/video.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String line : allLines) {
            if (line.trim().isEmpty()) {
                if (!frame.isEmpty()) {
                    video.add(frame);
                    frame = new ArrayList<>();
                }
            } else {
                List<Integer> row = new ArrayList<>();
                for (String part : line.split("\\s+")) {
                    row.add(Integer.parseInt(part.trim()));
                }
                frame.add(row);
            }
        }
        if (!frame.isEmpty()) {
            video.add(frame);
        }
        // 渲染ui
        Skin skin = VisUI.getSkin();
        TextButton menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showMenu();
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.top().right(); // 定位到舞台的右上角
        table.add(menuButton).pad(10).align(Align.topRight).uniformX().minWidth(120).minHeight(80).pad(10, 0, 10, 0);;
        videoGroup = new Group();
        stage.addActor(videoGroup);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1); // 设置清屏颜色为灰色
        super.render(delta);
        time += delta;
        if (time > 0.2f) {
            time = 0;
            //渲染
            videoGroup.clear();
            for(int i=0;i<GameScreen.col;i++)
                for(int j=0;j<GameScreen.row;j++) {
                    if (video.get(cnt).get(i).get(j) == 0) {
                        Base base = new Base(manager.get("pix/base.png", Texture.class),i,j,null);
                        videoGroup.addActor(base);
                    } else if (video.get(cnt).get(i).get(j) == 1) {
                        Wall wall = new Wall(manager.get("pix/wall.png", Texture.class),i,j,null);
                        videoGroup.addActor(wall);
                    } else if (video.get(cnt).get(i).get(j) == 2) {
                        Player player = new Player(manager.get("pix/hero.png", Texture.class),i,j,null);
                        videoGroup.addActor(player);
                    } else if (video.get(cnt).get(i).get(j) == 3) {
                        Enemy enemy = new Enemy(manager.get("pix/enemy.png", Texture.class),i,j,null);
                        videoGroup.addActor(enemy);
                    } else if (video.get(cnt).get(i).get(j) == 4) {
                        Bullet bullet = new Bullet(manager.get("pix/bullet.png", Texture.class),i,j,0,null,null,null);
                        videoGroup.addActor(bullet);
                    } else if (video.get(cnt).get(i).get(j) == 5) {
                        Bullet bullet = new Bullet(manager.get("pix/f_bullet.png", Texture.class),i,j,0,null,null,null);
                        videoGroup.addActor(bullet);
                    } else {
                        throw new RuntimeException("Unknown type: " + video.get(cnt).get(i).get(j));
                    }
                }
            ++cnt;
            if(cnt==video.size()){
                cnt=0;
            }
        }
        stage.draw();
    }
}
