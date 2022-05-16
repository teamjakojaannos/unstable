package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import fi.jakojaannos.unstable.InputManager;
import fi.jakojaannos.unstable.components.PlayerInput;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.List;
import java.util.Optional;

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
                new Texture("Painting_Zero.png"),
                new Texture("taulu_iso.png"),
                new Texture("taulu2_iso.png"),
                new Texture("Riddle_Note.png"),

                new Texture("Note_1.png"),
                new Texture("Note_2.png"),
                new Texture("Note_3.png"),
                new Texture("Note_4.png"),
                new Texture("Note_5.png"),

                new Texture("Scottish_Times_Article_1.png"),
                new Texture("Scottish_Times_Article_2.png"),
                new Texture("Scottish_Times_Article_3.png"),

                new Texture("Medicalrebort_big.png"),
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
        if (player.flatMap(p -> p.numlockTag).isPresent()) {
            if (resources.numlock) {
                tickNumberLock(resources);
            }
            return;
        }

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

    private void tickNumberLock(Resources resources) {
        if (resources.combinationSolved) {
            return;
        }

        final var key0 = resources.playerInput.getState(InputManager.KeyInput.Key0);
        final var key1 = resources.playerInput.getState(InputManager.KeyInput.Key1);
        final var key2 = resources.playerInput.getState(InputManager.KeyInput.Key2);
        final var key3 = resources.playerInput.getState(InputManager.KeyInput.Key3);
        final var key4 = resources.playerInput.getState(InputManager.KeyInput.Key4);
        final var key5 = resources.playerInput.getState(InputManager.KeyInput.Key5);
        final var key6 = resources.playerInput.getState(InputManager.KeyInput.Key6);
        final var key7 = resources.playerInput.getState(InputManager.KeyInput.Key7);
        final var key8 = resources.playerInput.getState(InputManager.KeyInput.Key8);
        final var key9 = resources.playerInput.getState(InputManager.KeyInput.Key9);

        final var justPressed0 = key0 == InputManager.State.PRESSED;
        final var justPressed1 = key1 == InputManager.State.PRESSED;
        final var justPressed2 = key2 == InputManager.State.PRESSED;
        final var justPressed3 = key3 == InputManager.State.PRESSED;
        final var justPressed4 = key4 == InputManager.State.PRESSED;
        final var justPressed5 = key5 == InputManager.State.PRESSED;
        final var justPressed6 = key6 == InputManager.State.PRESSED;
        final var justPressed7 = key7 == InputManager.State.PRESSED;
        final var justPressed8 = key8 == InputManager.State.PRESSED;
        final var justPressed9 = key9 == InputManager.State.PRESSED;

        final var anyPressed = justPressed0 ||
                justPressed1 ||
                justPressed2 ||
                justPressed3 ||
                justPressed4 ||
                justPressed5 ||
                justPressed6 ||
                justPressed7 ||
                justPressed8 ||
                justPressed9;

        if (resources.enteringNumber == -1) {
            resources.setDialogueText(List.of(List.of(new TextOnScreen("I need a three digit combination..."),
                                                      new TextOnScreen("(use number keys 0-9)"))));
        } else if (resources.enteringNumber == 0) {
            resources.setDialogueText(List.of(List.of(new TextOnScreen("Alright, next one is..."),
                                                      new TextOnScreen("(use number keys 0-9)"))));
        } else if (resources.enteringNumber == 1) {
            resources.setDialogueText(List.of(List.of(new TextOnScreen("And the last one..."),
                                                      new TextOnScreen("(use number keys 0-9)"))));
        }

        if (anyPressed && !resources.isInteractOnCooldown()) {
            resources.setInteractCooldown();

            resources.enteringNumber++;
            resources.numbers[resources.enteringNumber] = justPressed0
                    ? 0
                    : justPressed1
                    ? 1
                    : justPressed2
                    ? 2
                    : justPressed3
                    ? 3
                    : justPressed4
                    ? 4
                    : justPressed5
                    ? 5
                    : justPressed6
                    ? 6
                    : justPressed7
                    ? 7
                    : justPressed8
                    ? 8
                    : 9;

            if (resources.enteringNumber == 2) {
                if (resources.numbers[0] == 4 && resources.numbers[1] == 6 && resources.numbers[2] == 0) {
                    resources.combinationSolved = true;
                } else {
                    resources.stormy = true;
                }

                resources.enteringNumber = -1;
                resources.popup = null;
                resources.setDialogueText(null);
                if (resources.player.hasComponent(Tags.FreezeInput.class)) {
                    resources.player.removeComponent(Tags.FreezeInput.class);
                }
                if (resources.player.hasComponent(Tags.Numlock.class)) {
                    resources.player.removeComponent(Tags.Numlock.class);
                }
            }
        }
    }

    @Override
    public void close() {
        this.font.dispose();
        for (Texture background : this.backgrounds) {
            background.dispose();
        }
    }

    public record Input(
            PlayerInput playerInput,
            Optional<Tags.Numlock> numlockTag
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

        public TextOnScreen(String content) {
            this(content, 0, 0, 1.0f);
        }
    }
}
