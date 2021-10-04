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

    public static class Locked implements Component<Locked> {
        @Override
        public Locked cloneComponent() {
            return new Locked();
        }
    }

    public static class Nurse implements Component<Nurse> {
        @Override
        public Nurse cloneComponent() {
            return new Nurse();
        }
    }

    public static class Spoopy implements Component<Spoopy> {
        @Override
        public Spoopy cloneComponent() {
            return new Spoopy();
        }
    }
}
