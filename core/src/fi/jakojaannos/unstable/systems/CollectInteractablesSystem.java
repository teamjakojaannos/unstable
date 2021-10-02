package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.InteractItems;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.Resources;

public class CollectInteractablesSystem implements EcsSystem<CollectInteractablesSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        resources.interactItems.items.clear();

        input.entities().forEach(entity -> resources.interactItems.items.add(new InteractItems.InteractableItem(entity.action, entity.body)));
    }

    public record Input(
            Interactable action,
            PhysicsBody body
    ) {
    }
}
