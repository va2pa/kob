package com.kob.backend.consumer.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long userId;
    private Integer sx;
    private Integer sy;

    private ArrayList<Integer> stepList;

    private boolean tailIncreasing(int steps) {
        if (steps <= 10) return true;
        return steps % 3 == 0;
    }
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx;
        int y = sy;
        int steps = 0;
        for (Integer step : stepList) {
            x += dx[step];
            y += dy[step];
            cells.add(new Cell(x, y));
            if (!tailIncreasing(++ steps)) {
                cells.remove(0);
            }
        }
        return cells;
    }

    public String getStepListString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer step : stepList) {
            stringBuilder.append(step);
        }
        return stringBuilder.toString();
    }
}
