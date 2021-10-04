package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.ecs.Entity;

public class Interactable implements Component<Interactable> {
    public final Action action;

    public Interactable(final Action action) {
        this.action = action;
    }

    public boolean execute(Entity self, Resources resources) {
        if (!this.action.condition(self, resources)) {
            this.action.onExecuteFailed(self, resources);
            return false;
        }

        return this.action.execute(self, resources);
    }

    @Override
    public Interactable cloneComponent() {
        return new Interactable(this.action);
    }

    public interface Action {
        default boolean condition(Entity self, Resources resources) {
            return true;
        }

        default void onExecuteFailed(Entity self, Resources resources) {/* NOOP */}

        boolean execute(Entity self, Resources resources);
    }
}
