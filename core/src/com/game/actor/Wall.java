package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;

public class Wall extends Being{
    public Wall(Texture region, int x, int y, RougerLike game) {
        super(region,x,y,RougerLike.CELL_SIZE,game);
    }

}