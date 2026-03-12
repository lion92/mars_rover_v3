package com.kriss;

import com.kriss.direction.Direction;
import com.kriss.rover.Rover;
import com.kriss.rover.postion.PositionRover;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Rover(new PositionRover(0,0), Direction.N).executeCommand("FFFFFF").toString());
        System.out.println(new Rover(new PositionRover(0,0), Direction.N).executeCommand("FBFBFBFB").toString());
        System.out.println(new Rover(new PositionRover(0,0), Direction.N).executeCommand("LB").toString());


    }
}