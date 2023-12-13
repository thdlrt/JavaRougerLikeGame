package com.game.map;

import com.game.actor.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    public int getWidth() {
        return col;
    }
    public int getHeight() {
        return row;
    }
    public List<List<Integer>> simpleCapture() {
        List<List<Integer>> map = new ArrayList<>(col);

        for (int i = 0; i < col; i++) {
            List<Integer> rowList = new ArrayList<>(row);
            for (int j = 0; j < row; j++) {
                if (cells[i][j].isEmpty()) {
                    rowList.add(0);
                } else {
                    if (cells[i][j].getBeing() instanceof Wall) {
                        rowList.add(1);
                    } else if (cells[i][j].getBeing() instanceof Player) {
                        rowList.add(2);
                    } else if (cells[i][j].getBeing() instanceof Enemy) {
                        rowList.add(3);
                    } else if (cells[i][j].getBeing() instanceof Bullet && ((Bullet) cells[i][j].getBeing()).target == Enemy.class) {
                        rowList.add(4);
                    } else if (cells[i][j].getBeing() instanceof Bullet && ((Bullet) cells[i][j].getBeing()).target == Player.class) {
                        rowList.add(5);
                    } else {
                        rowList.add(-1);
                    }
                }
            }
            map.add(rowList);
        }

        return map;
    }

}
