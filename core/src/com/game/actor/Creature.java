package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.RougerLike;
import com.game.alogrithm.Move;

import java.util.concurrent.atomic.AtomicInteger;

public class Creature extends Being {
    private Sprite region;
    //基本属性
    private AtomicInteger health = new AtomicInteger(100);
    public int at;
    private volatile boolean isDead = false;
    public Creature(Texture region,int x,int y,int health,int at,RougerLike game) {
        super(region,x,y,RougerLike.CELL_SIZE,game);
        this.health.set(health);
        this.at = at;
    }

    public void move(Move op){
        setPlace(x+op.getX(),y+op.getY());
    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        if(!isDead()){
            super.draw(batch,parentAlpha);
        }
    }
    public boolean isDead(){
        return isDead;
    }
    public void attack(Move op){
        if(isDead()){
            return;
        }
        game.shoot(this,op);
    }
    public void underAttack(int damage){
        if(isDead()){
            return;
        }
        health.addAndGet(-damage);
        if(health.get()<=0){
            isDead = true;
        }
    }
}
