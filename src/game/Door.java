package game;

/*
 * Represents a door on the map that leads to another room.
 */
public class Door {
    private final int x; // X-coordinate of the door on the grid
    private final int y;  // Y-coordinate of the door on the grid
    private final String destinationPath; // Path to the next room's CSV file

    // Constructor: initializes position and destination path
    public Door(int x, int y, String destinationPath) {
        this.x = x;
        this.y = y;
        this.destinationPath = destinationPath;
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
}
