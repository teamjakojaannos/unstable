package fi.jakojaannos.unstable.level;

import fi.jakojaannos.unstable.ecs.Component;

import java.util.ArrayList;
import java.util.List;

public record TileMap(List<Tile> tiles, TileSet tileSet) implements Component<TileMap> {
    @Override
    public TileMap cloneComponent() {
        return new TileMap(new ArrayList<>(this.tiles), this.tileSet);
    }

    public static TileMap parse(TileSet tileset, String[] src, int WIDTH, int HEIGHT) {
        final var filledByProps = new ArrayList<Tile>();
        final var tiles = new ArrayList<Tile>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int finalX = x;
                int finalY = y;
                if (filledByProps.stream().anyMatch(tile -> tile.x() == finalX && tile.y() == finalY)) {
                    continue;
                }

                final var index = y * WIDTH + x;
                final var id = src[index];

                final var tile = tileset.getTile(id, x, y);
                final var isWall = tileset.allowWallDecor(id);
                if (tile.isPresent()) {
                    tiles.add(new Tile(tile.get(), x, y, isWall));
                } else {
                    final var prop = List.of(tileset.getProp(id, x, y, isWall));
                    filledByProps.addAll(prop);
                    tiles.addAll(prop);
                }

            }
        }

        return new TileMap(tiles, tileset);
    }
}
