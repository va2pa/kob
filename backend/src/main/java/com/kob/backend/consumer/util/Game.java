package com.kob.backend.consumer.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
public class Game {
    private int[][] g;
    private Integer rows;
    private Integer cols;
    private int innerWallsCount;
    private static int[] dx = {0, 1, 0, -1};
    private static int[] dy = {1, 0, -1, 0};

    public Game(int r, int c, int innerWallsCount){
        this.rows = r;
        this.cols = c;
        this.innerWallsCount = innerWallsCount;
        this.g = new int[r][c];
    }

    private boolean draw() {
        for (int i = 0;i < this.rows; i ++) {
            for (int j = 0; j < this.cols; j ++) {
                g[i][j] = 0;
            }
        }
        for (int i = 0; i < this.rows; i ++) {
            g[i][0] = g[i][this.cols - 1] = 1;
        }
        for (int i = 0; i < this.cols; i ++) {
            g[0][i] = g[rows - 1][i] = 1;
        }
        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount / 2; i ++) {
            for (int j = 0; j < 1000; j ++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 -c] == 1) {
                    continue;
                }
                if (r == 1 && c == this.cols - 2 || r == this.rows - 2 && c == 1) {
                    continue;
                }
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 -c] = 1;
                break;
            }
        }
        if(checkConnectivity(this.g, 1, this.cols - 2, this.rows - 2, 1)) {
            return true;
        }
        return false;
    }

    private boolean checkConnectivity(int[][] g, int sx, int sy, int ex, int ey) {
        if (sx == ex && sy == ey) {
            return true;
        }
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i ++) {
            int x = sx + dx[i];
            int y = sy + dy[i];
            if (g[x][y] == 1 || x < 0 || x >= this.rows || y < 0 || y >= this.cols) {
                continue;
            }
            if (checkConnectivity(g, x, y, ex, ey)) {
                g[sx][sy] = 0;
                return true;
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++) {
            if (draw()) {
                break;
            }
        }
    }
}
