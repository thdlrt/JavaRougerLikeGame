package com.game.alogrithm;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.game.io.NetWork;
import com.game.screen.GameScreen;
import com.game.screen.GuideScreen;
import com.game.util.Utils;

public class GuideInput extends InputListener {
    GuideScreen guest;
    public GuideInput(GuideScreen guest) {
        this.guest = guest;
    }
    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.W:{
                try {
                    guest.server.send(guest.id+" 1 w");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case Input.Keys.A:{
                try {
                    guest.server.send(guest.id+" 1 a");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case Input.Keys.S:{
                try {
                    guest.server.send(guest.id+" 1 s");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case Input.Keys.D:{
                try {
                    guest.server.send(guest.id+" 1 d");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            default:{

            }
        }
        return false;
    }
    @Override
    public boolean touchDown(InputEvent event, float screenX, float screenY, int pointer, int button) {

        Vector2 stageCoordinates = guest.stage.screenToStageCoordinates(new Vector2(screenX, screenY));
        stageCoordinates.x-=50;
        stageCoordinates.y-=50;
        stageCoordinates.y=(GameScreen.row-1)*GameScreen.CELL_SIZE-stageCoordinates.y;
        try {
            guest.server.send(guest.id+" 2 "+stageCoordinates.x+" "+stageCoordinates.y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
