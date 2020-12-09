package DroneSimulation.gui;

import javafx.scene.canvas.GraphicsContext;

/**
 * A canvas on which objects are to be drawn.
 * @author Harshil Surendralal bf000259
 */
public class MyCanvas {

    protected int canvasX, canvasY;
    protected GraphicsContext gc;

    /**
     * Create an object of type MyCanvas that holds the width and height of the canvas, and the graphics context on
     * which objects can be drawn.
     * @param gc The graphics context on which objects are drawn.
     * @param canvasX The width of the canvas.
     * @param canvasY The height of the canvas.
     */
    public MyCanvas(GraphicsContext gc, int canvasX, int canvasY) {
        this.canvasX = canvasX;
        this.canvasY = canvasY;
        this.gc = gc;
    }

    /**
     * @return The width of the canvas.
     */
    public int getCanvasX() {
        return canvasX;
    }

    /**
     * @return The height of the canvas.
     */
    public int getCanvasY() {
        return canvasY;
    }

    /**
     * Clear the canvas of all objects.
     */
    public void clearCanvas() {
        gc.clearRect(0,  0,  canvasX,  canvasY);
    }
}
