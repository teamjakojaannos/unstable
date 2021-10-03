package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.acts.act1.Rooms;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CafeIntroAct {
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
        final var room = Rooms.CAFE;

        final var tileMap = room.createMap();
        gameState.world().spawn(Entity.builder().component(tileMap));

        final var playerPosition = room.playerStartPosition();
        final var player = gameState.world().spawn(Player.create(playerPosition));

        room.spawnInitialEntities(gameState.world(), player);

        return gameState;
    }

    public Collection<EcsSystem> renderSystems(final SpriteBatch batch) {
        final var gradientVertexShader = String.format(
                """
                        attribute vec4 %1$s;
                        attribute vec4 %2$s;
                        attribute vec2 %3$s;

                        uniform mat4 u_projTrans;
                        uniform vec4 u_overlay_color;
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
                            
                            vec4 overlay = mix(vec4(0, 0, 0, 1), vec4(u_overlay_color.rgb, 1.0), u_overlay_color.a);
                            v_color.rgb += overlay.rgb;
                            
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
                uniform vec2 u_screenSize;
                uniform vec2 u_bgSize;
                uniform vec2 u_playerPos;
                uniform int u_debug_bg;
                uniform sampler2D u_texture;
                uniform sampler2D u_bg_texture;
                uniform sampler2D u_bg_texture2;
                uniform sampler2D u_bg_texture3;
                                
                uniform vec4 u_overlay_color;

                void main() {
                    vec4 tex_sample = texture2D(u_texture, v_texCoords);
                    if (tex_sample.rgb == vec3(1.0, 0.0, 1.0) || u_debug_bg == 1) {
                        float bg_scale = 3.0;
                        float parallax_move_scale = 4.0;
                        float parallax_move_scale2 = 1.0;
                        float parallax_move_scale3 = 0.0;

                        vec2 scaled_player_pos = u_playerPos * parallax_move_scale;
                        vec2 scaled_player_pos2 = u_playerPos * parallax_move_scale2;
                        vec2 scaled_player_pos3 = u_playerPos * parallax_move_scale3;
                        vec2 offset = vec2(scaled_player_pos.x / u_screenSize.x, 0.8);
                        vec2 offset2 = vec2(scaled_player_pos2.x / u_screenSize.x, 0.8);
                        vec2 offset3 = vec2(scaled_player_pos3.x / u_screenSize.x, 0.8);
                        
                        float bg_aspect = u_bgSize.y / u_bgSize.x;
                        
                        float u = gl_FragCoord.x * bg_aspect;
                        float v = gl_FragCoord.y;
                        
                        vec2 projected = vec2(u, v) * bg_scale;
                        vec2 flipped = projected / u_screenSize.xy;
                        vec2 bg_tex_coord = vec2(flipped.x, 1.0 - flipped.y) + offset;
                        vec2 bg_tex_coord2 = vec2(flipped.x, 1.0 - flipped.y) + offset2;
                        vec2 bg_tex_coord3 = vec2(flipped.x, 1.0 - flipped.y) + offset3;
                        
                        vec2 clamped = vec2(
                            bg_tex_coord.x,
                            clamp(bg_tex_coord.y, -0.65, 1.1)
                        );
                        vec2 clamped2 = vec2(
                            bg_tex_coord2.x,
                            clamp(bg_tex_coord2.y, -0.65, 1.1)
                        );
                        vec2 clamped3 = vec2(
                            bg_tex_coord3.x,
                            clamp(bg_tex_coord3.y, -0.65, 1.1)
                        );
                    
                        tex_sample = texture2D(u_bg_texture3, clamped3) + u_overlay_color;
                        vec4 tex_sample2 = texture2D(u_bg_texture2, clamped2);
                        tex_sample = mix(tex_sample, tex_sample2, tex_sample2.a);
                        vec4 tex_sample3 = texture2D(u_bg_texture, clamped);
                        tex_sample = mix(tex_sample, tex_sample3, tex_sample3.a);
                    }

                    gl_FragColor = v_color * tex_sample;
                }
                """;
        return List.of(
                new SetShader(batch, gradientVertexShader, gradientFragmentShader),
                new SetCafeUniforms(),
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new SetShader(batch, null, null),
                new DebugRenderer(),
                new TextRenderer(batch)
        );
    }
}
