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

    // Current room number
    public void start() {
        System.out.println("=== Welcome to Solo Adventure Maze ===");

        prepareSaveFolder();  // Copy initial room files into save directory

        room = new Room(saveDir + "room1.csv");  // Copy initial room files into save directory
        hero = new Hero(room.getHeroStartX(), room.getHeroStartY());  // Place hero
        hero.setRoom(room); //connect 

        gameLoop();  // Start main game loop
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

            // Show current room and hero
            boolean movedToNextRoom = room.checkInteractions(hero, saveDir + "room" + currentRoomNum + ".csv");

            // If moved to next room, load the new room
            if (movedToNextRoom) {
                currentRoomNum++;
                if (currentRoomNum > 4) {
                    System.out.println("You escaped the maze! Game complete!");
                    break;
                }
                room = new Room(saveDir + "room" + currentRoomNum + ".csv");
                hero.setPosition(room.getHeroStartX(), room.getHeroStartY());
            }
        }

        System.out.println("Game Over.");
    }
}
