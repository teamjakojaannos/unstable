package fi.jakojaannos.unstable.acts.act1;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act2.Act2;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.List;

public class SomeRoom2 {
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
            public void spawnInitialEntities(
                    EcsWorld world,
                    Resources res,
                    Entity player
            ) {
                world.spawn(Closet.create(new Vector2(1.0f, 1.0f), player, HidingSpot.Type.MansionClosetLarge));

                world.spawn(Poster.createDoor(new Vector2(WIDTH - 12, 1.0f),
                                              Act2.MIRROR_ROOM,
                                              new Act2(),
                                              null,
                                              (s, r) -> r.playerInventory.photo));
                world.spawn(Poster.createDoor(new Vector2(WIDTH - 6, 1.0f), Act1.SMALL_BEDROOM, null));

                world.spawn(Poster.create(
                        new Vector2(WIDTH - 8, 1.0f),
                        Poster.Type.POSTER,
                        new PopUp(List.of(), PopUp.Background.Note3),
                        (s, r) -> true,
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("This one rings a bell...")),
                                List.of(new TextRenderer.TextOnScreen("I remember one of my friends pulling off"),
                                        new TextRenderer.TextOnScreen("a similar prank on me some time."))
                        )));
            }
        };
    }
}
