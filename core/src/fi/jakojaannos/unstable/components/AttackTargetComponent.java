package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.ecs.Entity;

public class AttackTargetComponent implements Component<AttackTargetComponent> {

    public final AttackAction action;

    public AttackTargetComponent(AttackAction action) {
        this.action = action;
    }

    @Override
    public AttackTargetComponent cloneComponent() {
        return new AttackTargetComponent(this.action);
    }

    public interface AttackAction {
        void takeHit(Entity self);
    }
}
