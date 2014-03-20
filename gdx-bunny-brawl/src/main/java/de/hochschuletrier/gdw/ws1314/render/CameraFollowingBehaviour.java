package de.hochschuletrier.gdw.ws1314.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;

public class CameraFollowingBehaviour {
	Vector2 prevFollowingPosition;
	ClientEntity followingEntity;
	final OrthographicCamera camera;
	LevelBoundings bounds;
	
	public CameraFollowingBehaviour(OrthographicCamera camera, LevelBoundings bounds) {
		this.camera = camera;
		this.bounds = bounds;
		prevFollowingPosition = new Vector2();
	}
	
	public void setFollowingEntity(ClientEntity followingEntity) {
		this.followingEntity = followingEntity;
	}
	
	public void update(float dt) {
		if(followingEntity!=null) {
			Vector2 currentPos = followingEntity.getPosition();
			currentPos.x = currentPos.x;
			currentPos.y = currentPos.y;
//			currentPos.x = currentPos.x - Gdx.graphics.getWidth() * 0.5f;
//			currentPos.y = currentPos.y - Gdx.graphics.getHeight() * 0.5f;

			float dx = (currentPos .x - this.prevFollowingPosition.x)*dt;
			float dy = (currentPos .y - this.prevFollowingPosition.y)*dt;
			
			
			camera.translate(dx, dy);
			bounds.boundVector(camera.position);
			
//			System.out.println(camera.position + " " + currentPos);
			
			this.prevFollowingPosition.x = this.prevFollowingPosition.x + dx;
			this.prevFollowingPosition.y = this.prevFollowingPosition.y + dy;
		}
	}
}
