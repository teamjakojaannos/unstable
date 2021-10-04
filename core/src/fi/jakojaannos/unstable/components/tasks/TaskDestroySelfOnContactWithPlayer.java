package fi.jakojaannos.unstable.components.tasks;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public class TaskDestroySelfOnContactWithPlayer<T> implements Task<T>{
    private final T t;
    private final float targetDistance2;

    public TaskDestroySelfOnContactWithPlayer(float targetDistance, T t) {
        this.t = t;
        this.targetDistance2 = targetDistance*targetDistance;
    }

    @Override
    public void initialize(Entity entity, Resources resources) {
    }

    @Override
    public boolean isCompleted(Entity entity, Resources resources) {
        return true;
    }

    @Override
    public void doAction(Entity entity, Resources resources) {
        final var playerPos = resources.player.getComponent(PhysicsBody.class).orElseThrow().getPosition();
        final var distToPlayer = playerPos.dst2(entity.getComponent(PhysicsBody.class).orElseThrow().getPosition());
        if(distToPlayer <= this.targetDistance2){
            entity.destroy();
        }
    }

    @Override
    public T getState() {
        return t;
    }
}
