package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPlayer implements EcsSystem<RenderPlayer.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture texture;


    public RenderPlayer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("Journalist.png");
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities()
             .forEach(entity -> {
                 final var physics = entity.body();

                 this.spriteBatch.begin();
                 final var position = physics.getPosition();
                 final var x = position.x;
                 final var y = position.y;
                 final var width = physics.getWidth() * 2;
                 final var height = physics.getHeight() * 2;

                 final var originX = 0.0f;
                 final var originY = 0.0f;

                 final var region = physics.facingRight
                         ? new TextureRegion(this.texture, 32, 48)
                         : new TextureRegion(this.texture, 32, 0, -32, 48);
                 this.spriteBatch.draw(region, x, y, originX, originY, width, height, 1.0f, 1.0f, 0.0f);
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
