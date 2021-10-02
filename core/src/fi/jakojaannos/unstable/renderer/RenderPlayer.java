package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPlayer implements EcsSystem<RenderPlayer.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture texture;


    public RenderPlayer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("badlogic.jpg");
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities()
             .forEach(entity -> {
                 final var physics = entity.body();

                 this.spriteBatch.begin();
                 final var position = physics.body.getPosition();
                 final var x = position.x;
                 final var y = position.y;
                 final var width = 1.0f;
                 final var height = 1.0f;

                 this.spriteBatch.draw(this.texture, x, y, width, height);
                 this.spriteBatch.end();
             });
    }

    @Override
    public void close() {
        this.texture.dispose();
    }

    public record Input(
            PhysicsBody body
    ) {}
}
