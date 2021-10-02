package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.Component;

public class HidingSpot implements Component<HidingSpot> {
    public final Vector2 offset;
    public final Type type;
    public boolean occupied;
    private boolean oldOccupied;

    public HidingSpot(Type type) {
        this.offset = switch (type) {
            case MansionClosetLarge -> new Vector2(0.15f, 0.0f);
            case MansionClosetThin -> new Vector2(0.5f, 0.0f);
            case WallHole -> new Vector2(0.55f, 0.0f);
        };
        this.type = type;
    }

    public HidingSpot(Vector2 offset, Type type) {
        this.offset = offset;
        this.type = type;
    }

    @Override
    public HidingSpot cloneComponent() {
        return new HidingSpot(this.offset.cpy(), this.type);
    }

    public boolean occupiedChanged_onlyCallThisFromRendererPls() {
        if (this.occupied != this.oldOccupied) {
            this.oldOccupied = this.occupied;

            return true;
        }

        return false;
    }

    public boolean isCloset() {
        return switch (type) {
            case MansionClosetLarge, MansionClosetThin -> true;
            case WallHole -> false;
        };
    }

    public enum Type {
        MansionClosetLarge,
        MansionClosetThin,
        WallHole,
    }
}
