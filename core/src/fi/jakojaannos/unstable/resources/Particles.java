package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Particles {
    public final List<Particle> list = new ArrayList<>();

    public void burst(Vector2 origin, Color color,int count, int currentTick) {
        for (int i = 0; i < count; i++) {
            this.list.add(new Particle(
                    color,
                    new Vector2(origin).add(MathUtils.random(-0.2f, 0.2f), MathUtils.random(-1.2f, 1.8f)),
                    new Vector2(MathUtils.random(-2.0f, 2.0f), MathUtils.random(-2.0f, 2.0f)),
                    currentTick,
                    MathUtils.random(50, 4 * 50),
                    MathUtils.random(0.05f, 0.2f), MathUtils.random(0.05f, 0.15f),
                    MathUtils.random(0.05f, 0.2f), MathUtils.random(0.05f, 0.15f)
            ));
        }
    }

    public static class Particle {
        private final Color color;
        private final Vector2 startPos;
        private final Vector2 velocity;
        private final int spawnTick;
        private final int lifeTime;
        private final float startWidth, endWidth;
        private final float startHeight, endHeight;

        private Particle(Color color, Vector2 startPos, Vector2 velocity, int spawnTick, int lifeTime, float startWidth, float endWidth, float startHeight, float endHeight) {
            this.color = color;
            this.startPos = startPos;
            this.velocity = velocity;
            this.spawnTick = spawnTick;
            this.lifeTime = lifeTime;
            this.startWidth = startWidth;
            this.endWidth = endWidth;
            this.startHeight = startHeight;
            this.endHeight = endHeight;
        }

        public Vector2 positionAt(int tick) {
            final var t = timeElapsed(tick);
            final var p = lifeTimeProgress(tick);
            final var x = (startPos.x) + (velocity.x * p);
            final var y = (startPos.y) + (velocity.y * p);
            return new Vector2(x, y);
        }

        public float widthAt(int tick) {
            return MathUtils.lerp(startWidth, endWidth, lifeTimeProgress(tick));
        }

        public float heightAt(int tick) {
            return MathUtils.lerp(startHeight, endHeight, lifeTimeProgress(tick));
        }

        public float alphaAt(int tick) {
            return MathUtils.lerp(1.0f, 0.0f, lifeTimeProgress(tick));
        }

        private int timeElapsed(int tick) {
            return MathUtils.clamp(tick - spawnTick, 0, lifeTime);
        }

        private float lifeTimeProgress(int tick) {
            final float timeElapsed = (float) MathUtils.clamp(tick - spawnTick, 0, lifeTime);
            return timeElapsed / lifeTime;
        }

        public Color color() {
            return this.color;
        }

        public boolean hasDied(int tick) {
            return tick >= this.spawnTick + this.lifeTime;
        }
    }
}
