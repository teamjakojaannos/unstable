package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.entities.Poster;

public class PosterState implements Component<PosterState> {
    public boolean active;
    public Poster.Type type;
    public boolean dialogueShown;
    private boolean wasActive;

    public PosterState(Poster.Type type) {
        this.type = type;
    }

    public boolean activeChanged_onlyCallThisFromRendererPls() {
        if (this.active != this.wasActive) {
            this.wasActive = this.active;

            return true;
        }

        return false;
    }

    @Override
    public PosterState cloneComponent() {
        final var clone = new PosterState(this.type);
        clone.active = this.active;
        clone.wasActive = this.wasActive;
        return clone;
    }
}
