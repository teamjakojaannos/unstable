package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Random;

public class RenderTiles implements EcsSystem<RenderTiles.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture[] tileset;
    private final TextureRegion[][] tileRegions;
    private final long seed;
    private final Random random = new Random();

    public RenderTiles(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.tileset = new Texture[]{
                new Texture("mansion_tiles.png"),
                new Texture("cafe_tiles.png"),
        };

        final var tilesX = 16;
        final var tilesY = 16;
        this.tileRegions = new TextureRegion[2][];
        for (int renderId = 0; renderId < this.tileset.length; renderId++) {
            this.tileRegions[renderId] = new TextureRegion[tilesX * tilesY];
            for (int x = 0; x < tilesX; x++) {
                for (int y = 0; y < tilesY; y++) {
                    final var index = tilesY * y + x;
                    this.tileRegions[renderId][index] = new TextureRegion(this.tileset[renderId], x * 16, y * 16, 16, 16);
                }
            }
        }
        seed = this.random.nextLong();
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        spriteBatch.begin();
        input.entities()
             .forEach(entity -> {
                 final var tileMap = entity.tileMap();
                 final var tileSet = tileMap.tileSet();
                 tileMap.tiles().forEach(tile -> {
                     final var region = this.tileRegions[tileSet.renderId()][tile.id()];
                     final var x = tile.x() - 0.001f;
                     final var y = tile.y() - 0.001f;
                     final var width = 1.002f;
                     final var height = 1.002f;

                     spriteBatch.draw(region, x, y, width, height);

                     if (tile.isWall()) {
                         final var hash = ((long) x) + ((long) y << 32);
                         this.random.setSeed(hash + this.seed);

                         final var variantIds = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 2, -1, -1, -1,};
                         final var id = variantIds[this.random.nextInt(variantIds.length )];

                         if (id != -1) {
                             final var decorRegion = this.tileRegions[tileSet.renderId()][id];
                             spriteBatch.draw(decorRegion, x, y, width, height);
                         }
                     }
                 });
             });
        spriteBatch.end();
    }

    @Override
    public void close() {
        for (final var tiles : this.tileset) {
            tiles.dispose();
        }
    }

    public record Input(
            TileMap tileMap
    ) {}
}
