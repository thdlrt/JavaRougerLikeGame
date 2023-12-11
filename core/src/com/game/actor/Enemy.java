package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;

public class Enemy extends Creature{
    public Enemy(Texture region, int x, int y, RougerLike game) {
        super(region,x,y,100,10,game);
    }
}
