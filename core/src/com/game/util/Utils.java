package com.game.util;

import com.game.alogrithm.Move;

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
}
