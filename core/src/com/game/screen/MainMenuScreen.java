package com.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.RougerLike;
import com.kotcrab.vis.ui.VisUI;

public class MainMenuScreen extends ScreenAdapter {
    public Stage stage=null;
    public Skin skin=null; // 用于按钮的样式
    public AssetManager manager= new AssetManager();
    public final RougerLike game;
    public MainMenuScreen(RougerLike rougerlike) {
        game = rougerlike;
    }

    public Skin getSkin() {
        return skin;
    }
    public Stage getStage() {
        return stage;
    }
    public RougerLike getGame() {
        return game;
    }

    @Override
    public void show() {
        manager.load("pix/menu.png", Texture.class);
        manager.finishLoading();
        Texture bgTexture = manager.get("pix/menu.png", Texture.class);
        Image backgroundImage = new Image(bgTexture);
        backgroundImage.setFillParent(true);
        stage = new Stage(new ScreenViewport());
        stage.addActor(backgroundImage);

        if (!VisUI.isLoaded()) {
            VisUI.load();
        }
        skin = VisUI.getSkin();

        // 创建按钮
        Button newGameButton = new TextButton("new game", skin);
        Button loadGameButton = new TextButton("load game", skin);
        Button multerGameButton = new TextButton("online game", skin);
        Button gameRecordsButton = new TextButton("game video", skin);
        Button exitButton = new TextButton("exit game", skin);

        // 添加按钮事件监听器
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.newGame();
            }
        });

        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.loadGame();
            }
        });
        multerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.onlineGame();
            }
        });
        gameRecordsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showVideo();
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.left();

        float screenWidthThird = Gdx.graphics.getWidth() / 3f;
        table.columnDefaults(0).width(screenWidthThird);

        table.add(newGameButton).fillX().uniformX().minHeight(80).pad(10, 0, 10, 0);
        table.row();
        table.add(multerGameButton).fillX().uniformX().minHeight(80).pad(10, 0, 10, 0);
        table.row();
        table.add(loadGameButton).fillX().uniformX().minHeight(80).pad(10, 0, 10, 0);
        table.row();
        table.add(gameRecordsButton).fillX().uniformX().minHeight(80).pad(10, 0, 10, 0);
        table.row();
        table.add(exitButton).fillX().uniformX().minHeight(80).pad(10, 0, 10, 0);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // 清屏
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        VisUI.dispose();
    }
}

