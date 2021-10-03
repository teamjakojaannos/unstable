package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PosterState;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPosters implements EcsSystem<RenderPosters.Input>, AutoCloseable {
    private final Texture texture;
    private final Texture tilesCafe;
    private final TextureRegion[][] variants;
    private final SpriteBatch spriteBatch;

    private final Sound interact;
    private final Sound interact2;

    public RenderPosters(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("poster.png");
        this.tilesCafe = new Texture("cafe_tiles.png");
        this.variants = new TextureRegion[][]{
                {
                        new TextureRegion(this.texture, 0, 0, 16, 16),
                        new TextureRegion(this.texture, 0, 0, 16, 16),
                },
                {
                        new TextureRegion(this.tilesCafe, 196, 0, 64, 48),
                        new TextureRegion(this.tilesCafe, 196, 48, 64, 48),
                },
                {
                        new TextureRegion(this.tilesCafe, 64, 48, 64, 105),
                        new TextureRegion(this.tilesCafe, 64, 48, 64, 105),
                },
        };

        this.interact = Gdx.audio.newSound(Gdx.files.internal("PaperTurnPage.ogg"));
        this.interact2 = Gdx.audio.newSound(Gdx.files.internal("PaperOpen.ogg"));
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        spriteBatch.begin();
        input.entities()
             .forEach(entity -> {
                 final var body = entity.body;
                 final var poster = entity.poster;

                 if (poster.activeChanged_onlyCallThisFromRendererPls()) {
                     if (poster.active) {
                         this.interact.play(1.0f,
                                            1.0f,
                                            0.0f);
                     } else {
                         this.interact2.play(0.25f,
                                             1.0f,
                                             0.0f);
                     }
                 }

                 final var region = this.variants[poster.type.ordinal()][resources.spoopy ? 1 : 0];
                 final var y = body.getPosition().y - 0.001f;
                 final var width = region.getRegionWidth() / 16.0f + 0.001f;
                 final var height = region.getRegionHeight() / 16.0f + 0.001f;
                 final var x = body.getPosition().x - 0.001f;

                 spriteBatch.draw(region, x, y, width, height);
             });
        spriteBatch.end();
    }

    @Override
    public void close() {
        this.texture.dispose();
        this.tilesCafe.dispose();
        this.interact.dispose();
        this.interact2.dispose();
    }

    public record Input(
            PhysicsBody body,
            PosterState poster
    ) {}
}
