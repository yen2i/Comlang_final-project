package game;

public class Monster extends Entity {
    private final String name;
    private final int damage;
    private final boolean dropsKey;

    public Monster(String name, int x, int y, int hp, int damage, boolean dropsKey) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.damage = damage;
        this.dropsKey = dropsKey;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        System.out.println("You attacked " + name + " for " + amount + " damage!");
        if (hp <= 0) System.out.println(name + " is defeated.");
    }

    public int getDamage() {
        return damage;
    }

    public boolean dropsKey() {
        return dropsKey;
    }

    public String getName() {
        return name;
    }
}
