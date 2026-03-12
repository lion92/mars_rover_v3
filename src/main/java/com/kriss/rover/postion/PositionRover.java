package com.kriss.rover.postion;

import java.util.Objects;

public class PositionRover {

    private int x;
    private int y;

    public PositionRover() {
    }

    public PositionRover(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PositionRover(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "PositionRover{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
