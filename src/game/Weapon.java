package game;

public class Weapon implements Item {
    private final String name;
    private final int damage;
    private final char symbol;

    public Weapon(String name, int damage, char symbol) {
        this.name = name;
        this.damage = damage;
        this.symbol = symbol;
    }

    public void use(Hero hero) {
        System.out.println("Picked up: " + name);
        hero.setWeapon(this);
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }
}
