package game;

import java.io.*;
import java.util.*;

/*
 * Represents a single room in the game, including its grid, monsters, items, and doors.
 */
public class Room {
    private char[][] grid;  // 2D map layout
    private int rows, cols;  // Room dimensions
    private List<Monster> monsters = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<Door> doors = new ArrayList<>();  

    private int startX = 1, startY = 1;  // Hero spawn position

    // Constructor: loads room data from CSV file
    public Room(String filename) {
        loadFromCSV(filename);
    }

    // Sets the character at the specified position (x, y) in the room grid.
    public void setCell(int x, int y, char value) {
        grid[x][y] = value;
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
                            // Determine next room based on current room filename
                            String currentRoomPath = filename;
                            String roomNumStr = currentRoomPath.replaceAll("[^0-9]", "");
                            int nextRoomNum = Integer.parseInt(roomNumStr) + 1;
                        
                            String nextRoomPath = "saves/run1/room" + nextRoomNum + ".csv";
                        
                            if (nextRoomNum <= 4) {
                                doors.add(new Door(i, j, nextRoomPath));
                            } else {
                                doors.add(new Door(i, j, "END")); // End game if last room
                            }
                            break;
                        
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading room file: " + filename);
        }
    }

    // Displays the room grid with hero and symbols
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

    // Checks whether a cell at (x, y) is walkable
    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) return false;
        char cell = grid[x][y];
    
        // Returns true if the cell is not a wall or obstacle (i.e., walkable)
        return !(cell == '#' || cell == '|');  // Non-walkable: walls
    }

    // Handles item pickups and door interactions
    public boolean checkInteractions(Hero hero, String savePath) {
        Scanner s = new Scanner(System.in);

        // === Handle item pickup ===
        for (Item item : new ArrayList<>(items)) {
            int x = hero.getX();
            int y = hero.getY();

            // Weapon pickup and swap
            if (item instanceof Weapon && grid[x][y] == ((Weapon) item).getSymbol()) {
                System.out.print("Switch to " + ((Weapon) item).getName() + "(" + ((Weapon) item).getDamage() + ")" + "? (y/n): ");
                if (s.nextLine().equalsIgnoreCase("y")) {
                    hero.pickUp(item);      // This will handle dropping old weapon into grid + items
                    items.remove(item);     // Remove the newly picked-up weapon from the list
                }
            }

            // Potion pickup (only if hero is not full health)
            else if (item instanceof Potion && grid[x][y] == item.getSymbol()) {
                if (hero.getHp() < 25) {
                    hero.pickUp(item);
                    setCell(x, y, ' ');     // Clear potion symbol from grid
                    items.remove(item);     // Remove potion from item list
                } else {
                    System.out.println("You're already at full health.");
                }
            }
        }

        // Handle door interaction
        for (Door d : doors) {
            if (hero.getX() == d.getX() && hero.getY() == d.getY()) {
                String[] parts = savePath.split("room");
                int currentRoomNum = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
        
                // Allow doors in room1 and room2 to open without a key
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

    // Handles attack logic when hero is adjacent to a monster
    public void attackAdjacentMonster(Hero hero) {
        Iterator<Monster> mi = monsters.iterator();
        Scanner scanner = new Scanner(System.in);  

        while (mi.hasNext()) {
            Monster m = mi.next();
            if (isAdjacent(hero, m)) {
                System.out.println("You are next to a " + m.getName() + " (HP: " + m.getHp() + ")");
                System.out.print("Attack? (y/n): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("y")) {
                    hero.attack(m);

                    // If monster is defeated, remove from map and grid
                    if (m.getHp() <= 0) {
                        if (m.dropsKey()) {
                            System.out.println("The Troll dropped a key!");
                            hero.obtainKey();
                        }
                        mi.remove();
                        setCell(m.getX(), m.getY(), ' ');  // Remove monster symbol from grid
                    }
                } else {
                    System.out.println("You chose not to attack.");
                }
                return;  // Attack only one monster per turn
            }
        }
        System.out.println("No monster nearby to attack.");
    }
    
    // Saves current room grid state to CSV file
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
    
    // Checks if the hero is next to a monster (Manhattan distance = 1)
    private boolean isAdjacent(Hero h, Monster m) {
        int dx = Math.abs(h.getX() - m.getX());
        int dy = Math.abs(h.getY() - m.getY());
        return (dx + dy == 1);
    }

    // Getter for hero spawn X position
    public int getHeroStartX() {
        return startX;
    }

    // Getter for hero spawn Y position
    public int getHeroStartY() {
        return startY;
    }

    public List<Item> getItems() {
    return items;
    }
}
