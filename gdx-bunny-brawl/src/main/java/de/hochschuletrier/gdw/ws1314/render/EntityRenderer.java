package de.hochschuletrier.gdw.ws1314.render;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;



public class EntityRenderer {

	ArrayList<RenderObject> renderObjects;
	Vector<RenderObject> pool;
	
	public EntityRenderer() {
		renderObjects = new ArrayList<RenderObject>();
		pool = new Vector<RenderObject>();
	}
	
	protected void poolObjects(int cap) {
		pool.setSize(pool.capacity() + cap);
	}
	
	public void draw() {
		for(RenderObject obj : this.renderObjects) {
			Vector2 pos = obj.entity.getPosition();
			DrawUtil.batch.draw(obj.material.texture, pos.x, pos.y, obj.material.width, obj.material.height); 
		}
	}
	
	public void registerRenderObject(ClientEntity entity, Material material) {
		if(pool.size()==0) {
			poolObjects(10);
		}
		RenderObject renderObj = pool.remove(0);
		renderObj.entity = entity;
	}
}
