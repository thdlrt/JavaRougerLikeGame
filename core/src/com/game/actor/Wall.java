package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;
import com.game.screen.GameScreen;

public class Wall extends Being{
    public Wall(Texture region, int x, int y, GameScreen game) {
        super(region,x,y,GameScreen.CELL_SIZE,game);
    }

}
