package game;

public class Potion implements Item {
    private final int healAmount;
    private final char symbol;

    public Potion(int amount, char symbol) {
        this.healAmount = amount;
        this.symbol = symbol;
    }

    public void use(Hero hero) {
        if (hero.getHp() >= 25) {
            System.out.println("You're already at full health.");
        } else {
            System.out.println("You drink a potion.");
            hero.heal(healAmount);
        }
    }

    public char getSymbol() {
        return symbol;
    }
}
