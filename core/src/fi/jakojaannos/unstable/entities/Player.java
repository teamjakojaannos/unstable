package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.MovementAttributes;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;

public class Player {
    private Player() {}

    public static Entity.Builder create(final World physicsWorld, final Vector2 position) {
        final var bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;

        final var body = physicsWorld.createBody(bodyDef);
        final var hitBox = new CircleShape();
        hitBox.setRadius(0.5f);
        final var hbFixture = new FixtureDef();
        hbFixture.filter.categoryBits = UnstableGame.Constants.Collision.CATEGORY_PLAYER;
        hbFixture.filter.maskBits = UnstableGame.Constants.Collision.MASK_PLAYER;
        hbFixture.shape = hitBox;
        hbFixture.density = 80.0f;
        hbFixture.friction = 0.15f;
        hbFixture.restitution = 0.0f;
        body.createFixture(hbFixture);
        hitBox.dispose();

        final var drag = 1.0f;
        final var force = 2.0f;
        return Entity.builder()
                     .component(new PhysicsBody(body, drag))
                     .component(new MovementAttributes(force))
                     .component(new MovementInput());
    }
}
