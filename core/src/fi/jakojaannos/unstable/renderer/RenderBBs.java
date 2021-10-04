package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.entities.BreakableBlocker;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderBBs implements EcsSystem<RenderBBs.Input>, AutoCloseable {
    private final Texture ruuggu;
    private final TextureRegion[][] variants;
    private final SpriteBatch spriteBatch;

    public RenderBBs(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.ruuggu = new Texture("ruuggu.png");
        this.variants = new TextureRegion[][]{
                {
                        new TextureRegion(this.ruuggu, 0, 0, 32, 64),
                        new TextureRegion(this.ruuggu, 32, 0, 32, 64),
                }
        };
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        spriteBatch.begin();
        input.entities()
             .forEach(entity -> {
                 final var body = entity.body;
                 final var bb = entity.bb;

                 final var region = this.variants[bb.type.ordinal()][bb.broken ? 1 : 0];

                 final var y = body.getPosition().y - 0.001f;
                 final var width = region.getRegionWidth() / 16.0f + 0.001f;
                 final var height = region.getRegionHeight() / 16.0f + 0.001f;
                 final var x = body.getPosition().x - 0.001f;

                 spriteBatch.draw(region, x, y, width, height);
             });
        spriteBatch.end();
    }

    @Override
    public void close() {
        this.ruuggu.dispose();
    }

    public record Input(
            Entity handle,
            PhysicsBody body,
            BreakableBlocker bb
    ) {}
}
