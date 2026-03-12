package com.kriss.rover;

import com.kriss.direction.Direction;
import com.kriss.rover.postion.PositionRover;
import com.kriss.turnleft.TurnLeft;

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

    public Rover turnLeft() {
        return new TurnLeft(this).execute();
    }

    public Rover turnRight() {
        return new Rover(
                position,
                switch (direction) {
                    case N -> Direction.E;
                    case E -> Direction.S;
                    case S -> Direction.W;
                    case W -> Direction.N;
                }
        );
    }
}
