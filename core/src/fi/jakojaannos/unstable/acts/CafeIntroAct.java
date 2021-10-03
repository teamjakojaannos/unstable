package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CafeIntroAct {
    private static final int WIDTH = 17;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_",
            "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2",
            // @formatter:on
    };

    public Collection<EcsSystem> systems() {
        return List.of(
                new PlayerInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new PlayerActionSystem()
        );
    }

    public GameState state() {
        final var gameState = new GameState();

        final var tileMap = TileMap.parse(TileSet.CAFE, TILES, WIDTH, HEIGHT);
        gameState.world()
                 .spawn(Entity.builder()
                              .component(tileMap));

        final var player = gameState
                .world()
                .spawn(Player.create(new Vector2(2.0f, 1.0f)));

        gameState.world().spawn(Closet.create(new Vector2(1.0f, 1.0f), player, HidingSpot.Type.MansionClosetThin));
        gameState.world().spawn(Closet.create(new Vector2(4.0f, 1.0f), player, HidingSpot.Type.Chest));
        gameState.world().spawn(Poster.create(
                new Vector2(8.0f, 2.5f),
                player,
                new PopUp(List.of(new TextRenderer.TextOnScreen("Myydaan potkukelkkoja!\nJa paskoja vihanneksia.\nTerveisin Teslak Aarisaari",
                                                                0.55f,
                                                                0.60f,
                                                                0.5f),
                                  new TextRenderer.TextOnScreen("""
                                                                        Viime yona skotlantilainen juoppo sticky jumppasi pankin holviin ja rajaytti noin 400kg kultaa ja seitseman sentrya.
                                                                                                
                                                                        Han pakeni paikalta traktorilla ja soitti sakkipillia aamunkoittoon asti.""",
                                                                0.05f,
                                                                0.6f,
                                                                0.45f))
                )));

        // borders
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(-1.0f, 1.0f, 1.0f, 2.0f)));

        return gameState;
    }

    public Collection<EcsSystem> renderSystems(final SpriteBatch batch) {
        final var gradientVertexShader = String.format(
                """
                        attribute vec4 %1$s;
                        attribute vec4 %2$s;
                        attribute vec2 %3$s;

                        uniform mat4 u_projTrans;
                        uniform vec2 v_player_pos;
                        varying vec4 v_color;
                        varying vec2 v_texCoords;
                                                        
                        void main() {
                            float y = %1$s.y;
                            float max_y = 1.1;
                            float min_y = 1.0;
                            
                            float remapped = max_y - (y + (-min_y));
                            float t = clamp(remapped / max_y, 0.0, 1.0);
                            
                            vec4 gradient_from = vec4(0, 0, 0, 1.0);
                            vec4 gradient_to = vec4(0, 0, 0, 1.0);
                            vec4 gradient_value = mix(gradient_from, gradient_to, t);
                                                
                            float gradient_factor = y > 1 ? 0.25 : 0.75;

                            v_color = %2$s;
                            v_color.a = v_color.a * (255.0/254.0);
                            v_color = mix(v_color, gradient_value, t * gradient_factor);
                            v_texCoords = %3$s;
                            gl_Position = u_projTrans * %1$s;
                        }
                        """,
                ShaderProgram.POSITION_ATTRIBUTE,
                ShaderProgram.COLOR_ATTRIBUTE,
                ShaderProgram.TEXCOORD_ATTRIBUTE + "0");

        final var gradientFragmentShader = """
                #ifdef GL_ES
                #define LOWP lowp
                precision mediump float;
                #else
                #define LOWP 
                #endif
                varying LOWP vec4 v_color;
                varying vec2 v_texCoords;
                uniform sampler2D u_texture;

                void main() {
                  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
                }
                """;
        return List.of(
                new SetShader(batch, gradientVertexShader, gradientFragmentShader),
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new SetShader(batch, null, null),
                new TextRenderer(batch)
        );
    }
}
