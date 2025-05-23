package game;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Game {
    private Room room;
    private Hero hero;
    private Scanner scanner = new Scanner(System.in);
    private final String runId = "run1";
    private final String saveDir = "saves/" + runId + "/";
    private int currentRoomNum = 1;

    public void start() {
        System.out.println("=== Welcome to Solo Adventure Maze ===");

        prepareSaveFolder();

        room = new Room(saveDir + "room1.csv");
        hero = new Hero(room.getHeroStartX(), room.getHeroStartY());

        gameLoop();
    }

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

    private void gameLoop() {
        while (true) {
            room.display(hero);
            System.out.println("HP: " + hero.getHp() + " | Weapon: " + hero.getWeaponName() + " | Key: " + (hero.hasKey() ? "YES" : "NO"));
            System.out.print("Enter command (u/d/l/r/q): ");
            String cmd = scanner.nextLine().toLowerCase();

            if (cmd.equals("q")) break;

            switch (cmd) {
                case "u": hero.move(-1, 0, room); break;
                case "d": hero.move(1, 0, room); break;
                case "l": hero.move(0, -1, room); break;
                case "r": hero.move(0, 1, room); break;
                default: System.out.println("Invalid input.");
            }

            boolean movedToNextRoom = room.checkInteractions(hero, saveDir + "room" + currentRoomNum + ".csv");

            if (movedToNextRoom) {
                currentRoomNum++;
                if (currentRoomNum > 4) {
                    System.out.println("🎉 You escaped the maze! Game complete!");
                    break;
                }
                room = new Room(saveDir + "room" + currentRoomNum + ".csv");
                hero.setPosition(room.getHeroStartX(), room.getHeroStartY());
            }
        }

        System.out.println("Game Over.");
    }
}
