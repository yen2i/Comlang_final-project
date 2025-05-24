package game;

/*
 * Interface for items that can be used by the hero (e.g. weapons, potions).
 */
public interface Item {
    void use(Hero hero);  // Applies the item's effect to the hero (equip weapon or heal HP)
    char getSymbol();  // Returns the symbol used to represent the item on the map
}
