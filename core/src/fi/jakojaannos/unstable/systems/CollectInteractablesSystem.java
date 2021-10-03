package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.InteractItems;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;

public class CollectInteractablesSystem implements EcsSystem<CollectInteractablesSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        resources.interactItems.items.clear();

        input.entities().forEach(entity -> resources.interactItems.items.add(
                new InteractItems.InteractableItem(entity.action,
                                                   entity.body,
                                                   entity.entity,
                                                   entity.indicator.orElse(PlayerHudComponent.Indicator.CLOSET))));
    }

    public record Input(
            Interactable action,
            PhysicsBody body,
            Entity entity,
            Optional<PlayerHudComponent.Indicator> indicator
    ) {
    }
}
