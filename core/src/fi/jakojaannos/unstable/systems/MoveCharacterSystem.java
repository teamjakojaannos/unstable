package fi.jakojaannos.unstable.systems;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.ecs.Without;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MoveCharacterSystem implements EcsSystem<MoveCharacterSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        System.out.println("Ticking!");
        var i = new AtomicInteger(0);
        input.entities()
             .forEach(entity -> {
                 final var movementInput = entity.input();
                 final var physics = entity.body();
                 final var inAir = entity.isInAir();

                 System.out.printf(" -> entity %d (in air: %s)\n", i.getAndIncrement(), inAir.isPresent() ? "true" : "false");

                 final var clampedInput = movementInput.direction.len2() > 1
                         ? new Vector2(movementInput.direction).nor()
                         : movementInput.direction;

                 final var position = physics.body.getPosition();
                 System.out.printf(" -> position: %s\n", position.toString());

                 physics.body.applyLinearImpulse(
                         clampedInput,
                         position,
                         true
                 );
             });
    }

    public record Input(
            MovementInput input,
            PhysicsBody body,
            Optional<Tags.InAir> isInAir,
            Without<Tags.FreezeInput> frozenInputTag
    ) {}
}
