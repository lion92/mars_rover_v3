package com.kriss.rover;

import com.kriss.command.fowardCommand.TurnRight;
import com.kriss.direction.Direction;
import com.kriss.command.fowardCommand.FowardCommand;
import com.kriss.rover.postion.PositionRover;
import com.kriss.command.fowardCommand.TurnLeft;

import java.util.Objects;

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

    public Rover moveBackward() {
        return new FowardCommand(this).moveBackward();
    }

    public Rover executeCommand(String command) {

        Rover rover = this;
        command = command.toUpperCase();
        for (char c : command.toCharArray()) {
            rover = switch (c) {
                case 'F' -> new FowardCommand(rover).moveForward();
                case 'B' -> new FowardCommand(rover).moveBackward();
                case 'L' -> rover.turnLeft();
                case 'R' -> rover.turnRight();
                default -> throw new IllegalArgumentException("Invalid command: " + c);
            };
        }
        return rover;
    }

    public Rover executeCommandBackward(String command) {

        Rover rover = this;
        command = new StringBuilder(command.toUpperCase()).reverse().toString();
        for (char c : command.toCharArray()) {
            rover = switch (c) {
                case 'F' -> new FowardCommand(rover).moveBackward();
                case 'L' -> new TurnLeft(rover).executeBackward();
                case 'R' -> new TurnRight(rover).executeBackward();
                default -> throw new IllegalArgumentException("Invalid command: " + c);
            };
        }
        return rover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rover rover = (Rover) o;
        return Objects.equals(position, rover.position) && direction == rover.direction;
    }

    @Override
    public String toString() {
        return "Rover{" +
                "position=" + position +
                ", direction=" + direction +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, direction);
    }
}
