package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.ecs.Entity;

import java.util.ArrayList;
import java.util.List;

public class InteractItems {

    public final List<InteractableItem> items = new ArrayList<>();

    public record InteractableItem(
            Interactable action,
            PhysicsBody body,
            Entity entity,
            PlayerHudComponent.Indicator icon
    ) {}
}
