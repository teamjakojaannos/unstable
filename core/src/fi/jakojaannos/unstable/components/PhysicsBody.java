package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsBody {
    public final Body body;

    public PhysicsBody(Body body) {
        this.body = body;
    }
}
