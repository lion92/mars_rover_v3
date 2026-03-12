package com.kriss.rover;

import com.kriss.command.fowardCommand.TurnRight;
import com.kriss.direction.Direction;
import com.kriss.command.fowardCommand.FowardCommand;
import com.kriss.rover.postion.PositionRover;
import com.kriss.command.fowardCommand.TurnLeft;

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



    public Rover turnLeft() {
        return new TurnLeft(this).execute();
    }

    public Rover turnRight() {
        return new TurnRight(this).execute();
    }

    public Rover executeCommand(String command) {

        Rover rover = this;
        command = command.toUpperCase();
        for (char c : command.toCharArray()) {
            rover = switch (c) {
                case 'F' -> new FowardCommand(rover).moveForward();
                case 'L' -> rover.turnLeft();
                case 'R' -> rover.turnRight();
                default -> throw new IllegalArgumentException("Invalid command: " + c);
            };
        }
        return rover;
    }


}
