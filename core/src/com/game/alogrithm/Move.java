package com.game.alogrithm;

public enum Move {
    UP(0,1),DOWN(0,-1),LEFT(-1,0),RIGHT(1,0),
    UPRight(1,1),UPLeft(-1,1),DOWNRight(1,-1),DOWNLeft(-1,-1);
    private int x;
    private int y;
    Move(int x,int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
