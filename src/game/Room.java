package game;

import java.util.*;
import java.io.*;

public class Room {
    private char[][] grid;
    private int rows, cols;
    private List<Monster> monsters = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<Door> doors = new ArrayList<>();

    private int startX = 1, startY = 1;

    public Room(String filename) {
        loadFromCSV(filename);
    }

    public void loadFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] size = br.readLine().split(",");
            rows = Integer.parseInt(size[0]);
            cols = Integer.parseInt(size[1]);
            grid = new char[rows][cols];

            for (int i = 0; i < rows; i++) {
                String[] line = br.readLine().split(",");
                for (int j = 0; j < cols; j++) {
                    char c = line[j].charAt(0);
                    grid[i][j] = c;

                    switch (c) {
                        case '@':
                            startX = i;
                            startY = j;
                            grid[i][j] = ' ';
                            break;
                        case 'G':
                            monsters.add(new Monster("Goblin", i, j, 3, 1, false));
                            break;
                        case 'O':
                            monsters.add(new Monster("Orc", i, j, 8, 3, false));
                            break;
                        case 'T':
                            monsters.add(new Monster("Troll", i, j, 15, 4, true));
                            break;
                        case 'S':
                            items.add(new Weapon("Stick", 1, 'S'));
                            break;
                        case 'W':
                            items.add(new Weapon("Weak Sword", 2, 'W'));
                            break;
                        case 'X':
                            items.add(new Weapon("Strong Sword", 3, 'X'));
                            break;
                        case 'm':
                            items.add(new Potion(6, 'm'));
                            break;
                        case 'B':
                            items.add(new Potion(12, 'B'));
                            break;
                        case 'D':
                            // 현재 파일 이름으로부터 다음 방 번호 계산
                            String currentRoomPath = filename;
                            String roomNumStr = currentRoomPath.replaceAll("[^0-9]", "");
                            int nextRoomNum = Integer.parseInt(roomNumStr) + 1;
                        
                            String nextRoomPath = "saves/run1/room" + nextRoomNum + ".csv";
                        
                            if (nextRoomNum <= 4) {
                                doors.add(new Door(i, j, nextRoomPath));
                            } else {
                                doors.add(new Door(i, j, "END")); // 마지막 방이면 종료 메시지
                            }
                            break;
                        
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading room file: " + filename);
        }
    }

    public void display(Hero hero) {
        System.out.println("+-----------+");
        for (int i = 0; i < rows; i++) {
            System.out.print("| ");
            for (int j = 0; j < cols; j++) {
                if (i == hero.getX() && j == hero.getY()) {
                    System.out.print("@ ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("+-----------+");
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) return false;
        char cell = grid[x][y];
    
        // 이동 가능 대상은 벽/경계/장애물이 아닌 모든 칸
        return !(cell == '#' || cell == '|');  // 나머지는 다 이동 허용
    }

    public boolean checkInteractions(Hero hero, String savePath) {
        Scanner s = new Scanner(System.in);
        Iterator<Monster> mi = monsters.iterator();

        Iterator<Item> ii = items.iterator();
        while (ii.hasNext()) {
            Item item = ii.next();
            if (item instanceof Weapon && grid[hero.getX()][hero.getY()] == ((Weapon) item).getSymbol()) {
                System.out.print("Switch to " + ((Weapon) item).getName() + "(" + ((Weapon) item).getDamage() + ")" + "? (y/n): ");
                if (s.nextLine().equalsIgnoreCase("y")) {
                    hero.pickUp(item);
                    ii.remove();
                }
            } else if (item instanceof Potion && grid[hero.getX()][hero.getY()] == item.getSymbol()) {
                hero.pickUp(item);
                ii.remove();
            }
        }

        for (Door d : doors) {
            if (hero.getX() == d.getX() && hero.getY() == d.getY()) {
                String[] parts = savePath.split("room");
                int currentRoomNum = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
        
                // room1, room2 문은 key 없이도 열리게!
                if (currentRoomNum <= 2 || hero.hasKey()) {
                    System.out.println(hero.hasKey() ? "You used the key to open the door." : "The door opens freely.");
                    saveToCSV(savePath);
                    System.out.println("Loading next room...");
                    return true;
                } else {
                    System.out.println("The door is locked. You need a key.");
                }
            }
        }
        return false;
    }

    public void attackAdjacentMonster(Hero hero) {
        Iterator<Monster> mi = monsters.iterator();
        while (mi.hasNext()) {
            Monster m = mi.next();
            if (isAdjacent(hero, m)) {
                System.out.println("You attack the " + m.getName() + " (HP: " + m.getHp() + ")");
                hero.attack(m);
                if (m.getHp() <= 0) {
                    if (m.dropsKey()) {
                        System.out.println("The Troll dropped a key!");
                        hero.obtainKey();
                    }
                    mi.remove();
                }
                return;  // 한 번만 공격
            }
        }
        System.out.println("No monster nearby to attack.");
    }
    
    public void saveToCSV(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(rows + "," + cols + "\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    bw.write(grid[i][j]);
                    if (j < cols - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving room: " + e.getMessage());
        }
    }
    
    private boolean isAdjacent(Hero h, Monster m) {
        int dx = Math.abs(h.getX() - m.getX());
        int dy = Math.abs(h.getY() - m.getY());
        return (dx + dy == 1);
    }

    public int getHeroStartX() {
        return startX;
    }

    public int getHeroStartY() {
        return startY;
    }
}
