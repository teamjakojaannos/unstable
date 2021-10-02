package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderHidingSpot implements EcsSystem<RenderHidingSpot.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture atlas;
    private final TextureRegion[][] regions;
    private final Sound enterClosetSound;
    private final Sound exitClosetSound;

    public RenderHidingSpot(SpriteBatch batch) {
        this.spriteBatch = batch;
        this.atlas = new Texture("mansion_tiles.png");
        this.regions = new TextureRegion[][]{
                {
                        new TextureRegion(this.atlas, 0, 16, 41, 64),
                        new TextureRegion(this.atlas, 32, 96, 41, 64)
                },
                {
                        new TextureRegion(this.atlas, 48, 16, 31, 64),
                        new TextureRegion(this.atlas, 80, 96, 31, 64)
                },
                {
                        new TextureRegion(this.atlas, 80, 0, 57, 64),
                        new TextureRegion(this.atlas, 80, 0, 57, 64),
                },
        };
        this.enterClosetSound = Gdx.audio.newSound(Gdx.files.internal("Door_Unlock.ogg"));
        this.exitClosetSound = Gdx.audio.newSound(Gdx.files.internal("Footstep_Wood2.ogg"));
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        spriteBatch.begin();
        input.entities()
             .forEach(entity -> {
                 final var body = entity.body();
                 final var hidingSpot = entity.hidingSpot();
                 if (hidingSpot.occupiedChanged_onlyCallThisFromRendererPls()) {
                     if (hidingSpot.occupied) {
                         if (hidingSpot.isCloset()) {
                             this.enterClosetSound.play(0.5f, 1.5f, 0.0f);
                         } else {
                             this.exitClosetSound.play(0.25f, 0.75f, 0.0f);
                         }
                     } else {
                         this.exitClosetSound.play(0.35f, 0.95f, 0.0f);
                     }
                 }

                 final var region = this.regions[hidingSpot.type.ordinal()][hidingSpot.occupied ? 1 : 0];
                 final var x = body.getPosition().x - 0.001f;
                 final var y = body.getPosition().y - 0.001f;
                 final var width = region.getRegionWidth() / 16.0f + 0.001f;
                 final var height = region.getRegionHeight() / 16.0f + 0.001f;

                 spriteBatch.draw(region, x, y, width, height);
             });
        spriteBatch.end();
    }

    @Override
    public void close() {
        this.atlas.dispose();
        this.enterClosetSound.dispose();
        this.exitClosetSound.dispose();
    }

    public record Input(
            PhysicsBody body,
            HidingSpot hidingSpot
    ) {}
}
