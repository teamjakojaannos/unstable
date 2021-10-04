package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;


// Adding this component to an entity triggers the sound effect once
public class SoundTags {
    private SoundTags() {}

    public static class Locked implements Component<Locked> {
        @Override
        public Locked cloneComponent() {
            return new Locked();
        }
    }

    public static class DoorCreak implements Component<DoorCreak> {
        @Override
        public DoorCreak cloneComponent() {
            return new DoorCreak();
        }
    }
}
