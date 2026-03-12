package com.kriss.rover;

import com.kriss.direction.Direction;
import com.kriss.rover.postion.PositionRover;

public class Rover {
    private PositionRover position;
    private Direction direction;

    public Rover() {
    }

    public Rover(PositionRover position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }


    public PositionRover getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Rover moveForward() {
        return new Rover(
                new PositionRover(position.getX(), position.getY() + 1),
                direction
        );
    }

    public Rover turnRight() {
        return switch (direction) {
            case Direction.N -> new Rover(
                    position,
                    Direction.E
            );
            case Direction.E -> new Rover(
                    position,
                    Direction.S
            );
            case Direction.S -> new Rover(
                    position,
                    Direction.W
            );
            case Direction.W -> new Rover(
                    position,
                    Direction.N
            );
        };
    }

    public Rover turnLeft() {
        return switch (direction) {
            case Direction.N -> new Rover(
                    position,
                    Direction.W
            );
            case Direction.E -> new Rover(
                    position,
                    Direction.N
            );
            case Direction.S -> new Rover(
                    position,
                    Direction.E
            );
            case Direction.W -> new Rover(
                    position,
                    Direction.S
            );
        };
    }
}
