package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPosters implements EcsSystem<RenderPosters.Input>, AutoCloseable {
    private final Texture texture;
    private final TextureRegion[] variants;
    private final SpriteBatch spriteBatch;

    public RenderPosters(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("poster.png");
        this.variants = new TextureRegion[]{
                new TextureRegion(this.texture, 0, 0, 16, 16)
        };
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        spriteBatch.begin();
        input.entities()
             .forEach(poster -> {
                 final var body = poster.body;
                 final var type = poster.type;

                 final var region = this.variants[type.ordinal()];
                 final var y = body.getPosition().y - 0.001f;
                 final var width = region.getRegionWidth() / 16.0f + 0.001f;
                 final var height = region.getRegionHeight() / 16.0f + 0.001f;
                 final var x = body.getPosition().x - 0.001f + width;

                 spriteBatch.draw(region, x, y, width, height);
             });
        spriteBatch.end();
    }

    @Override
    public void close() {
        this.texture.dispose();
    }

    public record Input(
            PhysicsBody body,
            Poster.Type type
    ) {}
}
