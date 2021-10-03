package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class Mirror implements Component<Mirror> {


    @Override
    public Mirror cloneComponent() {
        return this;
    }
}
