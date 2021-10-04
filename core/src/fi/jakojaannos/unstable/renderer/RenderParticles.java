package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderParticles implements EcsSystem<RenderParticles.Input>, AutoCloseable {
    private final SpriteBatch batch;
    private final Texture texture;
    private final TextureRegion region;

    public RenderParticles(SpriteBatch spriteBatch) {
        this.batch = spriteBatch;
        this.texture = new Texture("pixel.png");
        this.region = new TextureRegion(texture);
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        final var currentTick = resources.timeManager.currentTick();

        resources.particles.list
                .stream().filter(particle -> !particle.hasDied(currentTick))
                .forEach(particle -> {
                    this.batch.begin();

                    final var pos = particle.positionAt(currentTick);
                    final var width = particle.widthAt(currentTick);
                    final var height = particle.heightAt(currentTick);
                    final var x = pos.x - width / 2.0f;
                    final var y = pos.y - height / 2.0f;
                    final var originX = width / 2.0f;
                    final var originY = height / 2.0f;

                    final var col = particle.color();
                    final var alpha = particle.alphaAt(currentTick);
                    this.batch.setColor(col.r, col.g, col.b, alpha);
                    this.batch.draw(region,
                            x, y,
                            originX, originY,
                            width, height,
                            1.0f, 1.0f,
                            0.0f
                    );

                    this.batch.end();
                });

        this.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void close() {
        this.texture.dispose();
    }

    public record Input() {
    }
}
