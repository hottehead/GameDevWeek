package de.hochschuletrier.gdw.ws1314.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;

public class CameraFollowingBehaviour {
	Vector2 prevFollowingPosition;
	ClientEntity followingEntity;
	final OrthographicCamera camera;
	
	public CameraFollowingBehaviour(OrthographicCamera camera) {
		this.camera = camera;
		prevFollowingPosition = new Vector2();
	}
	
	public void setFollowingEntity(ClientEntity followingEntity) {
		this.followingEntity = followingEntity;
		
		this.prevFollowingPosition.x = followingEntity.getPosition().x;
		this.prevFollowingPosition.x = followingEntity.getPosition().x;
	}
	
	public void update(float dt) {
		if(followingEntity!=null) {
			Vector2 currentPos = followingEntity.getPosition();
			float dx = currentPos.x - this.prevFollowingPosition.x;
			float dy = currentPos.y - this.prevFollowingPosition.y;
			
			camera.translate(dx, dy);
		}
	}
}
