package com.kriss.command.fowardCommand;

import com.kriss.direction.Direction;
import com.kriss.rover.Rover;
import com.kriss.rover.postion.PositionRover;

public class FowardCommand {
    private Rover rover;

    public FowardCommand(Rover rover) {
        this.rover = rover;
    }

    public Rover moveForward() {
        PositionRover position = rover.getPosition();
        Direction direction = rover.getDirection();
        int x = position.getX();
        int y = position.getY();

        return switch (direction) {
            case N -> new Rover(new PositionRover(x, y + 1), direction);
            case E -> new Rover(new PositionRover(x + 1, y), direction);
            case S -> new Rover(new PositionRover(x, y - 1), direction);
            case W -> new Rover(new PositionRover(x - 1, y), direction);
        };
    }

    public Rover moveBackward() {
        PositionRover position = rover.getPosition();
        Direction direction = rover.getDirection();
        int x = position.getX();
        int y = position.getY();

        return switch (direction) {
            case N -> new Rover(new PositionRover(x, y - 1), direction);
            case E -> new Rover(new PositionRover(x - 1, y), direction);
            case S -> new Rover(new PositionRover(x, y + 1), direction);
            case W -> new Rover(new PositionRover(x + 1, y), direction);
        };
    }
}
