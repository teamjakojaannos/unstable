package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class ApplyDragSystem implements EcsSystem<ApplyDragSystem.Input> {
    private static final float MIN_SPEED = 0.000001f;

    @Override
    public void tick(
            final SystemInput<Input> input,
            final Resources resources
    ) {
        input.entities().forEach(entity -> {
            final var physics = entity.body();

            final var velocity = physics.body.getLinearVelocity().cpy();
            final var speedSq = velocity.len2();

            // through the frictions and drags
            final var dragForce = speedSq * physics.dragCoefficient;
            final var dragVec = velocity.cpy().nor().scl(-dragForce);

            physics.body.applyLinearImpulse(dragVec, physics.body.getPosition(), true);
        });
    }

    public record Input(
            PhysicsBody body
    ) {}
}
