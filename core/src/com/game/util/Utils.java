package com.game.util;

import com.game.alogrithm.Move;

import java.util.Random;

public class Utils {
    public static Move getBulletDirection(float angle) {
        if (angle >= 22.5 && angle < 67.5) {
            return Move.UPRight;
        } else if (angle >= 67.5 && angle < 112.5) {
            return Move.UP;
        } else if (angle >= 112.5 && angle < 157.5) {
            return Move.UPLeft;
        } else if (angle >= 157.5 && angle < 202.5) {
            return Move.LEFT;
        } else if (angle >= 202.5 && angle < 247.5) {
            return Move.DOWNLeft;
        } else if (angle >= 247.5 && angle < 292.5) {
            return Move.DOWN;
        } else if (angle >= 292.5 && angle < 337.5) {
            return Move.DOWNRight;
        } else {
            return Move.RIGHT;
        }
    }
    public static int dis(int x1,int y1,int x2,int y2){
        return Math.abs(x1-x2)+Math.abs(y1-y2);
    }
    public static Move generateMove(int x,int y) {
        if (x == 0 && y == 1) {
            return Move.UP;
        } else if (x == 0 && y == -1) {
            return Move.DOWN;
        }else if (x == 1 && y == 0) {
            return Move.RIGHT;
        }else if (x == -1 && y == 0) {
            return Move.LEFT;
        }else if (x == 1 && y == 1) {
            return Move.UPRight;
        }else if (x == -1 && y == 1) {
            return Move.UPLeft;
        }else if (x == 1 && y == -1) {
            return Move.DOWNRight;
        }else if (x == -1 && y == -1) {
            return Move.DOWNLeft;
        }
        return null;
    }
    //随机坐标

}
