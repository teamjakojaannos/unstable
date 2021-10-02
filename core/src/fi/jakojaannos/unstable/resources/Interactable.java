package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.ecs.Component;

public class Interactable implements Component<Interactable> {
    public final Action action;

    public Interactable(final Action action) {
        this.action = action;
    }

    public void execute() {
        this.action.execute();
    }

    @Override
    public Interactable cloneComponent() {
        return new Interactable(this.action);
    }

    public interface Action {
        void execute();
    }
}
