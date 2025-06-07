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
    private String filename;  // Save path of the current room

     // Hero spawn position
    private int heroStartX = -1;
    private int heroStartY = -1;


    // Constructor: loads room data from CSV file
    public Room(String filename) {
        this.filename = filename;          
        loadFromCSV(filename);
        findHeroStartPosition();  
    }

    // Sets the character at the specified position (x, y) in the room grid.
    public void setCell(int x, int y, char value) {
        grid[x][y] = value;
    }

    // Returns the character at a specific (x, y) location on the map grid
    public char getCell(int x, int y) {
        return grid[x][y];
    }

    public List<Door> getDoors() {
        return doors;
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
                    String cellData = line[j].trim();

                    if (cellData.startsWith("d:") || cellData.startsWith("D")) {
                        char doorType = cellData.charAt(0); // 'd' or 'D'
                        grid[i][j] = doorType;
                    
                        String path = "";
                        if (cellData.contains(":")) {
                            path = cellData.substring(2);
                        } else if (doorType == 'D') {
                            path = "END";  // Automatic END processing if there is only D
                        } else {
                            System.out.println("Warning: door without destination at (" + i + "," + j + ")");
                            continue;
                        }

                        String fullPath = path.equalsIgnoreCase("END") ? "END" : "saves/run1/" + path;
                    
                        doors.add(new Door(i, j, fullPath, doorType));
                        continue; // Already processed
                    }

                    // 2. Process in plain characters
                    char c = cellData.isEmpty() ? ' ' : cellData.charAt(0);
                    grid[i][j] = c;

                    switch (c) {
                        case '@':
                            heroStartX = i;
                            heroStartY = j;
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
    public String checkInteractions(Hero hero, String savePath) {
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
        
        // If hero steps on a key (★), pick it up
        if (getCell(hero.getX(), hero.getY()) == '★') {
            System.out.println("You picked up a key!");
            setCell(hero.getX(), hero.getY(), ' '); 
            hero.obtainKey();
        }

       // Handle door interaction
        for (Door d : doors) {
            if (hero.getX() == d.getX() && hero.getY() == d.getY()) {

                // Master Door 
                if (d.isMasterDoor()) {
                    if (hero.hasKey()) {
                        System.out.println("You used the key to open the Master Door.");
                        System.out.println("You have escaped the dungeon. Congratulations!");
                        return "END";
                    } else {
                        System.out.println("The Master Door is locked. You need a key to escape.");
                        return "NONE";
                    }
                }

                // Regular Door
                else {
                    System.out.println("The door opens freely.");
                    saveToCSV(savePath);
                    return "NEXT";
                }
            }
        }
        return "NONE";
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
                            System.out.println("The " + m.getName() + " dropped a key!");
                            setCell(m.getX(), m.getY(), '★');   // If the monster is a Troll, drop a key (★) at its position
                        } else {
                            setCell(m.getX(), m.getY(), ' ');  // Remove monster symbol from grid
                        }
                        mi.remove();
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
                    boolean isDoorCell = false;
                    for (Door door : doors) {
                                if (door.getX() == i && door.getY() == j) {
                                    String dest = door.getDestinationPath().replace("saves/run1/", "");
                                    bw.write(door.getType() + ":" + dest);
                                    isDoorCell = true;
                                    break;
                                }
                            }

                            if (!isDoorCell) {
                                bw.write(grid[i][j]);
                            }

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

    // Sets the hero's starting position based on rules
    private void findHeroStartPosition() {
        // 1. Look for '@'
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == ' ') {
                    heroStartX = i;
                    heroStartY = j;
                    grid[i][j] = ' '; // Clear the '@' from the map
                    return;
                }
            }
        }

        // 2. If no '@', try (1,1)
        if (grid[1][1] == ' ') {
            heroStartX = 1;
            heroStartY = 1;
            return;
        }

        // 3. Else, pick random empty space
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            Random rand = new Random(System.nanoTime()); // Stronger randomness
            int[] pos = emptyCells.get(rand.nextInt(emptyCells.size()));
            heroStartX = pos[0];
            heroStartY = pos[1];
        } else {
            // Fallback: should never happen
            heroStartX = 1;
            heroStartY = 1;
        }
    }
    
    // Checks if the hero is next to a monster (Manhattan distance = 1)
    private boolean isAdjacent(Hero h, Monster m) {
        int dx = Math.abs(h.getX() - m.getX());
        int dy = Math.abs(h.getY() - m.getY());
        return (dx <= 1 && dy <= 1 && (dx + dy > 0));
    }

    // Getter for hero spawn X position
    public int getHeroStartX() {
        return heroStartX;
    }

    // Getter for hero spawn Y position
    public int getHeroStartY() {
        return heroStartY;
    }

    public List<Item> getItems() {
    return items;
    }

    public String getFilename() {
    return filename;
    }
}
