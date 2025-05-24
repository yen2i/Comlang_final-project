package game;

/*
 * Represents a healing potion that restores the hero's HP.
 */
public class Potion implements Item {
    private final int healAmount;  // Amount of HP the potion restores
    private final char symbol;  // Symbol used to represent the potion on the map

    // Constructor: sets healing amount and map symbol
    public Potion(int amount, char symbol) {
        this.healAmount = amount;
        this.symbol = symbol;
    }

    // Uses the potion on the hero to restore HP
    public void use(Hero hero) {
        if (hero.getHp() >= 25) {
            System.out.println("You're already at full health.");
        } else {
            System.out.println("You drink a potion.");
            hero.heal(healAmount);
        }
    }

    // Returns the map symbol for this potion
    public char getSymbol() {
        return symbol;
    }
}
