package game;

/*
 * Represents a door on the map that leads to another room.
 */
public class Door {
    private final int x; // X-coordinate of the door on the grid
    private final int y;  // Y-coordinate of the door on the grid
    private final String destinationPath; // Path to the next room's CSV file
    private final char type;  // 'd' for normal door, 'D' for master door

    // Constructor: initializes position and destination path
    public Door(int x, int y, String destinationPath, char type) {
        this.x = x;
        this.y = y;
        this.destinationPath = destinationPath;
        this.type = type;
    }

    // Returns the x-position of the door   
    public int getX() {
        return x;
    }

    // Returns the y-position of the door
    public int getY() {
        return y;
    }

    /*
     * Gets the file path to the room this door leads to.
     */
    public String getDestinationPath() {
        return destinationPath;
    }

    // Returns the door type ('d' or 'D')
    public char getType() {
        return type;
    }

    // Returns true if this is a Master Door (D), requires key
    public boolean isMasterDoor() {
        return type == 'D';
    }
}
