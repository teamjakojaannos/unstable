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

public class RenderMorko implements EcsSystem<RenderMorko.Input> {
    private final SpriteBatch spriteBatch;
    private final Texture morko;
    private final TextureRegion[] morkoFrames;

    public RenderMorko(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.morko = new Texture("morkowalk.png");
        this.morkoFrames = new TextureRegion[4];

        final var frameWidth = this.morko.getWidth() / this.morkoFrames.length;
        final var frameHeight = this.morko.getHeight();
        for (int frameIndex = 0; frameIndex < this.morkoFrames.length; frameIndex++) {
            this.morkoFrames[frameIndex] = new TextureRegion(this.morko, frameWidth * frameIndex, 0, frameWidth, frameHeight);
        }
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        input.entities()
             .forEach(entity -> {
                 final var physics = entity.body;
                 final var tick = resources.timeManager.currentTick();

                 final var loopDuration = 0.5;

                 final var scaledTick = ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / this.morkoFrames.length);
                 final var region = this.morkoFrames[((int) scaledTick) % this.morkoFrames.length];
                 region.flip(region.isFlipX() == physics.facingRight, false);

                 this.spriteBatch.begin();
                 this.spriteBatch.draw(region,
                                       physics.getPosition().x,
                                       physics.getPosition().y - 0.21f,
                                       0.0f,
                                       0.0f,
                                       2.0f,
                                       4.0f,
                                       1.0f,
                                       1.0f,
                                       0.0f);
                 this.spriteBatch.end();
             });
    }

    public record Input(
            PhysicsBody body,
            Tags.Morko tag
    ) {}
}
