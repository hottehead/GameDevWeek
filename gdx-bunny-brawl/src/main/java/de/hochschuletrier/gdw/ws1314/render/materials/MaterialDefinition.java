package de.hochschuletrier.gdw.ws1314.render.materials;

import java.util.ArrayList;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.render.MaterialInfo;
import de.hochschuletrier.gdw.ws1314.render.RenderState;

public abstract class MaterialDefinition {
	protected int width, height, layer;
	
	protected ArrayList<MaterialInfo> materials;
	
	public MaterialDefinition(int width, int height, int layer) {
		this.materials = new ArrayList<MaterialInfo>(); 
		this.width = width;
		this.height = height;
		this.layer = layer;
	}
	
	public abstract void build();
	
	public MaterialInfo[] get() {
		materials.clear();
		build();
		MaterialInfo ret[] = new MaterialInfo[this.materials.size()]; 
		return materials.toArray(ret);
	}
	
	protected void newMaterial(String textureName, EntityStates entityState, FacingDirection facing, boolean isAnimation) {
		materials.add(new MaterialInfo(textureName, new RenderState(entityState, facing), width, height, layer, isAnimation));
	}

}
