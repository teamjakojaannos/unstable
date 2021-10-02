package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPlayer implements EcsSystem<RenderPlayer.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture texture;
    private final Texture textureIdle;
    private final TextureRegion[] frames;
    private final TextureRegion[] idleFrames;
    private final Texture closetIndicator;

    private final Sound[] sounds;

    public RenderPlayer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("Journalist.png");
        this.textureIdle = new Texture("Journalist_idle.png");
        this.frames = new TextureRegion[8];

        final var frameWidth = this.texture.getWidth() / this.frames.length;
        final var frameHeight = this.texture.getHeight();
        for (int frameIndex = 0; frameIndex < this.frames.length; frameIndex++) {
            this.frames[frameIndex] = new TextureRegion(this.texture, frameWidth * frameIndex, 0, frameWidth, frameHeight);
        }

        this.idleFrames = new TextureRegion[]{
                new TextureRegion(this.textureIdle)
        };

        this.closetIndicator = new Texture("badlogic.jpg");

        this.sounds = new Sound[]{
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood1.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood2.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood3.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood4.ogg")),
        };
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

                 final var tick = resources.timeManager.currentTick();
                 final var framesToUse = physics.speed > 0.001f
                         ? this.frames
                         : this.idleFrames;

                 final var loopDuration = 1.25;

                 final var scaledTick = ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / framesToUse.length);
                 final var region = framesToUse[((int) scaledTick) % framesToUse.length];
                 region.flip(region.isFlipX() == physics.facingRight, false);

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

                 if (physics.speed == 0f) {
                     resources.timers.clear(physics.footstepTimer);
                 } else if (!resources.timers.isActiveAndValid(physics.footstepTimer)) {
                     final var footstepTime = 0.5f;
                     physics.footstepTimer = resources.timers.set(footstepTime, false, () -> {
                         final var sound = this.sounds[MathUtils.random(this.sounds.length - 1)];
                         sound.play();
                     });
                 }
             });
    }

    @Override
    public void close() {
        this.texture.dispose();
        this.textureIdle.dispose();
        for (final var sound : this.sounds) {
            sound.dispose();
        }
    }

    public record Input(
            PhysicsBody body,
            PlayerHudComponent hud
    ) {}
}
