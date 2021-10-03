package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.MorkoAi;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class RenderMorko implements EcsSystem<RenderMorko.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture morkoTexture;
    private final TextureRegion[] walkFrames;
    private final TextureRegion[] idleFrames;
    private final TextureRegion[] attackFrames;
    private final TextureRegion[] searchFrames;

    private final Sound scream;
    private final Sound scream2;

    private long screamId = -1;
    private long screamId2;

    public RenderMorko(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.morkoTexture = new Texture("morko.png");
        this.walkFrames = new TextureRegion[4];
        this.idleFrames = new TextureRegion[1];
        this.attackFrames = new TextureRegion[8];
        this.searchFrames = new TextureRegion[8];

        final var frameWidth = 64;
        final var frameHeight = 92;

        var row = 0;
        for (int frameIndex = 0; frameIndex < this.walkFrames.length; frameIndex++) {
            this.walkFrames[frameIndex] = new TextureRegion(this.morkoTexture, frameWidth * frameIndex, row * frameHeight, frameWidth, frameHeight);
        }

        row++;
        for (int frameIndex = 0; frameIndex < this.idleFrames.length; frameIndex++) {
            this.idleFrames[frameIndex] = new TextureRegion(this.morkoTexture, frameWidth * frameIndex, row * frameHeight, frameWidth, frameHeight);
        }

        row++;
        for (int frameIndex = 0; frameIndex < this.attackFrames.length; frameIndex++) {
            this.attackFrames[frameIndex] = new TextureRegion(this.morkoTexture, frameWidth * frameIndex, row * frameHeight, frameWidth, frameHeight);
        }

        row++;
        for (int frameIndex = 0; frameIndex < this.searchFrames.length; frameIndex++) {
            this.searchFrames[frameIndex] = new TextureRegion(this.morkoTexture, frameWidth * frameIndex, row * frameHeight, frameWidth, frameHeight);
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
                    final var ai = entity.ai;
                    final var tick = resources.timeManager.currentTick();

                    final var distanceToPlayer = resources
                            .playerPosition()
                            .map(pos -> pos.dst(physics.getPosition()));

                    final var framesToUse = switch (ai.state) {
                        case IDLING -> this.idleFrames;
                        case WANDERING, CHASING -> this.walkFrames;
                        case SEARCHING -> this.searchFrames;
                        case ATTACKING -> this.attackFrames;
                    };

                    final var loopDuration = switch (ai.state) {
                        case SEARCHING -> 1.5;
                        case ATTACKING -> ai.attackDuration;
                        default -> 0.5;
                    };


                    final var scaledTick = switch (ai.state) {
                        case ATTACKING -> getProgress(ai.attackHandle, resources) * attackFrames.length;
                        default -> ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / framesToUse.length);
                    };
                    final var region = framesToUse[((int) scaledTick) % framesToUse.length];
                    region.flip(region.isFlipX() != physics.facingRight, false);

                    final var width = 4.0f;
                    final var offsetX = !region.isFlipX() ? 0.25f : 0.75f;

                    this.spriteBatch.begin();
                    this.spriteBatch.draw(region,
                            physics.getPosition().x + width * offsetX - width * 0.75f,
                            physics.getPosition().y - 0.25f,
                            0.0f,
                            0.0f,
                            width,
                            6.0f,
                            1.0f,
                            1.0f,
                            0.0f);
                    this.spriteBatch.end();

                    return distanceToPlayer.orElse(min);
                }, Float::min);

        final var maxDistance = 6.5f;
        if (minDistance < maxDistance) {
            final var baseVolume = 0.25f;
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

    private float getProgress(TimerHandle handle, Resources resources) {
        if (handle == null) {
            return 0.0f;
        }

        if (!resources.timers.isActiveAndValid(handle)) {
            return 0.0f;
        }

        return (resources.timers.getTimeElapsed(handle) / handle.duration());
    }

    @Override
    public void close() {
        this.scream.dispose();
        this.scream2.dispose();
        this.morkoTexture.dispose();
    }

    public record Input(
            PhysicsBody body,
            MorkoAi ai,
            Tags.Morko tag
    ) {
    }
}
