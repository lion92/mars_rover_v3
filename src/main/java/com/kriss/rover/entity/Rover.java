package com.kriss.rover.entity;

import com.kriss.command.fowardCommand.TurnRight;
import com.kriss.direction.Direction;
import com.kriss.command.fowardCommand.FowardCommand;
import com.kriss.rover.position.PositionRover;
import com.kriss.command.fowardCommand.TurnLeft;
import jakarta.persistence.*;


import java.util.Objects;

@Entity
@Table(name = "rovers")
public class Rover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PositionRover position;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    public Rover() {
    }

    public Rover(PositionRover position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Long getId() {
        return id;
    }

    public PositionRover getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setPosition(PositionRover position) {
        this.position = position;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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
        return Objects.equals(id, rover.id) &&
                Objects.equals(position, rover.position) &&
                direction == rover.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, direction);
    }

    @Override
    public String toString() {
        return "Rover{" +
                "id=" + id +
                ", position=" + position +
                ", direction=" + direction +
                '}';
    }
}