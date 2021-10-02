package fi.jakojaannos.unstable.level;

import fi.jakojaannos.unstable.ecs.Component;

import java.util.ArrayList;
import java.util.List;

public class TileMap implements Component<TileMap> {
    public final List<Tile> tiles;

    public TileMap(List<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    public TileMap cloneComponent() {
        return new TileMap(new ArrayList<>(this.tiles));
    }
}
