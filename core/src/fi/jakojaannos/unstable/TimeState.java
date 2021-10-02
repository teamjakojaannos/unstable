package fi.jakojaannos.unstable;

import fi.jakojaannos.unstable.resources.TimeManager;

public class TimeState {
    private final float timeStep;
    private float accumulator;
    private final TimeManager timeManager;

    public TimeState() {
        this.timeStep = UnstableGame.Constants.GameLoop.TIME_STEP;
        this.timeManager = new TimeManager();
    }

    public void consumeTime(final float deltaSeconds, final Tick tick) {
        // HACK: limit frame time to avoid lagging to death
        final var frameTime = Math.min(0.25f, deltaSeconds);

        this.accumulator += frameTime;
        while (this.accumulator >= this.timeStep) {
            this.accumulator -= this.timeStep;

            tick.run();

            this.timeManager.tick();
        }
    }

    public int currentTick() {
        return this.timeManager.currentTick();
    }

    public interface Tick {
        void run();
    }
}
