package fi.jakojaannos.unstable.resources;

public class WorldBounds {

    public final float leftBound, rightBound;

    public WorldBounds(float leftBound, float rightBound) {
        this.leftBound = Math.min(leftBound, rightBound);
        this.rightBound = Math.max(leftBound, rightBound);
    }
}
