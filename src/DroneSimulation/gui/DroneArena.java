package DroneSimulation.gui;

import java.util.ArrayList;
import java.util.Random;

/**
 * A drone arena which contains objects, all of which are to be drawn on the canvas.
 * @author Harshil Surendralal bf000259
 */
public class DroneArena {

    private Random randomGenerator = new Random();
    public double arenaX, arenaY, randomX, randomY, randomAngle;
    private ArrayList<Object> objects;

    /**
     * Create an object of type DroneArena that holds the width and height of the drone arena, and the objects within.
     * @param arenaX The width of the drone arena.
     * @param arenaY The height of the drone arena.
     */
    public DroneArena(double arenaX, double arenaY) {
        this.arenaX = arenaX;
        this.arenaY = arenaY;
        this.randomAngle = randomGenerator.nextInt();
        this.objects = new ArrayList<Object>();
    }

    /**
     * @return The width of the drone arena.
     */
    public double getArenaX() {
        return arenaX;
    }

    /**
     * @return The height of the drone arena.
     */
    public double getArenaY() {
        return arenaY;
    }

    /**
     * Add a drone into the drone arena.
     */
    public void addDrone() {
        int xLim = (int)arenaX - 40;
        int yLim = (int)arenaY - 40;

        randomX = randomGenerator.nextInt(xLim) + 15;
        randomY = randomGenerator.nextInt(yLim) + 15;
        randomAngle = randomGenerator.nextInt(360);

        objects.add(new Drone(randomX, randomY, 15, randomAngle, 1));
    }

    /**
     * Add a hunter drone into the drone arena.
     */
    public void addHunterDrone() {
        int xLim = (int)arenaX - 40;
        int yLim = (int)arenaY - 40;

        randomX = randomGenerator.nextInt(xLim) + 15;
        randomY = randomGenerator.nextInt(yLim) + 15;
        randomAngle = randomGenerator.nextInt(360);

        objects.add(new HunterDrone(randomX, randomY, 15, randomAngle, 1));
    }

    /**
     * Add an obstacle into the drone arena.
     * @param x The x co-ordinate of the obstacle.
     * @param y The y co-ordinate of the obstacle.
     */
    public void addObstacle(double x, double y) {
        objects.add(new Obstacle(x, y, 20));
    }

    /**
     * Clear the drone arena of all objects.
     */
    public void clear() {
        objects.clear();
        Object.objectCounter = 0;
    }

    /**
     * Check each object, ensuring they are not colliding into the walls of the drone arena, or other objects.
     */
    public void checkObjects() {
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            o.checkObject(this);
        }
    }

    /**
     * Move each object that is present in the drone arena, excluding the obstacles.
     */
    public void adjustObjects() {
        for (Object o : objects)
            o.adjustObject();
    }

    /**
     * Draw each object that is present in the drone arena onto the canvas.
     * @param mc The canvas on which the drone arena is to be drawn.
     */
    public void drawArena(MyCanvas mc) {
        for (Object o : objects)
            o.drawObject(mc);
    }

    /**
     * Checks whether the drone is going to hit a wall, or if the drone is going to collide with another object and
     * calculate the new angle accordingly.
     * @param x The x co-ordinate of the drone.
     * @param y The y co-ordinate of the drone.
     * @param radius The radius of the drone.
     * @param angle The angle at which the drone is moving.
     * @param objectId The object ID of the drone.
     * @return The new angle at which the drone is moving.
     */
    public double checkDroneAngle(double x, double y, double radius, double angle, int objectId) {
        double newAngle = angle;

        // if the drone is trying to go through the horizontal, i.e. the top or bottom walls, set the new angle to be
        // -(the current angle)
        if (y < radius * 1.1 || y > arenaY - radius * 1.1)
            newAngle = -angle;

        // if the drone is trying to go through the vertical, i.e. the left or right walls, set the new angle to be
        // 180 - the current angle
        if (x < radius * 1.1 || x > arenaX - radius * 1.1)
            newAngle = 180 - angle;

        // if the drone is going to collide with another object, calculate the angle that is created between this and
        // the other object
        for (Object o : objects)
            if (o.getId() != objectId && o.hitting(x, y, radius))
                newAngle = 180 * Math.atan2(y - o.getY(), x - o.getX()) / Math.PI;

        return newAngle;
    }

    /**
     * Remove an object from the drone arena.
     * @param id The ID of the object that is to be removed.
     */
    public void remove(int id) {
        objects.remove(id);
        Object.objectCounter--;

        // decrement the ID of subsequent objects
        for (int counter = id; counter < objects.size(); counter++)
            objects.get(counter).objectId--;
    }

    /**
     * Check if a drone has collided with a hunter drone, or if a hunter drone has collided with an obstacle. If this is
     * the case, remove the former from the drone arena.
     * @param id The ID of the current object.
     * @param x The x co-ordinate of the current object.
     * @param y The y co-ordinate of the current object.
     * @param radius The radius of the current object.
     * @param entity The class of which the current object is an instance of.
     * @return 1 if an object has been removed, else 0.
     */
    public int checkHit(int id, double x, double y, double radius, String entity) {
        // if the current object is a drone
        if (entity.equals("Drone")) {

            // iterate over every object, looking for hunter drones
            for (Object o : objects) {

                // if the drone has collided with a hunter drone, remove the drone from the drone arena
                if (o instanceof HunterDrone && o.hitting(x, y, radius)) {
                    objects.remove(id);
                    Object.objectCounter--;

                    for (int counter = id; counter < objects.size(); counter++)
                        objects.get(counter).objectId--;

                    return 1;
                }
            }
        }

        // if the current object is a hunter drone
        if (entity.equals("HunterDrone")) {

            // iterate over every object, looking for obstacles
            for (Object o : objects) {

                // if the hunter drone has collided with an obstacle, remove the hunter drone from the drone arena
                if (o instanceof Obstacle && o.hitting(x, y, radius)) {
                    objects.remove(id);
                    Object.objectCounter--;

                    for (int counter = id; counter < objects.size(); counter++)
                        objects.get(counter).objectId--;

                    return 1;
                }
            }
        }

        // otherwise no objects need to be removed from the drone arena
        return 0;
    }

    /**
     * Load the drone arena dimensions from a file.
     * @param x The width of the drone arena.
     * @param y The height of the drone arena.
     */
    public void loadArenaFromFile(double x, double y) {
        arenaX = x;
        arenaY = y;
    }

    /**
     * Load drones and hunter drones into the drone arena from a file.
     * @param entity The entity which is being loaded.
     * @param x The x co-ordinate of the drone.
     * @param y The y co-ordinate of the drone.
     * @param radius The radius of the drone.
     * @param angle The angle at which the drone is moving.
     * @param speed The speed at which the drone is moving.
     */
    public void loadDroneFromFile(char entity, double x, double y, double radius, double angle, double speed) {
        if (entity == 'D') {
            objects.add(new Drone(x, y, radius, angle, speed));
        } else if (entity == 'H') {
            objects.add(new HunterDrone(x, y, radius, angle, speed));
        }
    }

    /**
     * Load obstacles into the drone arena from a file.
     * @param x The x co-ordinate of the obstacle.
     * @param y The y co-ordinate of the obstacle.
     * @param radius The radius of the obstacle.
     */
    public void loadObstacleFromFile(double x, double y, double radius) {
        objects.add(new Obstacle(x, y, radius));
    }

    /**
     * @return String containing information of all drones in the drone arena, including the co-ordinates at which each
     * drone is located, and in which direction they are moving.
     */
    @Override
    public String toString() {
        String output = "";

        for (Object o : objects)
            output += o.toString() + "\n";

        return output;
    }

    /**
     * @return String containing information about the drone arena and the objects within. Intended to be used when
     * saving a drone arena.
     */
    public String saveString() {
        String output = "";

        output += "A " + arenaX + " " + arenaY + "\n";

        for (Object c : objects) {
            output += c.saveString() + "\n";
        }

        return output;

    }
}
