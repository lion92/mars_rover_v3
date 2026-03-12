package com.kriss.turnleft;

import com.kriss.direction.Direction;
import com.kriss.rover.Rover;

public class TurnLeft {
    private Rover rover;
    public TurnLeft(Rover rover) {
        this.rover = rover;
    }

    public Rover execute() {

        Direction direction = rover.getDirection();

        return switch (direction) {
            case N -> new Rover(
                    rover.getPosition(),
                    Direction.W
            );
            case E -> new Rover(
                    rover.getPosition(),
                    Direction.N
            );
            case S -> new Rover(
                    rover.getPosition(),
                    Direction.E
            );
            case W -> new Rover(
                    rover.getPosition(),
                    Direction.S
            );
        };
    }
}