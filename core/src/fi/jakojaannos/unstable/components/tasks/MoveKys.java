package fi.jakojaannos.unstable.components.tasks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public class MoveKys<T> implements Task<T> {
    private final Vector2 target;
    private final float targetDistance2;

    private final T state;

    public MoveKys(Vector2 target, float targetDistance, T state) {
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
        final var playerPos = resources.player.getComponent(PhysicsBody.class).orElseThrow().getPosition();
        final var distToPlayer = playerPos.dst2(entity.getComponent(PhysicsBody.class).orElseThrow().getPosition());
        if (distToPlayer <= this.targetDistance2) {

            var tick = resources.timeManager.currentTick();
            final var body = entity.getComponent(PhysicsBody.class).orElseThrow();
            final var pos = body.getPosition().cpy();
            pos.add(body.getWidth() / 2.0f, body.getHeight() / 2.0f);
            resources.particles.burst(pos, new Color(0.8f, 0.8f, 0.8f, 1.0f), 250, tick);

            entity.destroy();
            return;
        }

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
    public T getState() {
        return this.state;
    }
}
