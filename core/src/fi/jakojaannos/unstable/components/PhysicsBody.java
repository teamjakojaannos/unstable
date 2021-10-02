package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import fi.jakojaannos.unstable.ecs.Component;

public class PhysicsBody implements Component<PhysicsBody> {
    public final Body body;

    public float dragCoefficient;

    public PhysicsBody(final Body body, final float dragCoefficient) {
        this.body = body;
        this.dragCoefficient = dragCoefficient;
    }

    @Override
    public PhysicsBody cloneComponent() {
        final var bodyDef = new BodyDef();
        bodyDef.fixedRotation = this.body.isFixedRotation();
        bodyDef.position.set(this.body.getPosition());
        bodyDef.type = this.body.getType();
        bodyDef.active = this.body.isActive();
        bodyDef.allowSleep = this.body.isSleepingAllowed();
        bodyDef.angle = this.body.getAngle();
        bodyDef.angularDamping = this.body.getAngularDamping();
        bodyDef.angularVelocity = this.body.getAngularVelocity();
        bodyDef.awake = this.body.isAwake();
        bodyDef.bullet = this.body.isBullet();
        bodyDef.gravityScale = this.body.getGravityScale();
        bodyDef.linearDamping = this.body.getLinearDamping();
        bodyDef.linearVelocity.set(this.body.getLinearVelocity());

        final var newBody = this.body.getWorld().createBody(bodyDef);
        this.body.getFixtureList()
                 .forEach(fixture -> {
                     final var fixtureDef = new FixtureDef();
                     fixtureDef.density = fixture.getDensity();
                     fixtureDef.filter.set(fixture.getFilterData());
                     fixtureDef.friction = fixture.getFriction();
                     fixtureDef.restitution = fixture.getRestitution();
                     fixtureDef.shape = fixture.getShape();
                     fixtureDef.isSensor = fixture.isSensor();

                     newBody.createFixture(fixtureDef);
                 });

        return new PhysicsBody(newBody, this.dragCoefficient);
    }
}
