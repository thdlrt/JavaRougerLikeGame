package com.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.game.screen.GameScreen;

public class Player extends Creature {
    boolean own;
    BitmapFont font;
    BitmapFont font2 ;
    public Player(Texture region, int x, int y,boolean own, GameScreen game) {
        super(region,x,y,200,20,game);
        this.target=Enemy.class;
        this.bullet="pix/bullet.png";
        this.own=own;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font2 = new BitmapFont();
        font2.setColor(Color.RED);
    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        if(!isDead()){
            super.draw(batch,parentAlpha);
            if(own)
                font.draw(batch, "this", getX()+getWidth()-20, getY() + getHeight());
            font2.draw(batch, String.valueOf(health), getX(), getY() + getHeight());
        }
    }
}
