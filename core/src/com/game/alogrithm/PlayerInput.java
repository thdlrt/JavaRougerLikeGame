package com.game.alogrithm;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.game.RougerLike;
import com.game.actor.Player;
import com.game.screen.GameScreen;
import com.game.util.Utils;

public class PlayerInput extends InputListener {
    final private GameScreen rougerLike;
    final private Player player;
    public PlayerInput(GameScreen rougerLike){
        this.rougerLike=rougerLike;
        player=rougerLike.player;
    }
    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.W:{
                rougerLike.move(player,Move.UP);
                break;
            }
            case Input.Keys.A:{
                rougerLike.move(player,Move.LEFT);
                break;
            }
            case Input.Keys.S:{
                rougerLike.move(player,Move.DOWN);
                break;
            }
            case Input.Keys.D:{
                rougerLike.move(player,Move.RIGHT);
                break;
            }
            default:{

            }
        }
        return false;
    }
    @Override
    public boolean touchDown(InputEvent event, float screenX, float screenY, int pointer, int button) {

        Vector2 stageCoordinates = rougerLike.stage.screenToStageCoordinates(new Vector2(screenX, screenY));
        stageCoordinates.x-=50;
        stageCoordinates.y-=50;
        stageCoordinates.y=(GameScreen.row-1)*GameScreen.CELL_SIZE-stageCoordinates.y;
        Vector2 playerPosition = new Vector2(player.getX(), player.getY());
        Vector2 direction = new Vector2(stageCoordinates.x - playerPosition.x, stageCoordinates.y - playerPosition.y);
        direction.nor();
        float angle = direction.angleDeg();
        // 确定子弹的方向
        Move bulletDirection = Utils.getBulletDirection(angle);
        player.attack(bulletDirection);
        return true;
    }


}
