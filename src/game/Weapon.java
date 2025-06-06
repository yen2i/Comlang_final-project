package game;

/*
 * Represents a weapon that can be equipped by the hero.
 */
public class Weapon implements Item {
    private final String name;  // Name of the weapon
    private final int damage;  // Damage dealt when used
    private final char symbol;  // Symbol shown on the map

    // Constructor: sets name, damage, and map symbol
    public Weapon(String name, int damage, char symbol) {
        this.name = name;
        this.damage = damage;
        this.symbol = symbol;
    }

    // Equips this weapon to the hero
    public void use(Hero hero) {
        Weapon old = hero.getWeapon();
        int x = hero.getX();
        int y = hero.getY();
        Room room = hero.getRoom();

        if (old != null) {
            // If already armed, drop old weapon on current tile
            room.setCell(x, y, old.getSymbol());
            room.getItems().add(old);  // Add back to item list!
        } else {
            // If no previous weapon, clear the symbol
            room.setCell(x, y, ' ');
        }

        System.out.println("You picked up the " + name + ".");
        hero.setWeapon(this);
    }

    // Returns the weapon's damage value
    public int getDamage() {
        return damage;
    }

    // Returns the weapon's name
    public String getName() {
        return name;
    }

    // Returns the symbol representing this weapon on the map
    public char getSymbol() {
        return symbol;
    }
}
