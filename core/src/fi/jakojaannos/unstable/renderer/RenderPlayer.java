package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.Hiding;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;
import java.util.Random;

public class RenderPlayer implements EcsSystem<RenderPlayer.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture texture;
    private final Texture textureIdle;
    private final TextureRegion[] frames;
    private final TextureRegion[] idleFrames;
    private final TextureRegion[] hidingFrames;
    private final TextureRegion[] closetIconFrames;
    private final Texture closetIndicator;

    private final Random random;

    private final Sound[] sounds;

    public RenderPlayer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("Journalist.png");
        this.textureIdle = new Texture("Journalist_idle.png");
        this.frames = new TextureRegion[8];
        this.hidingFrames = new TextureRegion[8];

        final var frameWidth = this.texture.getWidth() / this.frames.length;
        final var frameHeight = this.texture.getHeight() / 2;
        for (int frameIndex = 0; frameIndex < this.frames.length; frameIndex++) {
            this.frames[frameIndex] = new TextureRegion(this.texture, frameWidth * frameIndex, 0, frameWidth, frameHeight);
            this.hidingFrames[frameIndex] = new TextureRegion(this.texture, frameWidth * frameIndex, frameHeight, frameWidth, frameHeight);
        }

        this.idleFrames = new TextureRegion[]{
                new TextureRegion(this.textureIdle)
        };

        this.closetIndicator = new Texture("hide_icons.png");
        this.closetIconFrames = new TextureRegion[]{
                new TextureRegion(this.closetIndicator, 0, 0, 16, 16),
                new TextureRegion(this.closetIndicator, 16, 0, 16, 16),
                new TextureRegion(this.closetIndicator, 32, 0, 16, 16),
        };


        this.sounds = new Sound[]{
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood1.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood2.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood3.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood4.ogg")),
        };

        this.random = new Random();
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
                         : entity.hidingTag.isPresent()
                         ? this.hidingFrames
                         : this.idleFrames;

                 final var loopDuration = 1.25;

                 final var scaledTick = ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / framesToUse.length);
                 final var region = framesToUse[((int) scaledTick) % framesToUse.length];
                 region.flip(region.isFlipX() == physics.facingRight, false);

                 this.spriteBatch.draw(region, x, y, originX, originY, width, height, 1.0f, 1.0f, 0.0f);

                 final var displayIcon = switch (entity.hud.currentIndicator) {
                     case CLOSET -> entity.hidingTag.isPresent() ? 0 : 1;
                     case QUESTION -> 2;
                     default -> -1;
                 };

                 if (displayIcon != -1) {
                     final var iconSize = 0.5f;
                     this.spriteBatch.draw(
                             this.closetIconFrames[displayIcon],
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
                         final var sound = this.sounds[random.nextInt(this.sounds.length)];
                         final var volume = 0.75f;
                         sound.play(volume, random.nextFloat(0.85f, 1.15f), 0.0f);
                     });
                 }
             });
    }

    @Override
    public void close() {
        this.texture.dispose();
        this.textureIdle.dispose();
        this.closetIndicator.dispose();

        for (final var sound : this.sounds) {
            sound.dispose();
        }
    }

    public record Input(
            PhysicsBody body,
            PlayerHudComponent hud,
            Optional<Hiding> hidingTag
    ) {}
}
