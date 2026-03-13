import com.kriss.command.fowardCommand.FowardCommand;
import com.kriss.direction.Direction;
import com.kriss.rover.position.PositionRover;
import com.kriss.rover.entity.Rover;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoverTest {
    @Test
    void should_return_N_0_0_when_the_rover_is_in_initial_position() {
        //given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when
        //it don t move

        //then
        Assertions.assertEquals(0, rover.getPosition().getX());
        Assertions.assertEquals(0, rover.getPosition().getY());
        Assertions.assertEquals(Direction.N, rover.getDirection());
    }

    @Test
    void should_return_N_0_1_when_the_rover_is_in_initial_position_and_it_go_foward_then_the_rover_is_in_0_1_N() {
        //Given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when

        Rover roverAfterMove = new FowardCommand(rover).moveForward();

        //then

        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(1, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.N, roverAfterMove.getDirection());
    }

    @Test
    void should_return_E_0_0_when_the_rover_is_in_initial_position_and_it_turn_right_then_the_rover_is_in_0_0_N() {
        //Given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when

        Rover roverAfterMove = rover.turnRight();

        //then
        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(0, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.E, roverAfterMove.getDirection());
    }

    @Test
    void should_return_W_0_0_when_the_rover_is_in_initial_position_and_it_turnLeft_then_the_rover_is_in_0_0_N() {
        //Given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when

        Rover roverAfterMove = rover.turnLeft();

        //then
        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(0, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.W, roverAfterMove.getDirection());
    }

    @Test
    void should_return_S_0_0_when_the_rover_turn_twice_right_when_it_is_in_N_0_0() {
        //Given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when

        Rover roverAfterMove = rover.turnRight().turnRight();

        //then
        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(0, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.S, roverAfterMove.getDirection());
    }

    @Test
    void should_return_S_0_0_when_the_rover_turn_twice_left_when_it_is_in_N_0_0() {
        //given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when
        Rover roverAfterMove = rover.turnLeft().turnLeft();

        //then
        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(0, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.S, roverAfterMove.getDirection());
    }

    @Test
    void should_return_S_0_moins1_when_the_rover_turn_twice_left_and_move_foward_when_it_is_in_N_0_0() {
        //given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when
        Rover roverAfterMove = rover.executeCommand("LLF");

        //then

        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(-1, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.S, roverAfterMove.getDirection());
    }

    @Test
    void should_return_S_0_moins2_when_the_rover_turn_twice_left_and_move_foward_twice_when_it_is_in_N_0_0() {
        //given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when
        Rover roverAfterMove = rover.executeCommand("LLFF");

        //then

        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(-2, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.S, roverAfterMove.getDirection());
    }

    @Test
    void should_return_to_origin_when_executeCommandBackward_undoes_executeCommand() {
        //given
        PositionRover position = new PositionRover(0, 0);
        Direction direction = Direction.N;
        Rover rover = new Rover(position, direction);

        //when — avance LLFF puis annule avec executeCommandBackward
        Rover roverAfterMove = rover.executeCommand("LLFF").executeCommandBackward("LLFF");

        //then — doit revenir à la position initiale
        Assertions.assertEquals(0, roverAfterMove.getPosition().getX());
        Assertions.assertEquals(0, roverAfterMove.getPosition().getY());
        Assertions.assertEquals(Direction.N, roverAfterMove.getDirection());
    }

}
