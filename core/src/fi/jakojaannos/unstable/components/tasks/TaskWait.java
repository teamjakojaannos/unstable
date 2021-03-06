package fi.jakojaannos.unstable.components.tasks;

import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class TaskWait<EntityState> implements Task<EntityState> {
    private final float duration;
    private TimerHandle handle;

    private final EntityState state;

    public TaskWait(float seconds, EntityState state) {
        this.duration = seconds;
        this.state = state;
    }

    @Override
    public void initialize(Entity entity, Resources resources) {
        this.handle = resources.timers.set(this.duration, false, () -> {
        });
    }

    @Override
    public boolean isCompleted(Entity entity, Resources resources) {
        final var isActive = resources.timers.isActiveAndValid(this.handle);
        return !isActive;
    }

    @Override
    public void doAction(Entity entity, Resources resources) {
    }

    @Override
    public EntityState getState() {
        return this.state;
    }
}
