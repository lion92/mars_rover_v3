package com.kriss.command.fowardCommand;

import com.kriss.direction.Direction;
import com.kriss.rover.Rover;

public class TurnRight {
    private Rover rover;

    public TurnRight(Rover rover) {
        this.rover = rover;
    }

    public Rover execute() {

        Direction direction = rover.getDirection();

        return switch (direction) {
            case N -> new Rover(
                    rover.getPosition(),
                    Direction.E
            );
            case E -> new Rover(
                    rover.getPosition(),
                    Direction.S
            );
            case S -> new Rover(
                    rover.getPosition(),
                    Direction.W
            );
            case W -> new Rover(
                    rover.getPosition(),
                    Direction.N
            );
        };
    }
}
