package de.hochschuletrier.gdw.ws1314.render;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;



public class EntityRenderer extends Pool<RenderObject> implements ClientEntityManagerListener {
	
	ArrayList<RenderObject> renderList;
	MaterialManager materials;
	
	public EntityRenderer(MaterialManager materialManager) {
		renderList = new ArrayList<RenderObject>();
		materials = materialManager;
	}
	
	
	public void draw() {
		for(RenderObject obj : this.renderList) {
			Vector2 pos = obj.entity.getPosition();
			DrawUtil.batch.draw(obj.material.texture, pos.x, pos.y+obj.material.height, obj.material.width, -obj.material.height); 
		}
	}
	
	@Override
	public void onEntityInsert(ClientEntity entity) {
		System.out.println("create renderobj");
		RenderObject renderObj = this.fetch();
		renderObj.material = materials.fetch(entity.getClass());
		renderObj.entity = entity;
		this.renderList.add(renderObj);
	}

	@Override
	public void onEntityRemove(ClientEntity entity) {
		// not yet implemented
		// find object in renderObj -> remove and provide to pool, without O(n)
	}


	@Override
	protected void onEmptyPool() {
		for(int i=0;i<10;++i) {
			providePoolObject(new RenderObject(null, null));
		}
	}
}
