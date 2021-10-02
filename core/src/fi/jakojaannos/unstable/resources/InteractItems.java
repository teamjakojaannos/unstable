package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.components.PhysicsBody;

import java.util.ArrayList;
import java.util.List;

public class InteractItems {

    public final List<InteractableItem> items = new ArrayList<>();

    public record InteractableItem(
            Interactable action,
            PhysicsBody body
    ) {
    }
}
