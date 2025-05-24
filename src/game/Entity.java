package game;

public abstract class Entity {
    protected int x, y;
    protected int hp;

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }

    public abstract void takeDamage(int amount);
}
