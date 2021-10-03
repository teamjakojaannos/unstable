package fi.jakojaannos.unstable;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;
import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.acts.act1.Act1;
import fi.jakojaannos.unstable.ecs.SystemDispatcher;
import fi.jakojaannos.unstable.resources.Resources;

public class UnstableGame extends ApplicationAdapter {
    private final TimeState timeState;
    private GameState gameState = new GameState();
    private Resources resources;

    private SpriteBatch batch;
    private SystemDispatcher dispatcher;
    private SystemDispatcher renderer;
    private Act currentAct;

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
        resources.nextAct = new Act1();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.resources.camera.resize(width, height);
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        if (this.renderer == null) {
            return;
        }
        ScreenUtils.clear(0, 0, 0, 1);
        this.batch.setProjectionMatrix(this.resources.camera.getCombinedMatrix());
        this.renderer.tick(this.gameState.world(), this.resources);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            final var x = ((float) Gdx.input.getX() / (float) Gdx.graphics.getWidth());
            final var y = 1.0f - ((float) Gdx.input.getY() / (float) Gdx.graphics.getHeight());
            final var xUnits = x * resources.camera.getWidthInUnits();
            final var yUnits = y * resources.camera.getHeightInUnits();

            final var cameraPos = resources.camera.getBottomLeft();
            final var xRelativeToCam = cameraPos.x + xUnits;
            final var yRelativeToCam = cameraPos.y + yUnits;
            System.out.printf("x(%.2f, %.2f)\n", xRelativeToCam, yRelativeToCam);
        }
    }

    private void update(final float deltaSeconds) {
        if (this.resources.nextAct != null) {
            this.currentAct = this.resources.nextAct;
            this.dispatcher = new SystemDispatcher.Impl(this.currentAct.systems());
            this.renderer = new SystemDispatcher.Impl(this.currentAct.renderSystems(this.batch));
            this.gameState = null;

            this.resources.nextAct = null;
        }

        if (this.resources.nextRoom != null || this.gameState == null) {
            if (this.resources.nextRoom != null) {
                this.gameState = this.currentAct.state(this.resources.nextRoom, resources);
            } else {
                this.gameState = this.currentAct.state(resources);
            }

            this.resources.reset(this.gameState.world());
            this.resources.nextRoom = null;
        }

        final var currentTick = this.timeState.currentTick();
        this.resources.playerInput.updateKeyStates(currentTick);

        this.timeState.consumeTime(deltaSeconds, () -> {
            this.dispatcher.tick(this.gameState.world(), this.resources);

            this.gameState.world().reapEntities();
            this.gameState.world().spawnEntities();
        }, this.gameState.world());
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

        public static class Debug {
            public static final boolean DEBUG_RENDERER_ENABLED = true;
        }
    }
}
