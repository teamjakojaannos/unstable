package fi.jakojaannos.unstable;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.SystemDispatcher;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.physics.PhysicsContactListener;
import fi.jakojaannos.unstable.systems.MoveCharacterSystem;

import java.util.List;

public class UnstableGame extends ApplicationAdapter {
    private final SystemDispatcher dispatcher;
    private final GameState gameState = new GameState();
    private final World physicsWorld;
    private final TimeState timeState;

    SpriteBatch batch;
    Texture img;

    public UnstableGame() {
        this.timeState = new TimeState();

        this.dispatcher = new SystemDispatcher.Impl(List.of(
                new MoveCharacterSystem()
        ));

        this.physicsWorld = new World(new Vector2(0.0f, -20.0f), true);
        this.physicsWorld.setContactListener(new PhysicsContactListener());

        this.gameState.world()
                      .spawn(Player.create(this.physicsWorld, new Vector2(5.0f, 5.0f)));
        this.gameState.world()
                      .spawn(Player.create(this.physicsWorld, new Vector2(5.0f, 5.0f))
                                   .component(new Tags.InAir()));

        this.gameState.world()
                      .spawn(Player.create(this.physicsWorld, new Vector2(5.0f, 5.0f))
                                   .component(new Tags.FreezePhysics()));
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    private void update(final float deltaSeconds) {
        this.timeState.consumeTime(deltaSeconds, () -> {
            this.dispatcher.tick(this.gameState.world());
            this.physicsWorld.step(Constants.GameLoop.TIME_STEP,
                                   Constants.PhysicsEngine.VELOCITY_ITERATIONS,
                                   Constants.PhysicsEngine.POSITION_ITERATIONS);
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
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
