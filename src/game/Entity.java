package game;

/*
 * Base class for all entities on the map (e.g. Hero, Monster).
 */
public abstract class Entity {
    protected int x, y;     // Position on the grid
    protected int hp;   // Health points

    public int getX() { return x; }  // Returns the x-position
    public int getY() { return y; }  // Returns the y-position
    public int getHp() { return hp; }  // Returns current HP

    // Abstract method for taking damage (implemented by subclasses)
    public abstract void takeDamage(int amount);
}
