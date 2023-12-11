package com.game.map;

import com.game.actor.Being;
import org.jetbrains.annotations.NotNull;

public class Map {
    int row;
    int col;
    private Cell[][] cells;
    public Map(int x,int y) {
        row=x;
        col=y;
        cells=new Cell[col][row];
        for(int i=0;i<col;i++)
            for(int j=0;j<row;j++)
                cells[i][j]=new Cell();
    }
    public Cell getCell(int x,int y) {
        return cells[x][y];
    }
    public synchronized void delCell(Being being) {
        cells[being.x][being.y].setBeing(null);
    }
    public synchronized void setCell(Being being) {
        cells[being.x][being.y].setBeing(being);
    }
    public synchronized boolean checkCell(int x,int y) {
        return cells[x][y].isEmpty();
    }
}
