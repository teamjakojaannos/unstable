package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.InputManager;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.renderer.Camera;
import fi.jakojaannos.unstable.renderer.TextRenderer;

import java.util.Optional;

public class Resources {
    public final InputManager playerInput = new InputManager();
    public final Entities entities;
    public final Camera camera;
    public final BoundingBox worldBounds;
    public final Timers timers;
    public final TimeManager timeManager;
    public final Players players = new Players();

    public final InteractItems interactItems = new InteractItems();
    public PopUp popup;
    public ShaderProgram activeShader;
    public boolean spoopy;

    public Resources(
            final EcsWorld world,
            final BoundingBox worldBounds,
            final BoundingBox cameraBounds,
            final Timers timers,
            final TimeManager timeManager
    ) {
        entities = new Entities(world);
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cameraBounds);
        this.worldBounds = worldBounds;
        this.timers = timers;
        this.timeManager = timeManager;
    }


    public Optional<Vector2> playerPosition() {
        return this.players.getPlayerPosition();
    }
}
