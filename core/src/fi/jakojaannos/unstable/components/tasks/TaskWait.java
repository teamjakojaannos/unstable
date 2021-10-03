package fi.jakojaannos.unstable.components.tasks;

import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class TaskWait implements Task {

    private final float duration;
    private TimerHandle handle;

    public TaskWait(float seconds) {
        this.duration = seconds;
    }

    @Override
    public boolean isCompleted(Entity entity, Resources resources) {
        return !resources.timers.isActiveAndValid(this.handle);
    }

    @Override
    public void doAction(Entity entity, Resources resources) {
        var timers = resources.timers;
        if (timers.isActiveAndValid(this.handle)) {
            return;
        }

        this.handle = timers.set(this.duration, false, () -> {});
    }
}
