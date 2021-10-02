package fi.jakojaannos.unstable;

import com.badlogic.gdx.ApplicationAdapter;
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

    SpriteBatch batch;
    Texture img;

    public UnstableGame() {
        this.dispatcher = new SystemDispatcher.Impl(List.of(
                new MoveCharacterSystem()
        ));

        this.physicsWorld = new World(new Vector2(0.0f, -20.0f), true);
        this.physicsWorld.setContactListener(new PhysicsContactListener());

        this.gameState.world()
                      .spawn(Player.create(physicsWorld, new Vector2(5.0f, 5.0f)));
        this.gameState.world()
                      .spawn(Player.create(physicsWorld, new Vector2(5.0f, 5.0f))
                                   .component(new Tags.InAir()));

        this.gameState.world()
                      .spawn(Player.create(physicsWorld, new Vector2(5.0f, 5.0f))
                                   .component(new Tags.FreezePhysics()));
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        update();

        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    private void update() {
        this.dispatcher.tick(this.gameState.world());
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    public static class Constants {

        public static class Collision {
            public static final short CATEGORY_TERRAIN = 0x0001;
            public static final short CATEGORY_PLAYER = 0x0002;

            public static final short MASK_PLAYER = ~(CATEGORY_PLAYER);
        }
    }
}
