package game;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/*
 * Main game logic controller.
 */
public class Game {
    private Room room;  // Current room instance
    private Hero hero;  // Current room instance
    private Scanner scanner = new Scanner(System.in);  // For user input
    private final String runId = "run1";  // Unique run session name
    private final String saveDir = "saves/" + runId + "/";  // Save file directory
    private int currentRoomNum = 1;  // Current room number
    private ArrayList<Room> roomList = new ArrayList<>();  

    // Current room number
    public void start() {
        System.out.println("=== Welcome to Solo Adventure Maze ===");
        prepareSaveFolder();    // Copy initial room files into save directory
        preloadRooms();            

        room = roomList.get(0);   
        hero = new Hero(room.getHeroStartX(), room.getHeroStartY());
        hero.setRoom(room); //connect 

        gameLoop();   // Start main game loop
    }

    // Start main game loop
    private void prepareSaveFolder() {
        try {
            Files.createDirectories(Paths.get(saveDir));
            for (int i = 1; i <= 4; i++) {
                String src = "rooms/room" + i + ".csv";
                String dst = saveDir + "room" + i + ".csv";
                Files.copy(Paths.get(src), Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.out.println("Failed to prepare save folder: " + e.getMessage());
        }
    }

    // Main game loop: displays map, handles input, interactions, and room transitions
    private void preloadRooms() {
        for (int i = 1; i <= 4; i++) {
            roomList.add(new Room(saveDir + "room" + i + ".csv"));
        }
    }

    private void gameLoop() {
        while (true) {
            room.display(hero);  // Show current room and hero
            System.out.println("HP: " + hero.getHp() + "/25" + " | Weapon: " + hero.getWeaponName() + " | Key: " + (hero.hasKey() ? "YES" : "NO"));
            System.out.print("Enter command (u/d/r/l to move, a to attack, q to quit): ");
            String cmd = scanner.nextLine().toLowerCase();

            if (cmd.equals("q")) break;

            // Show current room and hero
            switch (cmd) {
                case "u": hero.move(-1, 0, room); break;
                case "d": hero.move(1, 0, room); break;
                case "l": hero.move(0, -1, room); break;
                case "r": hero.move(0, 1, room); break;
                case "a": room.attackAdjacentMonster(hero); break;
                default: System.out.println("Invalid input.");
            }

            // If hero is dead after action, end the game
            if (hero.isDead()) {
                System.out.println("Your HP dropped to 0. You have died.");
                break;
            }

            // Show current room and hero
            String moveResult = room.checkInteractions(hero, saveDir + "room" + currentRoomNum + ".csv");

            if (moveResult.equals("NEXT")) {
                // Save the current room
                room.saveToCSV(saveDir + "room" + currentRoomNum + ".csv");

                // Find which door is currently located and determine the path to move
                for (Door d : room.getDoors()) {
                    if (hero.getX() == d.getX() && hero.getY() == d.getY()) {
                        String destination = d.getDestinationPath();

                        if (destination.equals("END")) {
                            System.out.println("You escaped the maze! Game complete!");
                            return;
                        }

                        // Update currentRoomNum
                        if (destination.contains("room1")) currentRoomNum = 1;
                        else if (destination.contains("room2")) currentRoomNum = 2;
                        else if (destination.contains("room3")) currentRoomNum = 3;
                        else if (destination.contains("room4")) currentRoomNum = 4;

                        // Reuse Room objects using cache
                        room = roomList.get(currentRoomNum - 1);
                        hero.setPosition(room.getHeroStartX(), room.getHeroStartY());
                        hero.setRoom(room);
                        break;
                    }
                }
            } else if (moveResult.equals("END")) {
                System.out.println("You escaped the maze! Game complete!");
                return;
            }
        }
    }
}
