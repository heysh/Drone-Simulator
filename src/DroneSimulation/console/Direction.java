package DroneSimulation;

import java.util.Random;

/**
 * Enum class which represents the direction a drone faces.
 * @author Harshil Surendralal
 */
public enum Direction {
    North, East, South, West;

    /**
     * @return A random cardinal direction.
     */
    public Direction getRandomDirection() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    /**
     * @return The next cardinal direction.
     */
    public Direction getNextDirection() {
        return values()[(ordinal() + 1) % values().length];
    }
}
