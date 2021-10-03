package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class PlayerHudComponent implements Component<PlayerHudComponent> {

    public Indicator currentIndicator;

    public PlayerHudComponent(final Indicator currentIndicator) {
        this.currentIndicator = currentIndicator;
    }

    @Override
    public PlayerHudComponent cloneComponent() {
        return new PlayerHudComponent(this.currentIndicator);
    }


    public enum Indicator implements Component<Indicator> {
        NONE, CLOSET, QUESTION;

        @Override
        public Indicator cloneComponent() {
            return this;
        }
    }
}
