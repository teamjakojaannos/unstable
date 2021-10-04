package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.UnstableGame;
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
            entity.hud.currentIndicator = PlayerHudComponent.Indicator.NONE;

            for (final var item : resources.interactItems.items) {
                if (entity.body.overlaps(item.body())) {

                    entity.hud.currentIndicator = item.icon();

                    if (entity.input.actionPressed) {
                        if (resources.timers.isActiveAndValid(resources.interactCooldown)) {
                            continue;
                        }

                        if (item.action().execute(item.entity(), resources)) {
                            resources.interactCooldown = resources.timers.set(UnstableGame.Constants.INTERACT_COOLDOWN, false, () -> {});
                            break;
                        }
                    }
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
