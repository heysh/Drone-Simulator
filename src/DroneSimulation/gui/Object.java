package DroneSimulation.gui;

/**
 * A base class from which drones and obstacles inherit.
 * @author Harshil Surendralal bf000259
 */
public abstract class Object {

    protected int objectId;
    protected double x, y, radius, speed;
    protected static int objectCounter = 0;

    /**
     * Construct an object of type Object. Invokes the second constructor and passes preset values to it.
     */
    public Object() {
        this(50, 50, 10, 0);
    }

    /**
     * Construct an object of type Object that holds its co-ordinates, the radius and the speed.
     * @param x The x co-ordinate of the object.
     * @param y The y co-ordinate of the object.
     * @param radius The radius of the object.
     * @param speed The speed of the object.
     */
    public Object(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.objectId = objectCounter++;
    }

    /**
     * @return The object ID of the current object.
     */
    public int getId() {
        return objectId;
    }

    /**
     * @return The x co-ordinate of the current object.
     */
    public double getX() {
        return x;
    }

    /**
     * @return The y co-ordinate of the current object.
     */
    public double getY() {
        return y;
    }

    /**
     * @return The radius of the current object.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return The speed of the current object.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Will draw the object onto the canvas.
     * @param mc The canvas on which the object will be drawn.
     */
    public void drawObject(MyCanvas mc) {}

    /**
     * Will check if the object is colliding into a wall.
     * @param a The drone arena in which the object resides.
     */
    protected void checkObject(DroneArena a) {}

    /**
     * Will move the object in the direction its facing.
     */
    protected void adjustObject() {}

    /**
     * Checks whether the object at the given co-ordinates is hitting this object.
     * @param otherX The x co-ordinate of the other object.
     * @param otherY The y co-ordinate of the other object.
     * @param otherRadius The radius of the other object.
     * @return True if the two objects are hitting, else false.
     */
    public boolean hitting(double otherX, double otherY, double otherRadius) {
        // Pythagoras' Theorem: a^2 + b^2 = c^2
        double distanceBetweenObjectsSquared = Math.pow(x - otherX, 2) + Math.pow(y - otherY, 2);
        double sumOfRadiiSquared = Math.pow(otherRadius + radius, 2);
        return distanceBetweenObjectsSquared < sumOfRadiiSquared;
    }

    /**
     * @return String containing information about the object. Intended to be used when saving a drone arena.
     */
    public String saveString() {
        return "";
    }
}


