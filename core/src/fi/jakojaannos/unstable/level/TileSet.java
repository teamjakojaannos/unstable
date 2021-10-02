package fi.jakojaannos.unstable.level;

import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.Map;

public class TileSet {
    private final Map<String, Integer[]> variantIds = new HashMap<>();
    private final Map<String, Prop> props = new HashMap<>();

    private final int tilesetWidth;
    private final int tilesetHeight;

    public TileSet(int tilesetWidth, int tilesetHeight) {
        this.tilesetWidth = tilesetWidth;
        this.tilesetHeight = tilesetHeight;
    }

    public void addTile(String name, Integer... variants) {
        variantIds.put(name, variants);
    }

    public void addProp(String name, int x, int y, int width, int height) {
        props.put(name, new Prop(x, y, width, height));
    }

    public int getTile(String name, int x, int y) {
        final var hash = ((long) x) + ((long) y << 32);
        MathUtils.random.setSeed(hash);

        final var ids = this.variantIds.get(name);
        return ids[MathUtils.random(0, ids.length - 1)];
    }

    public Tile[] getProp(String name, int x, int y) {
        final var prop = props.get(name);

        final var tiles = new Tile[prop.w * prop.h];
        for (int ix = 0; ix < prop.w; ix++) {
            for (int iy = 0; iy < prop.h; iy++) {
                final var index = iy * prop.w + ix;
                final var column = prop.x + ix;
                final var row = prop.y + iy;

                final var id = row * tilesetWidth + column % tilesetWidth;
                tiles[index] = new Tile(id, x + ix, y + prop.h - (iy + 1));
            }
        }

        return tiles;
    }

    private static record Prop(int x, int y, int w, int h) {}
}
