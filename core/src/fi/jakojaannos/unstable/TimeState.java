package fi.jakojaannos.unstable;

import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.resources.TimeManager;
import fi.jakojaannos.unstable.resources.Timers;

public class TimeState {
    private final float timeStep;
    private float accumulator;
    public final TimeManager timeManager;
    public final Timers timers;

    public TimeState() {
        this.timeStep = UnstableGame.Constants.GameLoop.TIME_STEP;
        this.timeManager = new TimeManager();
        this.timers = new Timers();
    }

    public void consumeTime(final float deltaSeconds, final Tick tick, final EcsWorld world) {
        // HACK: limit frame time to avoid lagging to death
        final var frameTime = Math.min(0.25f, deltaSeconds);

        this.accumulator += frameTime;
        while (this.accumulator >= this.timeStep) {
            this.accumulator -= this.timeStep;

            tick.run();

            this.timeManager.tick();
            this.timers.tick(deltaSeconds);
            world.commitComponentModifications();
        }
    }

    public int currentTick() {
        return this.timeManager.currentTick();
    }

    public interface Tick {
        void run();
    }
}
