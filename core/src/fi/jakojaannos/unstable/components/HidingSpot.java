package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.Component;

public class HidingSpot implements Component<HidingSpot> {
    public final Vector2 offset;
    public final Type type;
    public boolean occupied;

    public HidingSpot(Type type) {
        this.offset = switch (type) {
            case MansionClosetLarge -> new Vector2(0.85f, 1.0f);
            case MansionClosetThin -> new Vector2(0.5f, 1.0f);
        };
        this.type = type;
    }

    public HidingSpot(Vector2 offset, Type type) {
        this.offset = offset;
        this.type = type;
    }

    @Override
    public HidingSpot cloneComponent() {
        return new HidingSpot(this.offset, this.type);
    }

    public enum Type {
        MansionClosetLarge,
        MansionClosetThin,
    }
}
