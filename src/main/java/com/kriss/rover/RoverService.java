package com.kriss.rover;

import com.kriss.direction.Direction;
import com.kriss.rover.entity.Rover;
import com.kriss.rover.position.PositionRover;
import com.kriss.rover.repository.RoverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoverService {

    private final RoverRepository roverRepository;

    public RoverService(RoverRepository roverRepository) {
        this.roverRepository = roverRepository;
    }

    public Rover createRover(int x, int y, Direction direction) {

        Rover rover = new Rover(
                new PositionRover(x, y),
                direction
        );

        return roverRepository.save(rover);
    }

    public List<Rover> getAllRovers() {
        return roverRepository.findAll();
    }

    public Rover getRover(Long id) {
        return roverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rover not found"));
    }

    public Rover moveRover(Long id, String commands) {

        Rover rover = roverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rover not found"));

        rover = rover.executeCommand(commands);

        return roverRepository.save(rover);
    }

    public Rover moveRoverBackward(Long id, String commands) {

        Rover rover = roverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rover not found"));

        rover = rover.executeCommandBackward(commands);

        return roverRepository.save(rover);
    }

    public void deleteRover(Long id) {
        roverRepository.deleteById(id);
    }
}