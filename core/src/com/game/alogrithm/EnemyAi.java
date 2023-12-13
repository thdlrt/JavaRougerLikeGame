package com.game.alogrithm;

import com.game.map.Map;
import com.game.util.Utils;

import java.util.*;

public class EnemyAi implements PathAlogrithm,AttackAlogrithm {
    Map map;
    public EnemyAi(Map map) {
        this.map=map;
    }
    @Override
    public Move getNextMove(int x, int y, int targetX, int targetY, int minDis) {
        if(Utils.dis(x,y,targetX,targetY)<=minDis)
            return null;
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        boolean[][] visited = new boolean[map.getWidth()][map.getHeight()];
        int[][][] predecessors = new int[map.getWidth()][map.getHeight()][2]; // 记录前驱节点
        for (int[][] row : predecessors) {
            Arrays.fill(row, new int[]{-1, -1});
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{x, y});
        visited[x][y] = true; // 标记起始点为已访问

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int curX = current[0];
            int curY = current[1];

            if (curX == targetX && curY == targetY) {
                // 从目标点反向追溯到起点
                while (!(predecessors[targetX][targetY][0] == x && predecessors[targetX][targetY][1] == y)) {
                    int tempX = predecessors[targetX][targetY][0];
                    int tempY = predecessors[targetX][targetY][1];
                    targetX = tempX;
                    targetY = tempY;
                }
                // 生成并返回第一步的移动
                return Utils.generateMove(targetX - x, targetY - y);
            }

            for (int[] dir : dirs) {
                int newX = curX + dir[0];
                int newY = curY + dir[1];

                if (newX >= 0 && newX < map.getWidth() && newY >= 0 && newY < map.getHeight() && !visited[newX][newY]
                &&(map.checkCell(newX,newY)||newX==targetX&&newY==targetY)) {
                    queue.offer(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    predecessors[newX][newY] = new int[] {curX, curY}; // 记录前驱节点
                }
            }
        }
        return null; // 如果没有路径，返回null
    }


    @Override
    public Move getAttack(int x, int y, int targetX, int targetY, int maxDis) {
        if(Utils.dis(x,y,targetX,targetY)<=maxDis){
            int dx=targetX-x;
            int dy=targetY-y;
            if(dx!=0&&dy!=0&&Math.abs(dx)!=Math.abs(dy)){
                return null;
            }
            if(dx>0)
                dx=1;
            else if(dx<0)
                dx=-1;
            if(dy>0)
                dy=1;
            else if(dy<0)
                dy=-1;
            return Utils.generateMove(dx,dy);
        }
        return null;
    }
}
