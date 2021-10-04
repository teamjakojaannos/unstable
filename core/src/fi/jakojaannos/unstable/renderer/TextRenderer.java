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

public class TextRenderer implements EcsSystem<TextRenderer.Input>, AutoCloseable {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture[] backgrounds;
    private final Texture pixel;
    private final Color orange = new Color(0xA06345FF);

    public TextRenderer(SpriteBatch batch) {
        this.backgrounds = new Texture[]{
                new Texture("newspaper.png"),
                new Texture("Kuva_Hajonnut.png"),
        };
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
        final var bgType = resources.popup.photo();
        final var bg = this.backgrounds[bgType.ordinal()];


        this.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.batch.begin();

        final var ratio = ((float) bg.getWidth()) / bg.getHeight();

        final var vOffs = (Gdx.graphics.getHeight() * (1.0f - bgType.height())) / 2.0f;

        final var newspaperHeight = Gdx.graphics.getHeight() * bgType.height();
        final var newspaperWidth = newspaperHeight * ratio;

        final var excessSpace = Gdx.graphics.getWidth() - newspaperWidth;
        final var newspaperX = excessSpace / 2.0f;

        if (bgType.needsPixel()) {
            this.batch.setColor(this.orange);
            this.batch.draw(pixel,
                            newspaperX,
                            vOffs,
                            newspaperWidth,
                            newspaperHeight
            );
        }


        this.batch.draw(
                bg,
                newspaperX,
                vOffs,
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
        for (Texture background : this.backgrounds) {
            background.dispose();
        }
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
