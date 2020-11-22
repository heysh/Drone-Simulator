package DroneSimulation;

import java.util.Random;

/**
 *
 * @author Harshil Surendralal bf000259
 */
public enum Direction {
    North, East, South, West;

    public Direction getRandomDirection() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public Direction getNextDirection() {
        return values()[(ordinal() + 1) % values().length];
    }
}
