package game;

/*
 * Represents the player-controlled hero.
 */
public class Hero extends Entity {
    private boolean hasKey;  // Whether the hero has the key
    private Weapon weapon;  // Equipped weapon
    private Room currentRoom;  // Reference to the room the hero is currently in

    // Constructor: initializes hero's position, HP, and inventory
    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.hp = 25;
        this.hasKey = false;
        this.weapon = null;
    }

    // Moves the hero by (dx, dy) if the destination is walkable
    public void move(int dx, int dy, Room room) {
        int newX = x + dx;
        int newY = y + dy;
        if (room.isWalkable(newX, newY)) {
            //If there's an item on the current tile, restore its symbol
            Item item = room.getItemAt(x, y);
            if (item != null) {
                room.setCell(x, y, item.getSymbol());
            } else {
                room.setCell(x, y, ' ');
            }
            x = newX;
            y = newY;
        } else {
            System.out.println("You can't move there.");
        }
    }

    // Sets hero's position directly (used when entering new room)
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Attacks the given monster if the hero has a weapon
    public void attack(Monster monster) {
        if (weapon == null) {
            System.out.println("You need a weapon to attack!");
            return;
        }
        monster.takeDamage(weapon.getDamage());
        this.takeDamage(monster.getDamage());
    }

    // Picks up and uses an item (weapon or potion)
    public void pickUp(Item item) {
        item.use(this);
    }

    // Reduces HP and checks for death
    public void takeDamage(int amount) {
        hp -= amount;
        System.out.println("You took " + amount + " damage.");
        if (hp <= 0) System.out.println("You died.");
    }

    // Heals HP, up to max 25
    public void heal(int amount) {
        hp = Math.min(25, hp + amount);
        System.out.println("Healed to " + hp + " HP.");
    }

    // Equips a weapon
    public void setWeapon(Weapon w) {
        this.weapon = w;
    }

    //Returns picked weapons
    public Weapon getWeapon() {
        return this.weapon;
    }

    // Returns the name and damage of the current weapon (or None)
    public String getWeaponName() {
        return (weapon == null) ? "None" : weapon.getName() + "(" + weapon.getDamage() + ")";
    }
    
    // Sets the current room the hero is in
    public void setRoom(Room room) {
        this.currentRoom = room;
    }
    
    // Returns the current room the hero is in
    public Room getRoom() {
        return this.currentRoom;
    }

    // Returns the hero's damage value
    public int getDamage() {
        return (weapon == null) ? 0 : weapon.getDamage();
    }

    // Key-related methods
    public boolean hasKey() { return hasKey; }
    public void obtainKey() { hasKey = true; }
}
