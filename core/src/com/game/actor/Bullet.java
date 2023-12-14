package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;
import com.game.alogrithm.Move;
import com.game.screen.GameScreen;

public class Bullet extends Being{
    public Move direction;
    public int at;
    float distance=0.3f;
    float last;
    public Class<?extends Creature> target;

    public Bullet(Texture region, int x, int y, int at, Move direction, GameScreen game, Class<?extends Creature> target) {
        super(region,x,y, GameScreen.CELL_SIZE/2,game);
        this.direction=direction;
        this.at=at;
        last=distance-0.1f;
        this.target=target;
    }
    public void move(Move op){
        setPlace(x+op.getX(),y+op.getY());
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        last+=delta;
        if(last>0.4f){
            last=0;
            game.move(this,direction);
        }
    }
}
