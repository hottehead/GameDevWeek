package de.hochschuletrier.gdw.ws1314.render;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.Pool;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;

public class EntityRenderer extends Pool<RenderObject> implements
		ClientEntityManagerListener {

	ArrayList<RenderObject> renderList;
	MaterialManager materials;

	public EntityRenderer(MaterialManager materialManager) {
		renderList = new ArrayList<RenderObject>();
		materials = materialManager;
	}

	public void draw() {
		//Collections.sort(renderList);

		for (RenderObject obj : this.renderList) {
			if (obj.isVisible()) {
				float rot = obj.entity.getFacingDirection().getAngle()
						* MathUtils.radiansToDegrees;
				if (obj.entity instanceof ClientPlayer) {
					rot = 0;
				}
				Material activeMat = obj.getActiveMaterial();
				Vector2 pos = obj.entity.getPosition();
				if (activeMat != null) {

					float dh = activeMat.height * 0.5f;
					float dw = activeMat.width * 0.5f;

					TextureRegion texPtr = obj.getActiveTexture();
					DrawUtil.batch.draw(texPtr, pos.x - dw, pos.y + dh, dw,
							-dh, activeMat.width, -activeMat.height, 1, 1, rot);
				} else {
					Material m = MaterialManager.dbgMaterial;

					DrawUtil.batch.draw(m.texture, pos.x - m.width * 0.5f,
							pos.y + m.height * 0.5f, m.width * 0.5f,
							-m.height * 0.5f, m.width, -m.height, 1, 1, 0);
				}
			}
		}
	}

	RenderType fetchStore = new RenderType();
	
	@Override
	public void onEntityInsert(ClientEntity entity) {
		fetchStore.setByEntity(entity);;
		RenderObject renderObj = this.fetch();
		renderObj.materialAtlas = materials.fetch(fetchStore);
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
