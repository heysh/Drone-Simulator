package DroneSimulation;

/**
 * A drone that can be added to the drone arena.
 * @author Harshil Surendralal
 */
public class Drone {

    private int droneId, x, y;
    private Direction dir;
    private static int droneIdCounter = 0;

    /**
     * Creates an object of type Drone that holds the drone ID, x and y co-ordinates, the direction the drone is facing,
     * and the drone ID counter.
     * @param x The x co-ordinate of the drone.
     * @param y The y co-ordinate of the drone.
     * @param dir The direction the drone is facing.
     */
    public Drone(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.droneId = droneIdCounter++;
    }

    /**
     * @return The x co-ordinate of the drone.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return The y co-ordinate of the drone.
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return The direction the drone is facing.
     */
    public Direction getDir() {
        return this.dir;
    }

    /**
     * @return The ID of the drone.
     */
    public int getDroneId() {
        return this.droneId;
    }

    /**
     * @return The drone ID counter.
     */
    public int getDroneIdCounter() {
        return droneIdCounter;
    }

    @Override
    public String toString() {
        return "Drone " + this.droneId + " is at " + this.x + ", " + this.y + " facing " + this.dir.toString() + "\n";
    }

    /**
     * Checks whether the drone is located at the provided co-ordinates.
     * @param x The x co-ordinate that is to be checked.
     * @param y The y co-ordinate that is to be checked.
     * @return True if the drone is located at the provided co-ordinates, otherwise false.
     */
    public boolean isHere(int x, int y) {
        return this.x == x && this.y == y;
    }

    /**
     * Adds the drone to the console canvas.
     * @param c The console canvas that the drone is added to.
     */
    public void displayDrone(ConsoleCanvas c) {
        c.showIt(this.x, this.y, 'D');
    }

    /**
     * Moves the drone a single unit in the direction the drone is facing if possible, otherwise changes the direction
     * to the next cardinal direction (north, east, south and west) when moving in a clockwise fashion.
     * @param a The drone arena in which the drone resides.
     */
    public void tryToMove(DroneArena a) {
        int newX = this.x;
        int newY = this.y;

        // moves the drone one unit in the direction it's facing
        switch (this.dir.toString()) {
            case "North" -> newY -= 1;
            case "East" -> newX += 1;
            case "South" -> newY += 1;
            case "West" -> newX -= 1;
        }

        // if the drone is able to move into the new co-ordinates, then adjust the co-ordinates of the drone
        if (a.canMoveHere(newX, newY)) {
            this.x = newX;
            this.y = newY;

        // otherwise the drone is not able to move into the new co-ordinates, so change the direction instead
        } else {
            this.dir = dir.getNextDirection();
        }
    }
}
