package fi.jakojaannos.unstable.systems;

import com.badlogic.gdx.math.MathUtils;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.MovementAttributes;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.ecs.Without;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;

public class MoveCharacterSystem implements EcsSystem<MoveCharacterSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        final var delta = UnstableGame.Constants.GameLoop.TIME_STEP;
        var worldBorders = resources.worldBounds;

        input.entities()
             .forEach(entity -> {
                 final var movementInput = entity.input();
                 final var attributes = entity.attributes();
                 final var physics = entity.body();

                 final var moveAmount = movementInput.direction.x * attributes.moveSpeed * delta;
                 final var newX = MathUtils.clamp(
                         physics.getPosition().x + moveAmount,
                         worldBorders.min.x,
                         worldBorders.max.x
                 );

                 physics.facingRight = moveAmount == 0 ? physics.facingRight : moveAmount > 0;
                 physics.setPosition(newX, 1.0f);
             });
    }

    public record Input(
            MovementInput input,
            MovementAttributes attributes,
            PhysicsBody body,
            Optional<Tags.InAir> isInAir,
            Without<Tags.FreezeInput> frozenInputTag
    ) {
    }
}
