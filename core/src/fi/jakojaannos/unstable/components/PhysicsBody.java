package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class PhysicsBody implements Component<PhysicsBody> {
    private final BoundingBox bounds;
    private final Vector2 position;

    public float speed;
    public TimerHandle footstepTimer;
    public boolean facingRight;

    public PhysicsBody(final float x, final float y, float width, float height) {
        this(new Vector2(x, y), width, height);
    }

    public PhysicsBody(final Vector2 position, float width, float height) {
        this(position, new BoundingBox(
                new Vector3(0.0f, 0.0f, 0.0f),
                new Vector3(width, height, 0.0f))
        );
    }

    public PhysicsBody(final Vector2 position, final BoundingBox bounds) {
        this.bounds = bounds;
        this.position = position;
    }

    @Override
    public PhysicsBody cloneComponent() {
        return new PhysicsBody(new Vector2(this.position), new BoundingBox(this.bounds));
    }

    public Vector2 getPosition() {
        return new Vector2(this.position);
    }

    public void setPosition(final Vector2 position) {
        this.position.set(position);
    }

    public void setPosition(final float x, final float y) {
        this.position.set(x, y);
    }

    public boolean overlaps(final PhysicsBody another) {
        final var myBounds = this.boundsPositionCorrected();
        final var anotherBounds = another.boundsPositionCorrected();

        return myBounds.intersects(anotherBounds);
    }

    private BoundingBox boundsPositionCorrected() {
        final var min = this.bounds.min.cpy().add(this.position.x, this.position.y, 0.0f);
        final var max = this.bounds.max.cpy().add(this.position.x, this.position.y, 0.0f);

        return new BoundingBox(min, max);
    }

    public float getWidth() {
        return this.bounds.getWidth();
    }

    public float getHeight() {
        return this.bounds.getHeight();
    }
}
