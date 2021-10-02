package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.ecs.Entity;

public class Interactable implements Component<Interactable> {
    public final Action action;

    public Interactable(final Action action) {
        this.action = action;
    }

    public void execute(Entity self) {
        this.action.execute(self);
    }

    @Override
    public Interactable cloneComponent() {
        return new Interactable(this.action);
    }

    public interface Action {
        void execute(Entity self);
    }
}
