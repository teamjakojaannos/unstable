package fi.jakojaannos.unstable.components.tasks;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public class TaskMove<EntityState> implements Task<EntityState> {
    private final Vector2 target;
    private final float targetDistance2;

    private final EntityState state;

    public TaskMove(Vector2 target, float targetDistance, EntityState state) {
        this.target = target;
        this.targetDistance2 = targetDistance * targetDistance;
        this.state = state;
    }

    @Override
    public void initialize(Entity entity, Resources resources) {
    }

    @Override
    public boolean isCompleted(Entity entity, Resources resources) {
        return entity.getComponent(PhysicsBody.class)
                .map(body -> body.getPosition().dst2(this.target) <= this.targetDistance2)
                .orElse(false);
    }

    @Override
    public void doAction(Entity entity, Resources resources) {
        final var optBody = entity.getComponent(PhysicsBody.class);
        final var optInput = entity.getComponent(MovementInput.class);
        if (optBody.isEmpty() || optInput.isEmpty()) {
            return;
        }
        final var body = optBody.get();
        final var input = optInput.get();

        input.direction
                .set(this.target)
                .sub(body.getPosition())
                .nor();
    }

    @Override
    public EntityState getState() {
        return this.state;
    }
}
