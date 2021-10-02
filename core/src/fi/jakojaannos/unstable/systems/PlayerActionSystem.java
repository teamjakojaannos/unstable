package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.components.PlayerInput;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class PlayerActionSystem implements EcsSystem<PlayerActionSystem.Input> {


    @Override
    public void tick(SystemInput<Input> input, Resources resources) {

        input.entities().forEach(entity -> {

            if (entity.input.action2Pressed) {
                resources.timers.set(2.5f, false, () -> System.out.println("Hello from the timers"));
            }

            entity.hud.currentIndicator = PlayerHudComponent.Indicator.NONE;

            for (final var item : resources.interactItems.items) {
                if (entity.body.overlaps(item.body())) {

                    entity.hud.currentIndicator = PlayerHudComponent.Indicator.CLOSET;

                    if (entity.input.actionPressed) {
                        item.action().execute(item.entity(), resources);
                    }
                    break;
                }
            }
        });
    }

    public record Input(
            Tags.Player playerTag,
            PlayerInput input,
            PhysicsBody body,
            PlayerHudComponent hud
    ) {
    }
}
