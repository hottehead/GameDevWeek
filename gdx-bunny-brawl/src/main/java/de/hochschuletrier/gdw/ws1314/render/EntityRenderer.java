package de.hochschuletrier.gdw.ws1314.render;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ClientProjectile;

public class EntityRenderer extends Pool<RenderObject> implements
		ClientEntityManagerListener {

	ArrayList<RenderObject> renderList;
	MaterialManager materials;

	public EntityRenderer(MaterialManager materialManager) {
		renderList = new ArrayList<RenderObject>();
		materials = materialManager;
	}

	public void draw() {
		Collections.sort(renderList);
		
		for (RenderObject obj : this.renderList) {
			Vector2 pos = obj.entity.getPosition();
			
			float dh = obj.material.height * 0.5f;
			float dw = obj.material.width * 0.5f;

			if(obj.entity instanceof ClientProjectile) {
			ClientProjectile eProj = (ClientProjectile)obj.entity;
			DrawUtil.batch.draw(obj.getActiveTexture(), pos.x - dw, pos.y - dh
					+ obj.material.height, pos.x - dw, pos.y - dh
					+ obj.material.height, obj.material.width, obj.material.height, 1, 1, eProj.getFacingDirection().getAngle());
			}
			else {
				DrawUtil.batch.draw(obj.getActiveTexture(), pos.x - dw, pos.y - dh
						+ obj.material.height, obj.material.width,
						-obj.material.height);
			
			}
		}
	}

	@Override
	public void onEntityInsert(ClientEntity entity) {
		RenderObject renderObj = this.fetch();
		renderObj.material = materials.fetch(entity.getEntityType());
		renderObj.entity = entity;
		this.renderList.add(renderObj);
	}

	@Override
	public void onEntityRemove(ClientEntity entity) {
		// find object in renderObj -> remove and provide to pool, without O(n)
		for (RenderObject obj : renderList) {
			if (obj.entity.getID() == entity.getID()) {
				providePoolObject(obj);
				renderList.remove(obj);
				return;
			}
		}
	}

	@Override
	protected void onEmptyPool() {
		for (int i = 0; i < 10; ++i) {
			providePoolObject(new RenderObject(null, null));
		}
	}
}
