package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.PlayerInput;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class DialogueRenderer implements EcsSystem<DialogueRenderer.Input>, AutoCloseable {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Texture pixel;

    public DialogueRenderer(SpriteBatch batch) {
        this.pixel = new Texture("pixel.png");
        this.batch = batch;

        final var fontGen = new FreeTypeFontGenerator(Gdx.files.internal("OldNewspaperTypes.ttf"));
        final var paramRegular = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramRegular.size = 24;
        this.font = fontGen.generateFont(paramRegular);
        fontGen.dispose();
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        final var player = input.entities().findFirst();
        if (player.isEmpty() || resources.getDialogueText() == null) {
            return;
        }

        if (resources.getDialogueText().isEmpty()) {
            resources.setDialogueText(null);
            return;
        }

        final var lines = resources.getDialogueText().get(0);

        this.batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.batch.begin();
        this.batch.setColor(0.0f, 0.0f, 0.0f, 0.85f);
        final var boxW = Gdx.graphics.getWidth();
        final var boxH = 100.0f;
        final var boxX = 0.0f;
        final var boxY = 0.0f;

        this.batch.draw(pixel, boxX, boxY, boxW, boxH);

        this.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        final var lineHeight = this.font.getLineHeight();
        final var xOffs = 20;
        var yOffs = 0;
        for (var line : lines) {
            final var xPos = line.xPos() * boxW + xOffs;
            final var yPos = line.yPos() * boxH + boxH + yOffs - 20;

            this.font.setColor(line.color());
            this.font.draw(
                    this.batch,
                    line.content(),
                    boxX + xPos,
                    yPos,
                    boxW * line.targetWidth(),
                    Align.left,
                    true
            );
            yOffs -= lineHeight;
        }

        this.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.batch.end();

        if (player.map(i -> i.playerInput().actionPressed).orElse(false) && !resources.timers.isActiveAndValid(resources.interactCooldown)) {
            resources.interactCooldown = resources.timers.set(UnstableGame.Constants.INTERACT_COOLDOWN, false, () -> {});
            resources.getDialogueText().remove(0);
        }
    }

    @Override
    public void close() {
        this.font.dispose();
        this.pixel.dispose();
    }

    public record Input(
            PlayerInput playerInput
    ) {
    }
}
