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

    public void start() {
        System.out.println("=== Welcome to Solo Adventure Maze ===");

        prepareSaveFolder();  // 🔸 run1 폴더 생성 + 파일 복사

        hero = new Hero(1, 1);  // 위치는 room에서 추출
        room = new Room(saveDir + "room1.csv");  // 🔸 복사본 로딩
        hero = new Hero(room.getHeroStartX(), room.getHeroStartY());

        gameLoop();
    }

    private void prepareSaveFolder() {
        try {
            Files.createDirectories(Paths.get(saveDir));
            Files.copy(Paths.get("rooms/room1.csv"), Paths.get(saveDir + "room1.csv"), StandardCopyOption.REPLACE_EXISTING);
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

            room.checkInteractions(hero); // 전투, 아이템, 문 등
        }

        System.out.println("Game Over.");
    }
}
