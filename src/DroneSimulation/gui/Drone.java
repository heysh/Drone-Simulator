package DroneSimulation.gui;

import java.text.DecimalFormat;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * A drone that can be added to the drone arena.
 * @author Harshil Surendralal bf000259
 */
public class Drone extends Object {

    private double angle;
    private static DecimalFormat df2 = new DecimalFormat("##");

    public Drone() {}

    /**
     * Create an object of type Drone that holds the co-ordinates of where it is located, the radius of the drone, the
     * angle at which it is moving, and the speed of the drone.
     * @param x The x co-ordinate of the drone.
     * @param y The y co-ordinate of the drone.
     * @param radius The radius of the drone.
     * @param angle The angle at which the drone is moving.
     * @param speed The speed at which the drone is moving.
     */
    public Drone(double x, double y, double radius, double angle, double speed) {
        super(x, y, radius, speed);
        this.radius = radius;
        this.angle = angle;
        this.speed = speed;
    }

    /**
     * Draw the drone on the canvas.
     * @param mc The canvas on which the drone is to be drawn.
     */
    @Override
    public void drawObject(MyCanvas mc) {
        mc.gc.setFill(Color.CORAL);
        mc.gc.fillArc(x - radius, y - radius, radius * 2, radius * 2, 0, 360, ArcType.ROUND);
    }

    /**
     * Check if the drone is colliding into a hunter drone, other drones, obstacles, or walls. If the drone goes out of
     * bounds, remove the drone from the drone arena and add another drone.
     * @param a The drone arena in which the drone resides.
     */
    @Override
    protected void checkObject(DroneArena a) {
        // if the drone has collided into a hunter drone, it would already have been removed from the drone arena, so
        // do nothing
        if (a.checkHit(objectId, x, y, radius, "Drone") == 1) {

        }

        // if the drone has gone out of bounds, remove the drone and add another one to replace it
        else if ((x <= radius) || (x >= a.getArenaX() - radius) || (y <= radius) || (y >= a.getArenaY() - radius)) {
            a.remove(objectId);
            a.addDrone();
        }

        // otherwise the drone will be bouncing off the walls of the drone arena, or off other drones or obstacles
        else {
            angle = a.checkDroneAngle(x, y, radius, angle, objectId);
        }
    }

    /**
     * Move the drone in the direction it is facing.
     */
    @Override
    protected void adjustObject() {
        double radAngle = angle * Math.PI / 180;
        x += speed * Math.cos(radAngle);
        y += speed * Math.sin(radAngle);
    }

    /**
     * @return String stating the co-ordinates at which the drone is located, and in which direction it is moving.
     */
    @Override
    public String toString() {
        return "Drone is at " +  df2.format(x) + ", " + df2.format(y) + " and is travelling at a " + (int)angle % 360
                + " degree angle";
    }

    /**
     * @return String containing information about the drone. Intended to be used when saving a drone arena.
     */
    @Override
    public String saveString() {
        return "D " + x + " " + y + " " + radius + " " + angle % 360 + " " + speed;
    }
}
