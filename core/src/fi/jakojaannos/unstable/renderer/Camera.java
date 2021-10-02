package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Camera {
    private final float widthInUnits = 17.5f;
    private final OrthographicCamera camera;

    private int screenWidth, screenHeight;
    private float heightInUnits;

    public Camera(final int windowWidth, final int windowHeight) {
        final float aspectRatio = (float) windowHeight / windowWidth;

        this.heightInUnits = aspectRatio * this.widthInUnits;
        this.camera = new OrthographicCamera(this.widthInUnits, this.heightInUnits);

        this.screenWidth = windowWidth;
        this.screenHeight = windowHeight;
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
}
