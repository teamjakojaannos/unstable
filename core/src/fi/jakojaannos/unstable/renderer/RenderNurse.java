package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;

public class RenderNurse implements EcsSystem<RenderNurse.Input>, AutoCloseable {

    private final SpriteBatch spriteBatch;
    private final Texture nurseTexture;
    private final TextureRegion[] walkFrames;
    private final TextureRegion[] idleFrames;
    private final TextureRegion[] walkFramesSpooky;
    private final TextureRegion[] idleFramesSpooky;


    public RenderNurse(SpriteBatch batch) {
        this.spriteBatch = batch;
        this.nurseTexture = new Texture("nurse.png");
        this.walkFrames = new TextureRegion[4];
        this.walkFramesSpooky = new TextureRegion[4];
        this.idleFrames = new TextureRegion[1];
        this.idleFramesSpooky = new TextureRegion[1];

        final var frameWidth = 32;
        final var frameHeight = 48;
        for (int frameIndex = 0; frameIndex < this.walkFrames.length; frameIndex++) {
            this.walkFrames[frameIndex] = new TextureRegion(this.nurseTexture, frameWidth * frameIndex, 0, frameWidth, frameHeight);
            this.walkFramesSpooky[frameIndex] = new TextureRegion(this.nurseTexture, frameWidth * frameIndex, 2 * frameHeight, frameWidth, frameHeight);
        }

        for (int frameIndex = 0; frameIndex < this.idleFrames.length; frameIndex++) {
            this.idleFrames[frameIndex] = new TextureRegion(this.nurseTexture, frameWidth * frameIndex, frameHeight, frameWidth, frameHeight);
            this.idleFramesSpooky[frameIndex] = new TextureRegion(this.nurseTexture, frameWidth * frameIndex, 3 * frameHeight, frameWidth, frameHeight);
        }
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities().forEach(entity -> {
            final var body = entity.body;
            final var position = body.getPosition();
            final var x = position.x;
            final var y = position.y;
            final var width = body.getWidth() * 2;
            final var height = body.getHeight() * 2;
            final var originX = 0.0f;
            final var originY = 0.0f;

            final var tick = resources.timeManager.currentTick();
            final var loopDuration = 0.75;
            final var isWalking = body.speed > 0.001f;
            TextureRegion[] framesToUse;

            final var isSpoopy = resources.spoopy || entity.spoopy.isPresent();

            if (isWalking) {
                framesToUse = isSpoopy ? this.walkFramesSpooky : this.walkFrames;
            } else {
                framesToUse = isSpoopy ? this.idleFramesSpooky : this.idleFrames;
            }

            final var scaledTick = ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / framesToUse.length);
            final var region = framesToUse[((int) scaledTick) % framesToUse.length];
            region.flip(region.isFlipX() != body.facingRight, false);

            this.spriteBatch.begin();
            this.spriteBatch.draw(region,
                    x - width * 0.25f,
                    y,
                    originX,
                    originY,
                    width,
                    height,
                    1.0f,
                    1.0f,
                    0.0f
            );

            this.spriteBatch.end();
        });
    }

    @Override
    public void close() {
        this.nurseTexture.dispose();
    }

    public record Input(
            PhysicsBody body,
            Tags.Nurse tag,
            Optional<Tags.Spoopy> spoopy
    ) {
    }
}
