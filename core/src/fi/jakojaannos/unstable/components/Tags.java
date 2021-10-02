package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class Tags {
    private Tags() {}

    public static class InAir implements Component<InAir> {
        @Override
        public InAir cloneComponent() {
            return new InAir();
        }
    }

    public static class FreezeInput implements Component<FreezeInput> {
        @Override
        public FreezeInput cloneComponent() {
            return new FreezeInput();
        }
    }

    public static class Player implements Component<Player> {
        @Override
        public Player cloneComponent() {
            return new Player();
        }
    }

    public static class Morko implements Component<Morko> {
        @Override
        public Morko cloneComponent() {
            return new Morko();
        }
    }
}
