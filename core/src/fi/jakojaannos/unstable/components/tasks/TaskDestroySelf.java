package fi.jakojaannos.unstable.components.tasks;

import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public class TaskDestroySelf<EntityState> implements Task<EntityState> {

    private final EntityState state;

    public TaskDestroySelf(EntityState state) {
        this.state = state;
    }

    @Override
    public void initialize(Entity entity, Resources resources) {
        entity.destroy();
    }

    @Override
    public boolean isCompleted(Entity entity, Resources resources) {
        return true;
    }

    @Override
    public void doAction(Entity entity, Resources resources) {
    }

    @Override
    public EntityState getState() {
        return this.state;
    }
}
