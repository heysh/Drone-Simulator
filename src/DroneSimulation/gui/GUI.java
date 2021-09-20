package DroneSimulation.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class that is responsible for handling the GUI aspect of the Drone Simulator.
 * @author Harshil Surendralal
 */
public class GUI extends Application {

    private Stage window;
    private AnimationTimer timer;
    private MyCanvas mc;
    private VBox rtPane;
    private DroneArena arena;
    JFileChooser chooser = new JFileChooser();

    /**
     * Display a message to the user through an alert.
     * @param title The title of the alert.
     * @param content The message of the alert.
     */
    private void showMessage(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Display the About message to the user.
     */
    private void showAbout() {
        showMessage("About", "Harshil's Drone Simulator");
    }

    /**
     * Display the Help message to the user.
     */
    private void showHelp() {
        showMessage("Help", "Simulate the behaviour between drones and hunter drones.\n" +
                "Click anywhere on the canvas to place an obstacle in that location.");
    }

    /**
     * Save the contents of the drone arena to a file.
     */
    private void saveFile() {
        // allows the dialog to show in the foreground
        JFrame jf = new JFrame();
        jf.setAlwaysOnTop(true);

        // show the 'save file' dialog to the user and capture their response
        int response = chooser.showSaveDialog(jf);

        // the JFrame is no longer needed, so cleanly delete it
        jf.dispose();

        // if the user successfully saves the file, get the path of where the file has been saved
        if (response == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();

            // try to write the contents of the drone arena into the file
            try {
                PrintWriter file = new PrintWriter(new File(filePath));
                file.write(arena.saveString());
                file.close();

                showMessage("Success!", "File has been saved successfully.");

                // if the file could not be found, display an error to the user
            } catch (FileNotFoundException e) {
                showMessage("Error!", "File was not saved.");
            }
        }
    }

    /**
     * @return String containing the path of the drone arena file the user wishes to load.
     */
    public String getFilePathLoad() {
        // allows the dialog to show in the foreground
        JFrame jf = new JFrame();
        jf.setAlwaysOnTop(true);

        // show the 'open file' dialog to the user and capture their response
        int response = chooser.showOpenDialog(jf);

        // the JFrame is no longer needed, so cleanly delete it
        jf.dispose();

        // if the user successfully opens the file, get the path of where the file has been saved
        if (response == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        } else return null;
    }

    /**
     * Load a drone arena from a file.
     */
    private void loadFile() throws IOException {
        String filePath = getFilePathLoad();

        // if the user successfully opens the file
        if (filePath != null) {

            // try to open the file
            try {
                Scanner s = new Scanner(new File(filePath));

                // if the file could be opened successfully, clear the canvas and the drone arena in preparation for the
                // new drone arena
                arena.clear();
                mc.clearCanvas();

                String string;
                char entity;
                Double x, y, radius, angle, speed;

                // while there are still characters left in the file
                while(s.hasNext()) {
                    string = s.next();

                    // if the first character is an A, get the drone arena dimensions and load an arena
                    if (string.equals("A")) {
                        x = s.nextDouble();
                        y = s.nextDouble();

                        // set the dimensions of the canvas to the drone arena dimensions
                        mc.canvasX = (int)Math.round(x);
                        mc.canvasY = (int)Math.round(y);

                        arena.loadArenaFromFile(x, y);
                    }

                    // if the first character is a D or an H, get all of the information about the drone or hunter drone
                    // and load it into the drone arena
                    if (string.equals("D") || string.equals("H")) {
                        entity = string.charAt(0);
                        x = s.nextDouble();
                        y = s.nextDouble();
                        radius = s.nextDouble();
                        angle = s.nextDouble();
                        speed = s.nextDouble();
                        arena.loadDroneFromFile(entity, x, y, radius, angle, speed);
                    }

                    // if the first character is an O, get the location and the radius of the obstacle and load it into
                    // the drone arena
                    if (string.equals("O")) {
                        x = s.nextDouble();
                        y = s.nextDouble();
                        radius = s.nextDouble();
                        arena.loadObstacleFromFile(x, y, radius);
                    }

                    // update the canvas
                    drawWorld();
                    drawStatus();
                }

                s.close();

                // if the file could not be found, display an error to the user
            } catch (FileNotFoundException e) {
                showMessage("Error!", "File could not be opened.");
            }
        }
    }

    /**
     * Confirms whether the user would like to exit the program.
     */
    private void exitProgram() {
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
        if (answer == JOptionPane.YES_OPTION) window.close();
    }

    /**
     * @return MenuBar which allows the user to save or load arenas, exit the program, or get help.
     */
    private MenuBar setMenu() {
        MenuBar menuBar = new MenuBar();

        // create a Menu called File
        Menu mFile = new Menu("File");

        // create a MenuItem called Save that will prompt the user to save the arena
        MenuItem mSave = new MenuItem("Save");
        mSave.setOnAction(e -> {
            timer.stop();
            saveFile();
        });

        // create a MenuItem called Load that will prompt the user to load an arena
        MenuItem mLoad = new MenuItem("Load");
        mLoad.setOnAction(e -> {
            try {
                timer.stop();
                loadFile();
            } catch (IOException ex) {
                showMessage("Error", "Cannot load file.");
            }
        });

        // create a MenuItem called Exit that will ask the user for confirmation before exiting the program
        MenuItem mExit = new MenuItem("Exit");
        mExit.setOnAction(e -> exitProgram());

        // add these MenuItems to the File menu
        mFile.getItems().addAll(mSave, mLoad, mExit);

        // create a Menu called Help
        Menu mHelp = new Menu("Help");

        // create a MenuItem called About that will provide information about the program to the user
        MenuItem mAbout = new MenuItem("About");
        mAbout.setOnAction(e -> {
            timer.stop();
            showAbout();
            timer.start();
        });

        // create a MenuItem called Help that will provide information about how to use the program to the user
        MenuItem help = new MenuItem("Help");
        help.setOnAction(e -> {
            timer.stop();
            showHelp();
            timer.start();
        });

        // add these MenuItems to the Help menu
        mHelp.getItems().addAll(mAbout, help);

        // add the Menus to the menu bar
        menuBar.getMenus().addAll(mFile, mHelp);

        return menuBar;
    }

    /**
     * @return HBox containing buttons the user can press to control the drone arena.
     */
    private HBox setButtons() {
        HBox buttons = new HBox();

        // create a Label for buttons responsible for configuring the options of the drone arena
        Label options = new Label("   Options");

        // create a Button to start the animation
        Button btnAnimOn = new Button("Start");
        btnAnimOn.setOnAction(e -> timer.start());

        // create a Button to stop the animation
        Button btnAnimOff = new Button("Stop");
        btnAnimOff.setOnAction(e -> timer.stop());

        // create a Button to clear the drone arena
        Button btnClear = new Button("Clear"); // clear button which clears the canvas
        btnClear.setOnAction(e -> {
            arena.clear();
            mc.clearCanvas();
            drawWorld();
            drawStatus();
        });

        // create a Label for buttons responsible for adding objects into the drone arena
        Label add = new Label("Add");

        // create a Button to add a drone to the drone arena
        Button btnAddDrone = new Button("Drone");
        btnAddDrone.setOnAction(e -> {
            arena.addDrone();
            drawWorld();
            drawStatus();
        });

        // create a Button to add a Hunter Drone to the drone arena
        Button btnAddHunterDrone = new Button("Hunter Drone"); // add random hunter drone button
        btnAddHunterDrone.setOnAction(e -> {
            arena.addHunterDrone();
            drawWorld();
            drawStatus();
        });

        // add the labels and buttons to the HBox and space them
        buttons.getChildren().addAll(options, btnAnimOn, btnAnimOff, btnClear, add, btnAddDrone, btnAddHunterDrone);
        buttons.setSpacing(8);

        return buttons;
    }

    /**
     * Draw all objects in the drone arena on the canvas.
     */
    public void drawWorld () {
        mc.clearCanvas();
        arena.drawArena(mc);
    }

    /**
     * Draw the status of the drone arena and all objects within on the right of the canvas.
     */
    public void drawStatus() {
        rtPane.getChildren().clear();
        Label info = new Label(arena.toString());
        rtPane.getChildren().add(info);
    }

    /**
     * Adds an obstacle in the drone arena in the location the user clicked their mouse.
     * @param canvas The canvas to which the mouse event handler is added.
     */
    private void handleMouseEvents(Canvas canvas) {
        // if the mouse is clicked, add an obstacle in the location it was clicked
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                e -> {
                    arena.addObstacle(e.getX(), e.getY());
                    drawWorld();
                    drawStatus();
                });
    }

    /**
     * Main driver function of the Drone Simulation program. Creates and handles a canvas on which different objects are
     * drawn, alongside their status' and buttons which allow the user to control these objects.
     * @param primaryStage The window on which the Drone Simulator is displayed.
     * @throws Exception Throw any exception that may occur.
     */
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Harshil Surendralal's Drone Simulation (28000259)");

        // create a border pane and add a menu at the top
        BorderPane bp = new BorderPane();
        bp.setTop(setMenu());

        // create a canvas in which we can draw, and add this canvas on the left-hand side of the border pane
        Group root = new Group();
        Canvas canvas = new Canvas( 600, 600 );
        root.getChildren().add(canvas);
        bp.setLeft(root);

        // create an object of type MyCanvas, on which the drone arena will be drawn
        mc = new MyCanvas(canvas.getGraphicsContext2D(),600, 600);

        // add a mouse event handler to the canvas
        handleMouseEvents(canvas);

        // create a new drone arena that shares dimensions with the canvas on which it is to be drawn
        arena = new DroneArena(mc.getCanvasX(), mc.getCanvasY());

        // add three drones into the arena
        arena.addDrone();
        arena.addDrone();
        arena.addDrone();
        drawWorld();

        // as long as the timer is running, update the canvas with the new positions and statuses
        timer = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                arena.checkObjects();
                arena.adjustObjects();
                drawWorld();
                drawStatus();
            }
        };

        // create a VBox and add it on the right-hand side of the border pane
        rtPane = new VBox();
        bp.setRight(rtPane);

        // add buttons at the bottom of the border pane
        bp.setBottom(setButtons());

        // create a scene using the border pane and adjust the colour of the background
        Scene scene = new Scene(bp, mc.getCanvasX() * 2, mc.getCanvasY() * 1.2);
        bp.setStyle("-fx-background-color: #DFCAAE");

        window.setScene(scene);
        window.show();
    }
}

