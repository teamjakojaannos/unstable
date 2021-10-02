package fi.jakojaannos.unstable.resources;

public class TimeManager {

    private int tickCounter;

    public void tick() {
        tickCounter++;
    }

    public int currentTick() {
        return tickCounter;
    }
}
