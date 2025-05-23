package game;

public class Hero extends Entity {
    private boolean hasKey;
    private Weapon weapon;

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.hp = 25;
        this.hasKey = false;
        this.weapon = null;
    }

    public void move(int dx, int dy, Room room) {
        int newX = x + dx;
        int newY = y + dy;
        if (room.isWalkable(newX, newY)) {
            x = newX;
            y = newY;
        } else {
            System.out.println("You can't move there.");
        }
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void attack(Monster monster) {
        if (weapon == null) {
            System.out.println("You need a weapon to attack!");
            return;
        }
        monster.takeDamage(weapon.getDamage());
        this.takeDamage(monster.getDamage());
    }

    public void pickUp(Item item) {
        item.use(this);
    }

    public void takeDamage(int amount) {
        hp -= amount;
        System.out.println("You took " + amount + " damage.");
        if (hp <= 0) System.out.println("You died.");
    }

    public void heal(int amount) {
        hp = Math.min(25, hp + amount);
        System.out.println("Healed to " + hp + " HP.");
    }

    public void setWeapon(Weapon w) {
        this.weapon = w;
    }

    public String getWeaponName() {
        return (weapon == null) ? "None" : weapon.getName();
    }

    public int getDamage() {
        return (weapon == null) ? 0 : weapon.getDamage();
    }

    public boolean hasKey() { return hasKey; }
    public void obtainKey() { hasKey = true; }
}
