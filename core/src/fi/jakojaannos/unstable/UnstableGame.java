package fi.jakojaannos.unstable;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;
import fi.jakojaannos.unstable.acts.IntroAct;
import fi.jakojaannos.unstable.ecs.SystemDispatcher;
import fi.jakojaannos.unstable.resources.Resources;

public class UnstableGame extends ApplicationAdapter {
    private final TimeState timeState;
    private GameState gameState = new GameState();
    private Resources resources;

    private SpriteBatch batch;
    private SystemDispatcher dispatcher;
    private SystemDispatcher renderer;

    public UnstableGame() {
        this.timeState = new TimeState();
    }

    @Override
    public void create() {
        final var worldBoundLeft = 0.0f;
        final var worldBoundRight = 100.0f;

        final var cameraPadding = 5.0f;

        final var worldBounds = new BoundingBox(
                new Vector3(worldBoundLeft, 0.0f, 0.0f),
                new Vector3(worldBoundRight, 0.0f, 0.0f)
        );

        final var cameraBounds = new BoundingBox(
                new Vector3(worldBoundLeft + cameraPadding, 0.0f, 0.0f),
                new Vector3(worldBoundRight - cameraPadding, 0.0f, 0.0f)
        );
        this.resources = new Resources(this.gameState.world(), worldBounds, cameraBounds, timeState.timers, timeState.timeManager);

        this.batch = new SpriteBatch();

        // Initialize act
        final var intro = new IntroAct();
        this.dispatcher = new SystemDispatcher.Impl(intro.systems());
        this.renderer = new SystemDispatcher.Impl(intro.renderSystems(this.batch));
        this.gameState = intro.state();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.resources.camera.resize(width, height);
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(0, 0, 0, 1);
        this.batch.setProjectionMatrix(this.resources.camera.getCombinedMatrix());
        this.renderer.tick(this.gameState.world(), this.resources);
    }

    private void update(final float deltaSeconds) {
        final var currentTick = this.timeState.currentTick();
        this.resources.playerInput.updateKeyStates(currentTick);

        this.timeState.consumeTime(deltaSeconds, () -> {
            this.dispatcher.tick(this.gameState.world(), this.resources);

            this.gameState.world().reapEntities();
            this.gameState.world().spawnEntities();
        });
    }

    @Override
    public void dispose() {
        this.batch.dispose();
    }

    public static class Constants {
        public static class GameLoop {
            public static final int TICKS_PER_SECOND = 50;
            public static final float TIME_STEP = 1.0f / TICKS_PER_SECOND;
        }
    }
}
