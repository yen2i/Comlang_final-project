package game;

public class Door {
    private String targetRoomFile;  // 예: "room2.csv"

    public Door(String targetRoomFile) {
        this.targetRoomFile = targetRoomFile;
    }

    public String getTargetRoomFile() {
        return targetRoomFile;
    }

    public boolean canPass(Hero hero) {
        return hero.hasKey();
    }

    public void tryEnter(Hero hero) {
        if (canPass(hero)) {
            System.out.println("You unlock the door and move to " + targetRoomFile);
        } else {
            System.out.println("The door is locked. You need a key.");
        }
    }
}
