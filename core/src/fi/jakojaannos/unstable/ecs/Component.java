package fi.jakojaannos.unstable.ecs;

public interface Component<TSelf extends Component> {
    TSelf cloneComponent();
}
