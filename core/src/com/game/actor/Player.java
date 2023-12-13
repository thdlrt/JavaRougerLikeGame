package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.RougerLike;

public class Player extends Creature {
    public Player(Texture region, int x, int y, RougerLike game) {
        super(region,x,y,200,20,game);
        this.target=Enemy.class;
        this.bullet="pix/bullet.png";
    }

}
