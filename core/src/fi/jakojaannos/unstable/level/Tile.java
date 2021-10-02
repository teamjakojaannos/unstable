package fi.jakojaannos.unstable.level;

public record Tile(int id, int x, int y, boolean isWall) {
    public Tile(int id, int x, int y) {
        this(id, x, y, false);
    }

    public boolean isWall() {
        return isWall;
    }
}
