package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.screen.GameScreen;

public class Player extends Creature {
    public Player(Texture region, int x, int y, GameScreen game) {
        super(region,x,y,200,20,game);
        this.target=Enemy.class;
        this.bullet="pix/bullet.png";
    }

}
