package DroneSimulation;

/**
 *
 * @author Harshil Surendralal bf000259
 */
public class ConsoleCanvas {

    private int canvasX, canvasY;
    private char[][] canvas;

    /**
     *
     * @param canvasX
     * @param canvasY
     */
    public ConsoleCanvas(int canvasX, int canvasY) {
        this.canvasX = canvasX;
        this.canvasY = canvasY;
        this.canvas = new char[canvasY][canvasX];

        for (int i = 0; i < canvasY; i++) {
            for (int j = 0; j < canvasX; j++) {
                if (i == 0) {
                    canvas[i][j] = '#';
                } else if (j == 0) {
                    canvas[i][j] = '#';
                } else if (i == canvasY - 1) {
                    canvas[i][j] = '#';
                } else if (j == canvasX - 1) {
                    canvas[i][j] = '#';
                } else {
                    canvas[i][j] = ' ';
                }
            }
        }
    }

    /**
     * @return The width of the console canvas.
     */
    public int getCanvasX() {
        return this.canvasX;
    }

    /**
     * @return The height of the console canvas.
     */
    public int getCanvasY() {
        return this.canvasY;
    }

    /**
     * Adds the drone to the console canvas in the location it is situated in.
     * @param x The x co-ordinate of the drone.
     * @param y The y co-ordinate of the drone.
     * @param c The console canvas to which the drone is added.
     */
    public void showIt(int x, int y, char c) {
        this.canvas[y + 1][x + 1] = c;
    }

    @Override
    public String toString() {
        String output = "";

        for (int i = 0; i < this.canvasY; i++) {
            for (int j = 0; j < this.canvasX; j++) {
                output += this.canvas[i][j];
            }
            output += "\n";
        }

        return output;
    }
}
