package com.game.map;

import com.game.actor.Being;

public class Cell {
    private Being being;
    public Cell() {
        being=null;
    }
    public Being getBeing() {
        return being;
    }
    public void setBeing(Being being) {
        this.being = being;
    }
    public boolean isEmpty(){
        return being==null;
    }
}
