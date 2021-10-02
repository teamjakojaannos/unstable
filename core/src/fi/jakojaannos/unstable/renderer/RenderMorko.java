package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderMorko implements EcsSystem<RenderMorko.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture morko;
    private final TextureRegion[] morkoFrames;

    private final Sound scream;
    private final Sound scream2;

    private long screamId = -1;
    private long screamId2;

    public RenderMorko(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.morko = new Texture("morkowalk.png");
        this.morkoFrames = new TextureRegion[4];

        final var frameWidth = this.morko.getWidth() / this.morkoFrames.length;
        final var frameHeight = this.morko.getHeight();
        for (int frameIndex = 0; frameIndex < this.morkoFrames.length; frameIndex++) {
            this.morkoFrames[frameIndex] = new TextureRegion(this.morko, frameWidth * frameIndex, 0, frameWidth, frameHeight);
        }

        this.scream = Gdx.audio.newSound(Gdx.files.internal("Breathloop.ogg"));
        this.scream2 = Gdx.audio.newSound(Gdx.files.internal("Scream3.ogg"));
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        final var minDistance = input
                .entities()
                .reduce(Float.MAX_VALUE, (min, entity) -> {
                    final var physics = entity.body;
                    final var tick = resources.timeManager.currentTick();

                    final var distanceToPlayer = resources
                            .playerPosition()
                            .map(pos -> pos.dst(physics.getPosition()));

                    final var loopDuration = 0.5;

                    final var scaledTick = ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / this.morkoFrames.length);
                    final var region = this.morkoFrames[((int) scaledTick) % this.morkoFrames.length];
                    region.flip(region.isFlipX() == physics.facingRight, false);

                    this.spriteBatch.begin();
                    this.spriteBatch.draw(region,
                                          physics.getPosition().x,
                                          physics.getPosition().y - 0.25f,
                                          0.0f,
                                          0.0f,
                                          2.0f,
                                          6.0f,
                                          1.0f,
                                          1.0f,
                                          0.0f);
                    this.spriteBatch.end();

                    return distanceToPlayer.orElse(min);
                }, Float::min);

        final var maxDistance = 6.5f;
        if (minDistance < maxDistance) {
            final var baseVolume = 0.75f;
            final var volumeScale = (maxDistance - minDistance) / maxDistance;
            final var volume = baseVolume * volumeScale;

            if (this.screamId != -1) {
                this.scream.setVolume(this.screamId, volume);
                this.scream.setVolume(this.screamId2, volume);
            } else {
                this.screamId2 = this.scream2.loop(volume);
                this.screamId = this.scream.loop(volume, 1.5f, 0.0f);
            }
        } else {
            this.scream.stop();
            this.screamId = -1;
        }
    }

    @Override
    public void close() {
        this.scream.dispose();
        this.scream2.dispose();
        this.morko.dispose();
    }

    public record Input(
            PhysicsBody body,
            Tags.Morko tag
    ) {}
}
