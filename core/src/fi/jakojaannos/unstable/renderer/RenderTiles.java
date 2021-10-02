package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderTiles implements EcsSystem<RenderTiles.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final Texture tileset;
    private final TextureRegion[] tileRegions;

    public RenderTiles(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.tileset = new Texture("mansion_tiles.png");

        final var tilesX = this.tileset.getWidth() / 16;
        final var tilesY = this.tileset.getHeight() / 16;
        this.tileRegions = new TextureRegion[tilesX * tilesY];
        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                final var index = tilesY * y + x;
                this.tileRegions[index] = new TextureRegion(this.tileset, x * 16, y * 16, 16, 16);
            }
        }
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        input.entities()
             .forEach(entity -> {
                 final var tileMap = entity.tileMap();
                 tileMap.tiles.forEach(tile -> {
                     final var region = this.tileRegions[tile.id()];
                     final var x = tile.x() - 0.001f;
                     final var y = tile.y() - 0.001f;
                     final var width = 1.002f;
                     final var height = 1.002f;

                     spriteBatch.begin();
                     spriteBatch.draw(region, x, y, width, height);
                     spriteBatch.end();
                 });
             });
    }

    @Override
    public void close() {
        this.tileset.dispose();
    }

    public record Input(
            TileMap tileMap
    ) {}
}