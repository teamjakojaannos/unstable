package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Camera {
    public static final float widthInUnits = 24f;
    private final OrthographicCamera camera;
    public BoundingBox bounds;
    private int screenWidth, screenHeight;
    private float heightInUnits;

    public Camera(final int windowWidth, final int windowHeight, final BoundingBox bounds) {
        final float aspectRatio = (float) windowHeight / windowWidth;

        this.heightInUnits = aspectRatio * widthInUnits;
        this.camera = new OrthographicCamera(widthInUnits, this.heightInUnits);

        this.screenWidth = windowWidth;
        this.screenHeight = windowHeight;

        this.bounds = bounds;
    }

    public void resize(final int windowWidth, final int windowHeight) {
        final float aspectRatio = (float) windowHeight / windowWidth;

        this.heightInUnits = aspectRatio * this.widthInUnits;
        final var pos = new Vector3(this.camera.position);
        this.camera.setToOrtho(false, this.widthInUnits, this.heightInUnits);
        this.camera.position.set(pos);
        this.camera.update();

        this.screenWidth = windowWidth;
        this.screenHeight = windowHeight;
    }

    public Matrix4 getCombinedMatrix() {
        return this.camera.combined;
    }

    public float getWidth() {
        return this.screenWidth;
    }

    public float getHeight() {
        return this.screenHeight;
    }

    public Vector2 getPosition() {
        return new Vector2(this.camera.position.x, this.camera.position.y);
    }

    public void setPosition(final Vector3 pos) {
        this.camera.position.set(new Vector3(pos).add(0, heightInUnits / 3.0f, 0));
        this.camera.update();
    }

    public Vector2 getBottomLeft() {
        return getPosition().cpy().sub(widthInUnits / 2.0f, heightInUnits / 2.0f);
    }

    public float getWidthInUnits() {
        return widthInUnits;
    }

    public float getHeightInUnits() {
        return this.heightInUnits;
    }
}
