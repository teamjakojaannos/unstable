package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class PlayerInput implements Component<PlayerInput> {

    public boolean actionPressed;
    public boolean action2Pressed;
    public boolean newspaperPressed;

    @Override
    public PlayerInput cloneComponent() {
        return new PlayerInput();
    }
}
