package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class Trigger implements Component<Trigger> {



    @Override
    public Trigger cloneComponent() {
        return new Trigger();
    }
}
