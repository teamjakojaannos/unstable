package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.SoundTags;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.PopUp;

import java.util.List;

public class SmallBedroom {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            // @formatter:on
    };

    public static Room create() {
        return new Room() {
            @Override
            public TileMap createMap() {
                return TileMap.parse(TileSet.MANSION, TILES, WIDTH, HEIGHT);
            }

            @Override
            public Vector2 playerStartPosition() {
                return new Vector2(2.0f, 1.0f);
            }

            @Override
            public void spawnInitialEntities(EcsWorld world, Entity player) {
                world.spawn(Poster.createSofa(new Vector2(5.0f, 1.0f)));

                world.spawn(Poster.createDoor(
                        new Vector2(2.0f, 1.0f),
                        Act2.MANOR_ENTRY,
                        null,
                        new Vector2(ManorEntranceRoom.WIDTH - 12, 1.0f),
                        null
                ));

                world.spawn(Poster.create(
                        new Vector2(10.0f, 1.0f),
                        Poster.Type.PhotoRipped,
                        new PopUp(List.of(), PopUp.Background.Photo),
                        (s, r) -> {
                            r.setDialogueText(List.of(
                                    List.of(new TextRenderer.TextOnScreen("What was that?")),
                                    List.of(new TextRenderer.TextOnScreen("Sounds like a door opened somewhere"))
                            ));

                            r.player.addComponent(new SoundTags.DoorCreak());
                            r.playerInventory.photo = true;
                            s.destroy();

                            return true;
                        },
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("...this photo")),
                                List.of(new TextRenderer.TextOnScreen("I saw it on the newspaper"),
                                        new TextRenderer.TextOnScreen("back in the cafe.")),
                                List.of(new TextRenderer.TextOnScreen("I'm sure of it"))
                        )));
            }
        };
    }
}
