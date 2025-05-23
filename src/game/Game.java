package game;

import java.util.*;

public class Game {
    private Room room;
    private Hero hero;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("=== Welcome to Solo Adventure Maze ===");

        room = new Room("rooms/room1.csv");
        hero = new Hero(room.getHeroStartX(), room.getHeroStartY());

        gameLoop();
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
    }
}
