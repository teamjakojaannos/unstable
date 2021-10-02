package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Null;
import fi.jakojaannos.unstable.InputState;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.renderer.Camera;

import java.util.Optional;

public class Resources {
    public final InputState playerInput = new InputState();
    public final Entities entities;
    public final Camera camera;
    public BoundingBox worldBounds;

    @Null
    private Vector2 playerPosition;

    public Resources(final EcsWorld world, final BoundingBox worldBounds, final BoundingBox cameraBounds) {
        entities = new Entities(world);
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cameraBounds);
        this.worldBounds = worldBounds;
    }

    public void setPlayerPosition(final Vector2 position) {
        if (this.playerPosition != null) {
            this.playerPosition.set(position);
        } else {
            this.playerPosition = new Vector2(position);
        }
    }

    public Optional<Vector2> playerPosition() {
        return Optional.ofNullable(this.playerPosition);
    }
}
