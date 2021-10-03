package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import fi.jakojaannos.unstable.components.PlayerInput;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.ArrayList;
import java.util.List;

public class TextRenderer implements EcsSystem<TextRenderer.Input>, AutoCloseable {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture newspaper;
    private final Texture pixel;
    private final Color orange = new Color(0xA06345FF);

    public TextRenderer(SpriteBatch batch) {
        this.newspaper = new Texture("newspaper.png");
        this.pixel = new Texture("pixel.png");

        this.batch = batch;

        final var fontGen = new FreeTypeFontGenerator(Gdx.files.internal("OldNewspaperTypes.ttf"));
        final var paramRegular = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramRegular.size = 16;
        this.font = fontGen.generateFont(paramRegular);
        fontGen.dispose();
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        final var player = input.entities().findFirst();
        if (player.isEmpty() || resources.popup == null) {
            return;
        }

        final var lines = resources.popup.lines();

        this.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.batch.begin();

        final var ratio = ((float) newspaper.getWidth()) / newspaper.getHeight();
        final var newspaperHeight = Gdx.graphics.getHeight();
        final var newspaperWidth = newspaperHeight * ratio;

        final var excessSpace = Gdx.graphics.getWidth() - newspaperWidth;
        final var newspaperX = excessSpace / 2.0f;

        this.batch.setColor(this.orange);
        this.batch.draw(pixel,
                        newspaperX,
                        0.0f,
                        newspaperWidth,
                        newspaperHeight
        );

        this.batch.draw(
                this.newspaper,
                newspaperX,
                0.0f,
                newspaperWidth,
                newspaperHeight
        );


        this.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        for (var line : lines) {
            final var xPos = line.xPos * newspaperWidth;
            final var yPos = line.yPos * newspaperHeight;

            this.font.setColor(line.color);
            this.font.draw(
                    this.batch,
                    line.content,
                    newspaperX + xPos,
                    yPos,
                    newspaperWidth * line.targetWidth(),
                    Align.left,
                    true
            );
        }

        this.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batch.end();

    }

    @Override
    public void close() {
        this.font.dispose();
    }

    public record Input(
            PlayerInput playerInput
    ) {
    }

    public record TextOnScreen(
            String content,
            float xPos,
            float yPos,
            float targetWidth,
            Color color
    ) {
        public TextOnScreen(
                String content,
                float xPos,
                float yPos,
                float targetWidth
        ) {
            this(content, xPos, yPos, targetWidth, new Color(0xffffffff));
        }
    }
}
