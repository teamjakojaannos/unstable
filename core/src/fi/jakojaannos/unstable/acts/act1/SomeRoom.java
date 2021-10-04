package fi.jakojaannos.unstable.acts.act1;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.List;

public class SomeRoom {
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
            public int width() {
                return WIDTH;
            }
            
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
                world.spawn(Poster.create(
                        new Vector2(10.0f, 2.0f),
                        Poster.Type.POSTER,
                        new PopUp(List.of(), PopUp.Background.Note1),
                        (s, r) -> true,
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("Huh. A note."),
                                        new TextRenderer.TextOnScreen("Looks a bit like my own handwriting"))
                        )));

                world.spawn(Poster.createDoor(
                        new Vector2(12.0f, 1.0f),
                        Act1.NURSE_HALLWAY,
                        null,
                        new Vector2(2.0f, 1.0f),
                        null
                ));

                world.spawn(Poster.create(
                        new Vector2(6.0f, 1.0f),
                        Poster.Type.Haarniska,
                        null,
                        (s, r) -> {
                            r.setDialogueText(List.of(
                                    List.of(new TextRenderer.TextOnScreen("A medieval armor?")),
                                    List.of(new TextRenderer.TextOnScreen("Uh, just seeing it makes me feel chilly."),
                                            new TextRenderer.TextOnScreen("Why does it look oddly familiar, too?")),
                                    List.of(new TextRenderer.TextOnScreen("...")),
                                    List.of(new TextRenderer.TextOnScreen("I must be going crazy"))
                            ));
                            s.removeComponent(Interactable.class);

                            return true;
                        }));
            }
        };
    }
}
