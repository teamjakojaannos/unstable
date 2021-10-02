package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPlayer implements EcsSystem<RenderPlayer.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture texture;
    private final Texture closetIndicator;


    public RenderPlayer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("Journalist.png");
        this.closetIndicator = new Texture("badlogic.jpg");
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

                 if (entity.hud.currentIndicator == PlayerHudComponent.Indicator.CLOSET) {
                     final var iconSize = 0.5f;
                     this.spriteBatch.draw(
                             this.closetIndicator,
                             x + width / 2.0f - iconSize / 2.0f,
                             y + height + 0.25f,
                             iconSize, iconSize
                     );
                 }
                 this.spriteBatch.end();
             });
    }

    @Override
    public void close() {
        this.texture.dispose();
    }

    public record Input(
            PhysicsBody body,
            PlayerHudComponent hud
    ) {}
}
