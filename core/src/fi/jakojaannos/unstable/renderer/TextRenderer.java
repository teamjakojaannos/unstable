package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import fi.jakojaannos.unstable.components.PlayerInput;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.ArrayList;
import java.util.List;

public class TextRenderer implements EcsSystem<TextRenderer.Input>, AutoCloseable {
    public final List<TextOnScreen> lines = new ArrayList<>();
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture newspaper;
    private final Texture pixel;
    private final Color orange = new Color(0xA06345FF);

    public TextRenderer(SpriteBatch batch) {
        this.newspaper = new Texture("newspaper.png");
        this.pixel = new Texture("pixel.png");

        this.batch = batch;

        final var fontGen = new FreeTypeFontGenerator(Gdx.files.internal("Beyond_Wonderland.ttf"));
        final var paramRegular = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramRegular.size = 24;
        this.font = fontGen.generateFont(paramRegular);

        fontGen.dispose();

        lines.add(new TextOnScreen("Myydaan potkukelkkoja!\nJa paskoja vihanneksia.\nTerveisin Teslak Aarisaari", 350.0f, 290.0f));

        lines.add(new TextOnScreen(
                """
                        Viime yona skotlantilainen
                        sticky jumppasi pankin holviin ja
                        rajaytti noin 400kg kultaa ja
                        seitseman sentrya.
                                                
                        Han pakeni paikalta traktorilla ja
                        soitti sakkipillia aamunkoittoon asti.""",
                5.0f, 300.0f));
        lines.add(new TextOnScreen("juoppo", 260.0f, 300.0f, new Color(0x5B2400ff)));


    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        final var player = input.entities().findFirst();
        if (player.isEmpty() || !player.get().playerInput.newspaperPressed) {
            return;
        }

        this.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.batch.begin();


        this.batch.setColor(this.orange);
        this.batch.draw(pixel,
                0.0f,
                0.0f,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );

        //this.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batch.draw(
                this.newspaper,
                0.0f,
                0.0f,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );


        this.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        for (var line : this.lines) {
            this.font.setColor(line.color);
            this.font.draw(this.batch, line.content, line.xPos, line.yPos);
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
            Color color
    ) {
        public TextOnScreen(
                String content,
                float xPos,
                float yPos
        ) {
            this(content, xPos, yPos, new Color(0xffffffff));
        }
    }
}
