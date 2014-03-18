package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.gdx.utils.BodyEditorLoader;

public class Vase extends PhysixEntity {

	private BodyEditorLoader loader = new BodyEditorLoader(
			Gdx.files.internal("data/json/bodies.json"));
	private Vector2 position;
	private Vector2 origin = new Vector2(0f, 0f);
	private float rotation = 0f;
	private Vector2 scale = new Vector2(1f, 1f);
	private ImageX image;

	public Vase(float x, float y) {
		position = new Vector2(x, y);
	}

	public void initPhysics(PhysixManager manager) {
		PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
				.position(position.x - image.getWidth() / 2, position.y
				- image.getHeight() / 2).fixedRotation(false).create();
		float scaling = manager.toBox2D(image.getWidth());
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 2f;
		loader.attachFixture(body.getBody(), "test01", fixtureDef, scaling);
		body.setTransform(position.x, position.y, MathUtils.degRad * rotation);
		body.getLocalCenter(origin);
		rotation = body.getAngle();
		setPhysicsBody(body);
	}

	public void initGraphics(AssetManagerX assets) {
		image = assets.getImageX("vase");
	}

	public void update() {
		rotation = physicsBody.getAngle();
	}
	public void setPosition(Vector2 position) {
		physicsBody.setTransform(position.x - image.getWidth() / 2, position.y
				- image.getHeight() / 2, 0);
        physicsBody.setLinearVelocity(Vector2.Zero);
        physicsBody.setAngularVelocity(0);
	}

	public ImageX getImage() {
		return image;
	}

	public float getRotation() {
		return rotation;
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2 origin) {
		this.origin = origin;
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(Vector2 scale) {
		this.scale = scale;
	}

}
