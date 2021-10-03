package fi.jakojaannos.unstable.level;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class TileSet {
    public static final TileSet MANSION;
    public static final TileSet CAFE;

    static {
        MANSION = new TileSet(0, 16, 16);
        MANSION.addTile("f", false, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95);
        MANSION.addTile("w", true, 0);
        MANSION.addTile("w3", false, 27);
        MANSION.addTile("w_", false, 11, 11, 11, 11, 11, 11, 11, 11, 12);

        MANSION.addProp("hole", false, 5, 0, 4, 4);
        MANSION.addProp("tapetti", true, 0, 6, 2, 8);
        MANSION.addProp("closet_01", false, 0, 1, 3, 4);
        MANSION.addProp("closet_02", false, 3, 1, 2, 4);

        CAFE = new TileSet(1, 16, 16);
        CAFE.addTile("f", false, 48);
        CAFE.addTile("w1", false, 49);
        CAFE.addTile("w2", false, 50);
        CAFE.addTile("w_", false, 51);
        CAFE.addTile("w+", false, 67);

    }

    private final Map<String, Integer[]> variantIds = new HashMap<>();
    private final Map<String, Boolean> wallIds = new HashMap<>();
    private final Map<String, Prop> props = new HashMap<>();
    private final int tilesetWidth;
    private final int tilesetHeight;
    private final int renderId;
    private final Random random;

    private TileSet(int renderId, int tilesetWidth, int tilesetHeight) {
        this.renderId = renderId;
        this.tilesetWidth = tilesetWidth;
        this.tilesetHeight = tilesetHeight;
        this.random = new Random();
    }

    public int renderId() {
        return renderId;
    }

    public void addTile(String name, boolean allowWallDecor, Integer... variants) {
        this.variantIds.put(name, variants);
        this.wallIds.put(name, allowWallDecor);
    }

    public void addProp(String name, boolean allowWallDecor, int x, int y, int width, int height) {
        props.put(name, new Prop(x, y, width, height));
        this.wallIds.put(name, allowWallDecor);
    }

    public Optional<Integer> getTile(String name, int x, int y) {
        if (!this.variantIds.containsKey(name)) {
            return Optional.empty();
        }

        final var ids = this.variantIds.get(name);
        final var hash = ((long) x) + ((long) y << 32);
        this.random.setSeed(hash);

        return Optional.of(ids[this.random.nextInt(0, ids.length)]);
    }

    public Tile[] getProp(String name, int x, int y, boolean isWall) {
        final var prop = props.get(name);

        final var tiles = new Tile[prop.w * prop.h];
        for (int ix = 0; ix < prop.w; ix++) {
            for (int iy = 0; iy < prop.h; iy++) {
                final var index = iy * prop.w + ix;
                final var column = prop.x + ix;
                final var row = prop.y + iy;

                final var id = row * tilesetWidth + column % tilesetWidth;
                tiles[index] = new Tile(id, x + ix, y + prop.h - (iy + 1), isWall);
            }
        }

        return tiles;
    }

    public boolean allowWallDecor(String id) {
        return this.wallIds.getOrDefault(id, false);
    }

    private static record Prop(int x, int y, int w, int h) {}
}
