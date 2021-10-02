package fi.jakojaannos.unstable;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemDispatcher;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.physics.PhysicsContactListener;
import fi.jakojaannos.unstable.renderer.RenderPlayer;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.systems.*;

import java.util.List;

public class UnstableGame extends ApplicationAdapter {
    private final SystemDispatcher dispatcher;
    private final GameState gameState = new GameState();
    private final World physicsWorld;
    private final TimeState timeState;

    private Resources resources;

    private SpriteBatch batch;
    private SystemDispatcher renderer;

    public UnstableGame() {
        this.timeState = new TimeState();

        this.dispatcher = new SystemDispatcher.Impl(List.of(
                new PlayerInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new PlayerActionSystem()
        ));

        this.physicsWorld = new World(new Vector2(0.0f, 0.0f), true);
        this.physicsWorld.setContactListener(new PhysicsContactListener());

        this.gameState.world()
                .spawn(Player.create(this.physicsWorld, new Vector2(2.0f, 0.0f)));

        // borders
        this.gameState.world().spawn(Entity.builder()
                .component(new PhysicsBody(-1.0f, 1.0f, 1.0f, 2.0f)));
        this.gameState.world().spawn(Entity.builder()
                .component(new PhysicsBody(101.0f, 1.0f, 1.0f, 2.0f)));

        // vending machine
        this.gameState.world().spawn(Entity.builder()
                .component(new PhysicsBody(10.0f, 0.0f, 3.0f, 3.0f))
                .component(new Interactable()));

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
        this.resources = new Resources(this.gameState.world(), worldBounds, cameraBounds);

        this.batch = new SpriteBatch();

        this.renderer = new SystemDispatcher.Impl(List.of(
                new RenderPlayer(this.batch)
        ));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.resources.camera.resize(width, height);
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(1, 0, 0, 1);
        this.batch.setProjectionMatrix(this.resources.camera.getCombinedMatrix());
        this.renderer.tick(this.gameState.world(), this.resources);
    }

    private void update(final float deltaSeconds) {
        handleInput();

        this.timeState.consumeTime(deltaSeconds, () -> {
            this.dispatcher.tick(this.gameState.world(), this.resources);

            this.gameState.world().reapEntities();
            this.gameState.world().spawnEntities();

            this.physicsWorld.step(Constants.GameLoop.TIME_STEP,
                    Constants.PhysicsEngine.VELOCITY_ITERATIONS,
                    Constants.PhysicsEngine.POSITION_ITERATIONS);
        });
    }

    private void handleInput() {
        final var inputState = this.resources.playerInput;
        inputState.upPressed = Gdx.input.isKeyPressed(Input.Keys.W);
        inputState.downPressed = Gdx.input.isKeyPressed(Input.Keys.S);
        inputState.leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        inputState.rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);
        inputState.actionPressed = Gdx.input.isKeyPressed(Input.Keys.F);
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

        public static class PhysicsEngine {
            public static final int VELOCITY_ITERATIONS = 6;
            public static final int POSITION_ITERATIONS = 2;
        }

        public static class Collision {
            public static final short CATEGORY_TERRAIN = 0x0001;
            public static final short CATEGORY_PLAYER = 0x0002;

            public static final short MASK_PLAYER = ~(CATEGORY_PLAYER);
        }
    }
}
