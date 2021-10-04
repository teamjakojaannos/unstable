package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.Mirror;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderMirror implements EcsSystem<RenderMirror.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture texture;
    private final TextureRegion[] bgFrames;

    private final TextureRegion[] gremlinsEnterFrames;
    private final TextureRegion[] gremlinsLeaveFrames;
    private final TextureRegion[] gremlinsLaughFrames;
    private final TextureRegion[] longBoiEnterFrames;

    public RenderMirror(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("Mirror.png");

        this.bgFrames = new TextureRegion[]{
                new TextureRegion(this.texture, 0, 0, 32, 128),
                new TextureRegion(this.texture, 32, 0, 32, 128),
        };

        this.gremlinsEnterFrames = new TextureRegion[]{
                new TextureRegion(this.texture, 0, 0, 1, 1),
                new TextureRegion(this.texture, 32 * 2, 0, 32, 128),
                new TextureRegion(this.texture, 32 * 2, 0, 32, 128),
                new TextureRegion(this.texture, 32 * 3, 0, 32, 128),
                new TextureRegion(this.texture, 32 * 4, 0, 32, 128),
                new TextureRegion(this.texture, 0, 128, 32, 128),
        };
        this.gremlinsLeaveFrames = new TextureRegion[]{
                new TextureRegion(this.texture, 32 * 4, 0, 32, 128),
                new TextureRegion(this.texture, 32 * 3, 0, 32, 128),
                new TextureRegion(this.texture, 32 * 2, 0, 32, 128),
                new TextureRegion(this.texture, 0, 0, 1, 1),
                new TextureRegion(this.texture, 0, 0, 1, 1),
                new TextureRegion(this.texture, 0, 0, 1, 1),
        };
        this.gremlinsLaughFrames = new TextureRegion[]{
                new TextureRegion(this.texture, 32 * 4, 0, 32, 128),
                new TextureRegion(this.texture, 0, 128, 32, 128),
        };

        this.longBoiEnterFrames = new TextureRegion[]{
                new TextureRegion(this.texture, 32, 128, 32, 128),
                new TextureRegion(this.texture, 32 * 2, 128, 32, 128),
                new TextureRegion(this.texture, 32 * 3, 128, 32, 128),
                new TextureRegion(this.texture, 32 * 4, 128, 32, 128),
                new TextureRegion(this.texture, 0, 128 * 2, 32, 128),
                new TextureRegion(this.texture, 32, 128 * 2, 32, 128),
                new TextureRegion(this.texture, 32 * 2, 128 * 2, 32, 128),
                new TextureRegion(this.texture, 32 * 3, 128 * 2, 32, 128),
                new TextureRegion(this.texture, 32 * 4, 128 * 2, 32, 128),
                new TextureRegion(this.texture, 0, 128 * 3, 32, 128),
                new TextureRegion(this.texture, 32, 128 * 3, 32, 128),
                new TextureRegion(this.texture, 32 * 2, 128 * 3, 32, 128),
                new TextureRegion(this.texture, 32 * 3, 128 * 3, 32, 128),
                new TextureRegion(this.texture, 32 * 4, 128 * 3, 32, 128),
                new TextureRegion(this.texture, 0, 128 * 4, 32, 128),
                new TextureRegion(this.texture, 32, 128 * 4, 32, 128),

                // Uncomment for LOLNOPE
                // new TextureRegion(this.texture, 32, 128 * 4, 32, 128),
                // new TextureRegion(this.texture, 0, 128 * 4, 32, 128),
                // new TextureRegion(this.texture, 32 * 4, 128 * 3, 32, 128),
                // new TextureRegion(this.texture, 32 * 3, 128 * 3, 32, 128),
                // new TextureRegion(this.texture, 32 * 2, 128 * 3, 32, 128),
                // new TextureRegion(this.texture, 32, 128 * 3, 32, 128),
                // new TextureRegion(this.texture, 0, 128 * 3, 32, 128),
                // new TextureRegion(this.texture, 32 * 4, 128 * 2, 32, 128),
                // new TextureRegion(this.texture, 32 * 3, 128 * 2, 32, 128),
                // new TextureRegion(this.texture, 32 * 2, 128 * 2, 32, 128),
                // new TextureRegion(this.texture, 32, 128 * 2, 32, 128),
                // new TextureRegion(this.texture, 0, 128 * 2, 32, 128),
                // new TextureRegion(this.texture, 32 * 4, 128, 32, 128),
                // new TextureRegion(this.texture, 32 * 3, 128, 32, 128),
                // new TextureRegion(this.texture, 32 * 2, 128, 32, 128),
                // new TextureRegion(this.texture, 32, 128, 32, 128),
        };
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        final var maybeEntity = input.entities().findAny();
        if (maybeEntity.isEmpty()) {
            return;
        }
        final var entity = maybeEntity.get();

        if (!resources.timers.isActiveAndValid(resources.interactCooldown)) {
            if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
                resources.interactCooldown = resources.timers.set(1.0f, false, () -> {});
                entity.mirror.nextState(resources, entity.body.getPosition());
            }
        }

        final var physics = entity.body;

        final var bgRegion = this.bgFrames[resources.spoopy ? 1 : 0];

        final var width = 2.0f;
        this.spriteBatch.begin();
        this.spriteBatch.draw(bgRegion,
                              physics.getPosition().x,
                              physics.getPosition().y,
                              width,
                              8.0f);


        final var progress = resources.timers.isActiveAndValid(entity.mirror.stateProgress)
                ? resources.timers.getTimeElapsed(entity.mirror.stateProgress)
                : 0.0f;
        final var duration = resources.timers.isActiveAndValid(entity.mirror.stateProgress)
                ? entity.mirror.stateProgress.duration()
                : 0.0f;

        final var framesToUse = switch (entity.mirror.state) {
            case GremlinsEnter -> this.gremlinsEnterFrames;
            case GremlinsLeave -> this.gremlinsLeaveFrames;
            case Gremlins -> this.gremlinsLaughFrames;
            case LongBoiEnter -> this.longBoiEnterFrames;
            default -> null;
        };

        if (framesToUse != null) {
            final var frameIndex = (progress / duration) * framesToUse.length;
            final var region = framesToUse[(int) frameIndex];

            this.spriteBatch.draw(region,
                                  physics.getPosition().x,
                                  physics.getPosition().y,
                                  width,
                                  8.0f);
        }
        this.spriteBatch.end();
    }

    @Override
    public void close() {
        this.texture.dispose();
    }

    public record Input(
            PhysicsBody body,
            Mirror mirror
    ) {}
}
