package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.ecs.Component;

public class Interactable implements Component<Interactable> {

    public void execute() {
        System.out.println("Se on moro!");
    }

    @Override
    public Interactable cloneComponent() {
        return new Interactable();
    }
}
