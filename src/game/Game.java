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

    private ArrayList<Room> roomList = new ArrayList<>();  // ✅ ArrayList로 대체

    public void start() {
        System.out.println("=== Welcome to Solo Adventure Maze ===");
        prepareSaveFolder();       // 방 파일 복사
        preloadRooms();            // ✅ 방 미리 ArrayList에 로드

        room = roomList.get(0);    // room1부터 시작
        hero = new Hero(room.getHeroStartX(), room.getHeroStartY());
        hero.setRoom(room);

        gameLoop();  // 게임 시작
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

    // ✅ ArrayList에 방 1~4 미리 넣기
    private void preloadRooms() {
        for (int i = 1; i <= 4; i++) {
            roomList.add(new Room(saveDir + "room" + i + ".csv"));
        }
    }

    private void gameLoop() {
        while (true) {
            room.display(hero);
            System.out.println("HP: " + hero.getHp() + "/25" + " | Weapon: " + hero.getWeaponName() + " | Key: " + (hero.hasKey() ? "YES" : "NO"));
            System.out.print("Enter command (u/d/r/l to move, a to attack, q to quit): ");
            String cmd = scanner.nextLine().toLowerCase();

            if (cmd.equals("q")) break;

            switch (cmd) {
                case "u": hero.move(-1, 0, room); break;
                case "d": hero.move(1, 0, room); break;
                case "l": hero.move(0, -1, room); break;
                case "r": hero.move(0, 1, room); break;
                case "a": room.attackAdjacentMonster(hero); break;
                default: System.out.println("Invalid input.");
            }

            if (hero.isDead()) {
                System.out.println("Your HP dropped to 0. You have died.");
                break;
            }

            String moveResult = room.checkInteractions(hero, saveDir + "room" + currentRoomNum + ".csv");

            if (moveResult.equals("NEXT")) {
                room.saveToCSV(saveDir + "room" + currentRoomNum + ".csv");

                for (Door d : room.getDoors()) {
                    if (hero.getX() == d.getX() && hero.getY() == d.getY()) {
                        String destination = d.getDestinationPath();

                        if (destination.equals("END")) {
                            System.out.println("You escaped the maze! Game complete!");
                            return;
                        }

                        // ✅ 방 번호 갱신
                        if (destination.contains("room1")) currentRoomNum = 1;
                        else if (destination.contains("room2")) currentRoomNum = 2;
                        else if (destination.contains("room3")) currentRoomNum = 3;
                        else if (destination.contains("room4")) currentRoomNum = 4;

                        // ✅ roomCache 대신 ArrayList 사용
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
