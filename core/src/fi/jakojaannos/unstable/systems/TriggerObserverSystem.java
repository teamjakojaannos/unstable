package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Trigger;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class TriggerObserverSystem implements EcsSystem<TriggerObserverSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        var optBody = resources.players.getPlayer()
                .flatMap(player -> player.getComponent(PhysicsBody.class));
        if (optBody.isEmpty()) {
            return;
        }
        final var playerBody = optBody.get();

        input.entities().forEach(entity -> {
            if (!entity.body.overlaps(playerBody)) {
                return;
            }

            if (canExecute(entity, resources)) {
                entity.trigger.action.execute(resources);
                entity.trigger.triggered = true;
                entity.trigger.cooldown = resources
                        .timers
                        .set(entity.trigger.triggerCooldown, false, () -> {
                        });
            }
        });
    }

    private boolean canExecute(Input entity, Resources resources) {
        var trigger = entity.trigger;

        if (trigger.triggered && !trigger.canRepeat) {
            return false;
        }

        var onCooldown = resources.timers.isActiveAndValid(trigger.cooldown);
        return !onCooldown;
    }

    public record Input(
            PhysicsBody body,
            Trigger trigger
    ) {
    }
}
