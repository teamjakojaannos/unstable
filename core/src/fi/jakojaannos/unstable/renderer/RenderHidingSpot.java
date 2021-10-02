package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderHidingSpot implements EcsSystem<RenderHidingSpot.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private Texture atlas;
    private TextureRegion[][] regions;

    public RenderHidingSpot(SpriteBatch batch) {
        this.spriteBatch = batch;
        this.atlas = new Texture("mansion_tiles.png");
        this.regions = new TextureRegion[][]{
                {
                        new TextureRegion(this.atlas, 0, 16, 41, 64),
                        new TextureRegion(this.atlas, 32, 96, 41, 64)
                },
                {
                        new TextureRegion(this.atlas, 48, 16, 31, 64),
                        new TextureRegion(this.atlas, 80, 96, 31, 64)
                },
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
                 final var body = entity.body();
                 final var hidingSpot = entity.hidingSpot();
                 final var region = this.regions[hidingSpot.type.ordinal()][hidingSpot.occupied ? 1 : 0];
                 final var x = body.getPosition().x;
                 final var y = body.getPosition().y;
                 final var width = region.getRegionWidth() / 16.0f;
                 final var height = region.getRegionHeight() / 16.0f;

                 spriteBatch.draw(region, x, y, width, height);
             });
        spriteBatch.end();
    }

    @Override
    public void close() {
        this.atlas.dispose();
    }

    public record Input(
            PhysicsBody body,
            HidingSpot hidingSpot
    ) {}
}
