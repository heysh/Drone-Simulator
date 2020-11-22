package DroneSimulation;

import java.util.Scanner;

/**
 *
 * @author Harshil Surendralal bf000259
 */
public class DroneInterface {

    private int arenaX, arenaY;
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
            System.out.print("Enter (C)reate new arena, (A)dd drone, get (I)nformation, (D)isplay drones, (M)ove all drones once, move all drones (T)en times, or e(X)it > ");
            ch = s.next().charAt(0);
            s.nextLine();

            switch (ch) {
                case 'C':
                case 'c':
                    System.out.print("Enter the width of the arena > ");
                    arenaX = s.nextInt();
                    System.out.print("Enter the height of the arena > ");
                    arenaY = s.nextInt();

                    myArena = new DroneArena(arenaX, arenaY);

                    break;
                case 'A':
                case 'a':
                    myArena.addDrone();
                    break;
                case 'I':
                case 'i':
                    System.out.print(myArena.toString());
                    break;
                case 'D':
                case 'd':
                    doDisplay();
                    break;
                case 'M':
                case 'm':
                    myArena.moveAllDrones();
                    doDisplay();
                    break;
                case 'T':
                case 't':
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
}
