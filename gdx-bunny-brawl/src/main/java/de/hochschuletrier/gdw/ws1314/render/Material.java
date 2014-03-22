package de.hochschuletrier.gdw.ws1314.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class Material implements Comparable<Material> {

	protected int Layer;
	protected float width, height;
	protected TextureRegion texture;
	protected AnimationExtended animation;
	boolean isAnimation;

	protected Material(AssetManagerX assetManager, MaterialInfo materialInfo) {
		putInfo(assetManager, materialInfo);

	}
	
	protected void putInfo(AssetManagerX assetManager, MaterialInfo materialInfo) {
		if(materialInfo.isAnimation) {
			animation = assetManager.getAnimation(materialInfo.textureName);
		}
		else {
			this.texture = new TextureRegion(assetManager.getTexture(materialInfo.textureName));
		}
		this.Layer = materialInfo.layer;
		this.width = materialInfo.width; 
		this.height = materialInfo.height;
		this.isAnimation = materialInfo.isAnimation;
	}

	/*
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(Material o) {
		final int GREATER_THAN = 1;
		final int EQUAL_TO = 0;
		final int LESS_THAN = -1;
		if(this.Layer < o.Layer) {
			return LESS_THAN;
		}
		
		return EQUAL_TO;
	}

	public TextureRegion getActiveTexture(float stateTime) {
		if(isAnimation) {
			TextureRegion animationTex = animation.getKeyFrame(stateTime);
			return animation.getKeyFrame(stateTime);	
		}
		else {
			return texture;
		}
	}
	
	

}
