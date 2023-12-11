package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;
import com.game.alogrithm.Move;

public class Bullet extends Being{
    Move direction;
    int at;

    public Bullet(Texture region, int x, int y,int at,Move direction,RougerLike game) {
        super(region,x,y, RougerLike.CELL_SIZE/2,game);
        this.direction=direction;
        this.at=at;
    }
    @Override
    public void act(float delta) {
        super.act(delta);

    }
}
