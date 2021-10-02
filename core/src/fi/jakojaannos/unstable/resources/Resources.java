package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.Gdx;
import fi.jakojaannos.unstable.InputState;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.renderer.Camera;

public class Resources {
    public final InputState playerInput = new InputState();
    public final Entities entities;
    public final Camera camera;
    public WorldBounds worldBounds;

    public Resources(final EcsWorld world, final WorldBounds worldBounds) {
        entities = new Entities(world);
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.worldBounds = worldBounds;
    }
}
