package com.kriss.rover.controler;

import com.kriss.direction.Direction;
import com.kriss.rover.entity.Rover;
import com.kriss.rover.RoverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rovers")
@CrossOrigin(origins = "*")
public class RoverController {

    private final RoverService roverService;

    public RoverController(RoverService roverService) {
        this.roverService = roverService;
    }

    @GetMapping
    public List<Rover> getAllRovers() {
        return roverService.getAllRovers();
    }

    @PostMapping("/{id}/move")
    public Rover move(
            @PathVariable Long id,
            @RequestParam String commands
    ) {
        return roverService.moveRover(id, commands);
    }

    @PostMapping
    public Rover createRover(
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam Direction direction
    ) {
        return roverService.createRover(x, y, direction);
    }

}