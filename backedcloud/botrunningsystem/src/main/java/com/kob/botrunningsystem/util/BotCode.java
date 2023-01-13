package com.kob.botrunningsystem.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotCode implements java.util.function.Supplier<Integer> {
    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return move(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Cell {
        private int x;
        private int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private int[] dx = {-1, 0, 1, 0};
    private int[] dy = {0, 1, 0, -1};

    private boolean tailIncreasing(int steps) {
        if (steps <= 10) return true;
        return steps % 3 == 0;
    }
    private List<Cell> getCells(int sx, int sy, String stepsStr) {
        List<Cell> cells = new ArrayList<>();
        int x = sx;
        int y = sy;
        int steps = 0;
        cells.add(new Cell(x, y));
        for (int i = 0; i < stepsStr.length(); i ++) {
            int step = stepsStr.charAt(i) - '0';
            x += dx[step];
            y += dy[step];
            cells.add(new Cell(x, y));
            if (!tailIncreasing(++ steps)) {
                cells.remove(0);
            }
        }
        return cells;
    }

    private Integer move(String input) {
        String[] strArray = input.split("#");
        String mapStr = strArray[0];
        int r = 13, c = 14;
        int[][] g = new int[r][c];
        for(int i = 0; i < r; i ++) {
            g[i][0] = g[i][c - 1] = 1;
        }
        for(int i = 0; i < c; i ++) {
            g[0][i] = g[r - 1][i] = 1;
        }
        for(int i = 0, k = 0; i < r; i ++) {
            for(int j = 0; j < c; j ++) {
                if (mapStr.charAt(k ++) == '1') {
                    g[i][j] = 1;
                }
            }
        }
        int sxMe = Integer.parseInt(strArray[1]);
        int syMe = Integer.parseInt(strArray[2]);
        String stepsStrMe = strArray[3].substring(1, strArray[3].length() - 1);
        List<Cell> cellsMe = getCells(sxMe, syMe, stepsStrMe);
        for (Cell cell : cellsMe) {
            g[cell.x][cell.y] = 1;
        }
        int sxYou = Integer.parseInt(strArray[4]);
        int syYou = Integer.parseInt(strArray[5]);
        String stepsStrYou = strArray[6].substring(1, strArray[6].length() - 1);
        List<Cell> cellsYou = getCells(sxYou, syYou, stepsStrYou);
        for (Cell cell : cellsYou) {
            g[cell.x][cell.y] = 1;
        }
        Cell currentCell = cellsMe.get(cellsMe.size() - 1);
        int x = currentCell.x;
        int y = currentCell.y;
        for(int i = 0; i < 4; i ++) {
            int nextX = x + dx[i];
            int nextY = y + dy[i];
            if (g[nextX][nextY] == 0) {
                return i;
            }
        }
        return 0;
    }
}
