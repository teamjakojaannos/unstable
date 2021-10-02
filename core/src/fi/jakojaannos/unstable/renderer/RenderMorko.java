package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
                 
             });
    }

    public record Input(
            PhysicsBody body,
            Tags.Morko tag
    ) {}
}
