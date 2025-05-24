package game;

/*
 * Represents a monster that the hero can fight.
 */
public class Monster extends Entity {
    private final String name;  // Monster's name
    private final int damage;  // Damage dealt to the hero
    private final boolean dropsKey;  // Whether this monster drops a key when defeated

    // Constructor: sets name, position, HP, damage, and key drop status
    public Monster(String name, int x, int y, int hp, int damage, boolean dropsKey) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.damage = damage;
        this.dropsKey = dropsKey;
    }

    // Reduces monster HP and prints attack result
    public void takeDamage(int amount) {
        hp -= amount;
        System.out.println("You attacked " + name + " for " + amount + " damage!");
        if (hp <= 0) System.out.println(name + " is defeated.");
    }

    // Returns the monster's attack damage
    public int getDamage() {
        return damage;
    }

    // Returns whether the monster drops a key
    public boolean dropsKey() {
        return dropsKey;
    }

    // Returns the monster's name
    public String getName() {
        return name;
    }
}
