package com.kriss.command.fowardCommand;

import com.kriss.direction.Direction;
import com.kriss.rover.entity.Rover;

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

    public Rover executeBackward() {

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