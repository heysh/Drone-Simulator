package DroneSimulation.gui;

import java.text.DecimalFormat;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * An obstacle which can be added to the drone arena.
 * @author Harshil Surendralal
 */
public class Obstacle extends Object {

    private static DecimalFormat df2 = new DecimalFormat("#");

    /**
     * Create an object of type Obstacle that holds the co-ordinates of where it is located, and its radius.
     * @param x The x co-ordinate of the obstacle.
     * @param y The y co-ordinate of the obstacle.
     * @param radius The radius of the obstacle.
     */
    public Obstacle(double x, double y, double radius) {
        super(x, y, radius,0);
        this.radius = radius;
    }

    /**
     * Draw the obstacle on the canvas.
     * @param mc The canvas on which the obstacle is to be drawn.
     */
    @Override
    public void drawObject(MyCanvas mc) {
        mc.gc.setFill(Color.BLUE);
        mc.gc.fillArc(x - radius, y - radius, radius * 2, radius * 2, 0, 360, ArcType.ROUND);
    }

    /**
     * @return String stating the co-ordinates at which the obstacle is located.
     */
    @Override
    public String toString() {
        return "Obstacle is at " + df2.format(x) + ", " + df2.format(y);
    }

    /**
     * @return String containing information about the obstacle. Intended to be used when saving a drone arena.
     */
    @Override
    public String saveString() {
        return "O " + x + " " + y + " " + radius;
    }
}