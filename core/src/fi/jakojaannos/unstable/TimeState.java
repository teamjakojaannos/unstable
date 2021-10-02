package fi.jakojaannos.unstable;

public class TimeState {
    private final float timeStep;
    private float accumulator;

    public TimeState() {
        this.timeStep = UnstableGame.Constants.GameLoop.TIME_STEP;
    }

    public void consumeTime(final float deltaSeconds, final Tick tick) {
        // HACK: limit frame time to avoid lagging to death
        final var frameTime = Math.min(0.25f, deltaSeconds);

        this.accumulator += frameTime;
        while (this.accumulator >= this.timeStep) {
            this.accumulator -= this.timeStep;

            tick.run();
        }
    }

    public interface Tick {
        void run();
    }
}
