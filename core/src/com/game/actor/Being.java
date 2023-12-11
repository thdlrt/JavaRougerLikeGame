package com.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.RougerLike;

public class Being extends Actor {
    public int x;
    public int y;
    public int size;
    final private Sprite region;
    RougerLike game;
    Being(Texture region, int x, int y, int size,RougerLike game){
        this.region = new Sprite(region);
        this.size=size;
        this.game=game;
        setSize(size, size);
        setPlace(x,y);
    }

    protected void setPlace(int x,int y){
        this.x=x;
        this.y=y;
        int dis= RougerLike.CELL_SIZE/2-this.size/2;
        //设置渲染位置
        setPosition(x*RougerLike.CELL_SIZE+dis,y*RougerLike.CELL_SIZE+dis);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (region == null || !isVisible()) {
            return;
        }
        batch.draw(
                region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );
    }
}
