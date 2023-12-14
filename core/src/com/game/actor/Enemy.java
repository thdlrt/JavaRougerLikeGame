package com.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;
import com.game.alogrithm.EnemyAi;
import com.game.alogrithm.Move;
import com.game.alogrithm.PathAlogrithm;
import com.game.screen.GameScreen;

public class Enemy extends Creature implements Runnable{
    public int maxDis=7,minDis=3;
    int moveInterval=1000;
    public Enemy(Texture region, int x, int y, GameScreen game) {
        super(region,x,y,100,10,game);
        this.target=Player.class;
        this.bullet="pix/f_bullet.png";
    }
    @Override
    public void run() {
        EnemyAi ai=new EnemyAi(game.map);
        boolean running=true;
        while (!isDead()&&running&&game.player!=null) {
            Move nextMove = ai.getNextMove(x,y,game.player.x,game.player.y,minDis);
            Move nextAttack=ai.getAttack(x,y,game.player.x,game.player.y,maxDis);
            if(nextAttack!=null){
                attack(nextAttack);
            }
            else if (nextMove != null) {
                game.move(this, nextMove);
            }
            try {
                Thread.sleep(moveInterval);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
