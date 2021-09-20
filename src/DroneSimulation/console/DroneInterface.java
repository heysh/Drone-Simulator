package DroneSimulation;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

/**
 * A command line interface that allows the user to create and populate a drone arena.
 * @author Harshil Surendralal
 */
public class DroneInterface {

    private int arenaX, arenaY;
    private String fileName;
    private Scanner s;
    private DroneArena myArena;

    /**
     * Creates an object of type DroneInterface which allows the user to create new drone arenas, add drones to the
     * arena, get information about the drone arena and the drones within, display the drone arena on the screen, move
     * the all the drones in the drone arena once or ten times, and exit the program.
     */
    public DroneInterface() {
        s = new Scanner(System.in);

        char ch = ' ';
        do {
            System.out.print("Enter (C)reate new arena, (A)dd drone, get (I)nformation, (D)isplay drones, " +
                    "(M)ove all drones once, move all drones (T)en times, (S)ave the arena, (L)oad an arena, " +
                    "or e(X)it > ");
            ch = s.next().charAt(0);
            s.nextLine();

            switch (ch) {
                case 'C':
                case 'c':
                    arenaX = getArenaX();
                    arenaY = getArenaY();
                    myArena = new DroneArena(arenaX, arenaY);
                    break;
                case 'A':
                case 'a':
                    if (!(myArena == null)) {
                        myArena.addDrone();
                    } else {
                        System.out.println("You must first create or load an arena.");
                    }
                    break;
                case 'I':
                case 'i':
                    if (!(myArena == null)) {
                        System.out.print(myArena.toString());
                    } else {
                        System.out.println("You must first create or load an arena.");
                    }
                    break;
                case 'D':
                case 'd':
                    if (!(myArena == null)) {
                        doDisplay();
                    } else {
                        System.out.println("You must first create or load an arena.");
                    }
                    break;
                case 'M':
                case 'm':
                    if (!(myArena == null)) {
                        myArena.moveAllDrones();
                        doDisplay();
                    } else {
                        System.out.println("You must first create or load an arena.");
                    }
                    break;
                case 'T':
                case 't':
                    if (!(myArena == null)) {
                        for (int i = 0; i < 10; i++) {
                            myArena.moveAllDrones();
                            doDisplay();
                            System.out.println(myArena.toString());

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    } else {
                        System.out.println("You must first create or load an arena.");
                    }
                    break;
                case 'S':
                case 's':
                    if (!(myArena == null)) {
                        System.out.print("Enter the name of the file > ");
                        fileName = s.next();
                        myArena.saveArena(fileName);
                    } else {
                        System.out.println("You must first create or load an arena.");
                    }
                    break;
                case 'L':
                case 'l':
                    loadArena();
                    break;
                case 'x':
                    ch = 'X';
                    break;
            }
        } while (ch != 'X');

        s.close();
    }

    /**
     * Output the drone arena and all of the encompassed drones.
     */
    public void doDisplay() {
        // get the width and height of the drone arena
        int arenaX = this.myArena.getArenaX();
        int arenaY = this.myArena.getArenaY();

        // create a console canvas that is greater by 2 units in both directions to accompany the walls
        ConsoleCanvas c = new ConsoleCanvas(arenaX + 2, arenaY + 2);

        this.myArena.showDrones(c);
        System.out.println(c.toString());
    }

    /**
     * @return The width of the drone arena the user wishes to set.
     */
    public int getArenaX() {
        int arenaX;

        do {
            System.out.print("Enter the width of the arena > ");
            arenaX = this.s.nextInt();
        } while (arenaX < 1);

        return arenaX;
    }

    /**
     * @return The height of the drone arena the user wishes to set.
     */
    public int getArenaY() {
        int arenaY;

        do {
            System.out.print("Enter the height of the arena > ");
            arenaY = this.s.nextInt();
        } while (arenaY < 1);

        return arenaY;
    }

    /**
     * @return The file path of the drone arena the user wishes to load.
     */
    public String getFilePath() {
        String filePath;
        JFrame jf = new JFrame("Dialog");
        jf.setAlwaysOnTop(true);
        JFileChooser chooser = new JFileChooser();
        int response = chooser.showOpenDialog(jf);
        jf.dispose();

        if (response == JFileChooser.APPROVE_OPTION) {
            filePath = chooser.getSelectedFile().getAbsolutePath();
            return filePath;
        } else {
            return null;
        }
    }

    /**
     * Load a drone arena and the drones within from a file.
     */
    public void loadArena() {
        String filePath = getFilePath();
        Scanner s;

        int x, y, arenaX, arenaY;
        String dir, string;
        Direction direction = Direction.North;
        Drone d;
        DroneArena a = new DroneArena(-1, -1);

        try {
            s = new Scanner(new File(filePath));

            // while there are still characters left in the file
            while (s.hasNext()) {
                string = s.next();

                // if the object is a drone arena, extract the width and height values, and thus create a new DroneArena
                // object
                if (string.equals("arena")) {
                    arenaX = s.nextInt();
                    arenaY = s.nextInt();
                    a = new DroneArena(arenaX, arenaY);

                // if the object is a drone, extract the x and y co-ordinates, and the direction
                // create a new Drone object from these values, and add this drone to the drone arena
                } else if (string.equals("drone")) {
                    x = s.nextInt();
                    y = s.nextInt();
                    dir = s.next();

                    // convert the string to an object of type Direction
                    while (!direction.toString().equals(dir)) {
                        direction = direction.getNextDirection();
                    }

                    d = new Drone(x, y, direction);
                    a.addDrone(d);
                }
            }

            // replace the current drone arena with the new one
            this.myArena = a;

        } catch (Exception e) {

            // display an error if there was a problem opening the file
            System.out.println("An error has occurred opening the file.");
        }
    }
}
