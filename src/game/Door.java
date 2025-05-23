package game;

public class Door {
    private final int x;
    private final int y;
    private final String destinationPath;

    public Door(int x, int y, String destinationPath) {
        this.x = x;
        this.y = y;
        this.destinationPath = destinationPath;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDestinationPath() {
        return destinationPath;
    }
}
