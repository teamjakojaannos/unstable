package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PosterState;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderPosters implements EcsSystem<RenderPosters.Input>, AutoCloseable {
    private final Texture texture;
    private final Texture tilesCafe;
    private final Texture tilesMansion;
    private final Texture Indoordoor;
    private final Texture furnace;
    private final Texture hammer;
    private final Texture haarniska;
    private final Texture maaliA;
    private final Texture maaliB;
    private final Texture maaliC;
    private final TextureRegion[][] variants;
    private final SpriteBatch spriteBatch;

    private final Sound interact;
    private final Sound interact2;
    private final Texture kaappi;
    private final Texture kaappiNurin;
    private final Texture tohtoriDed;
    private final Texture tohtori;
    private final Texture statuetable;
    private final Texture medrep;
    private final Texture npcMies;
    private final Texture npcMies2;
    private final Texture npcNaine;
    private final Texture npcNaine2;
    private final Texture kynttila;
    private final Texture kynttila2;
    private final Texture poyta;

    public RenderPosters(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.texture = new Texture("poster.png");
        this.tilesCafe = new Texture("cafe_tiles.png");
        this.tilesMansion = new Texture("mansion_tiles.png");
        this.Indoordoor = new Texture("Indoordoor.png");
        this.furnace = new Texture("Furnace_Sheet.png");
        this.hammer = new Texture("Hammer.png");
        this.haarniska = new Texture("haarniska.png");
        this.kaappi = new Texture("Kaappi.png");
        this.kaappiNurin = new Texture("KaappiKaatunut.png");
        this.maaliA = new Texture("Painting_Zero_Small.png");
        this.maaliB = new Texture("taulu_pieni.png");
        this.maaliC = new Texture("taulu2_pieni.png");
        this.tohtori = new Texture("doctor_sit.png");
        this.tohtoriDed = new Texture("doctor_ded.png");
        this.statuetable = new Texture("rintakuvaP??yt??Spooky.png");
        this.medrep = new Texture("medicareport_SMALLL.png");
        this.npcMies = new Texture("npcmies1.png");
        this.npcMies2 = new Texture("npcmies2.png");
        this.npcNaine = new Texture("npcnaine1.png");
        this.npcNaine2 = new Texture("npcnaine2.png");
        this.kynttila = new Texture("JalakasKynttil??.png");
        this.kynttila2 = new Texture("Sein??Kynttil??t.png");
        this.poyta = new Texture("P??yt??.png");

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
                        new TextureRegion(this.tilesCafe, 0, 80, 64, 73),
                        new TextureRegion(this.tilesCafe, 0, 80, 64, 73),
                },
                {
                        new TextureRegion(this.tilesCafe, 0, 0, 128, 48),
                        new TextureRegion(this.tilesCafe, 0, 0, 128, 48),
                },
                {
                        new TextureRegion(this.texture, 16, 0, 16, 16),
                        new TextureRegion(this.texture, 16, 0, 16, 16),
                },
                {
                        new TextureRegion(this.Indoordoor, 0, 0, 32, 71),
                        new TextureRegion(this.Indoordoor, 32, 0, 32, 71),
                },
                {
                        new TextureRegion(this.furnace, 0, 0, 77, 128),
                        new TextureRegion(this.furnace, 77, 0, 77, 128),
                        new TextureRegion(this.furnace, 77 * 2, 0, 77, 128),
                        new TextureRegion(this.furnace, 77 * 3, 0, 77, 128),
                        new TextureRegion(this.furnace, 77 * 4, 0, 77, 128),
                },
                {
                        new TextureRegion(this.tilesMansion, 112, 96, 62, 32),
                        new TextureRegion(this.tilesMansion, 112, 96, 62, 32),
                },
                {
                        new TextureRegion(this.texture, 32, 0, 16, 16),
                        new TextureRegion(this.texture, 32, 0, 16, 16),
                },
                {
                        new TextureRegion(this.texture, 48, 0, 16, 16),
                        new TextureRegion(this.texture, 48, 0, 16, 16),
                },
                {
                        new TextureRegion(this.hammer),
                        new TextureRegion(this.hammer),
                },
                {
                        new TextureRegion(this.haarniska, 0, 0, 32, 64),
                        new TextureRegion(this.haarniska, 32, 0, 32, 64),
                },
                {
                        new TextureRegion(this.kaappiNurin),
                        new TextureRegion(this.kaappiNurin),
                },
                {
                        new TextureRegion(this.kaappi),
                        new TextureRegion(this.kaappi),
                },
                {
                        new TextureRegion(this.maaliA),
                        new TextureRegion(this.maaliA),
                },
                {
                        new TextureRegion(this.maaliB),
                        new TextureRegion(this.maaliB),
                },
                {
                        new TextureRegion(this.maaliC),
                        new TextureRegion(this.maaliC),
                },
                {
                        new TextureRegion(this.tohtoriDed),
                        new TextureRegion(this.tohtori),
                },
                {
                        new TextureRegion(this.statuetable),
                        new TextureRegion(this.statuetable),
                },
                {
                        new TextureRegion(this.medrep),
                        new TextureRegion(this.medrep),
                },
                {
                        new TextureRegion(this.tilesCafe, 128, 0, 32, 24),
                        new TextureRegion(this.tilesCafe, 128, 0, 32, 24),
                },
                {
                        new TextureRegion(this.npcMies, 0, 0, 32, 48),
                        new TextureRegion(this.npcMies, 32, 0, 32, 48),
                },
                {
                        new TextureRegion(this.npcMies2, 32, 0, -32, 48),
                        new TextureRegion(this.npcMies2, 64, 0, -32, 48),
                },
                {
                        new TextureRegion(this.tilesCafe, 128, 24, 32, 72),
                        new TextureRegion(this.tilesCafe, 128, 24, 32, 72),
                },
                {
                        new TextureRegion(this.npcNaine, 0, 0, 32, 48),
                        new TextureRegion(this.npcNaine, 32, 0, 32, 48),
                },
                {
                        new TextureRegion(this.npcNaine2, 32, 0, -32, 48),
                        new TextureRegion(this.npcNaine2, 64, 0, -32, 48),
                },
                {
                        new TextureRegion(this.kynttila, 0, 0, 29, 63),
                        new TextureRegion(this.kynttila, 29, 0, 29, 63),
                },
                {
                        new TextureRegion(this.kynttila2, 0, 0, 22, 24),
                        new TextureRegion(this.kynttila2, 22, 0, 22, 24),
                },
                {
                        new TextureRegion(this.poyta),
                        new TextureRegion(this.poyta),
                },
                {
                        new TextureRegion(this.tilesCafe, 0, 0, 1, 1),
                        new TextureRegion(this.tilesCafe, 0, 0, 1, 1),
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

                 final var isAnimated = switch (poster.type) {
                     case Furnace, NpcMIES, NpcMIES2, NpcNAINE, NpcNAINE2, JalkaKynttila, SeinaKynttila -> true;
                     default -> false;
                 };

                 final var isDoor = switch (poster.type) {
                     case Indoordoor -> true;
                     default -> false;
                 };

                 TextureRegion region;
                 if (isAnimated) {
                     final var tick = resources.timeManager.currentTick();
                     final var framesToUse = this.variants[poster.type.ordinal()];

                     final var loopDuration = switch (poster.type) {
                         case Furnace -> 2.5f;
                         case NpcMIES, NpcNAINE -> 0.5f;
                         case NpcMIES2, NpcNAINE2 -> 2.25f;
                         default -> 1.0f;
                     };

                     final var scaledTick = ((float) tick / (float) UnstableGame.Constants.GameLoop.TICKS_PER_SECOND) / (loopDuration / framesToUse.length);
                     region = framesToUse[((int) scaledTick) % framesToUse.length];
                 } else if (isDoor) {
                     final var isLocked = entity.handle
                             .getComponent(Interactable.class)
                             .map(i -> i.action.condition(entity.handle, resources))
                             .orElse(false);

                     region = this.variants[poster.type.ordinal()][isLocked ? 0 : 1];
                 } else {
                     region = this.variants[poster.type.ordinal()][resources.spoopy ? 1 : 0];
                 }

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
        this.tilesMansion.dispose();
        this.Indoordoor.dispose();
        this.furnace.dispose();
        this.hammer.dispose();
        this.haarniska.dispose();
    }

    public record Input(
            Entity handle,
            PhysicsBody body,
            PosterState poster
    ) {}
}
