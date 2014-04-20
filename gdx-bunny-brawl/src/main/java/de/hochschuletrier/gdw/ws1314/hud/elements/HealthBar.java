package de.hochschuletrier.gdw.ws1314.hud.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class HealthBar extends Widget {
	Logger logger = LoggerFactory.getLogger(HealthBar.class);
    private NinePatchDrawable loadingBarBackground;
    BitmapFont font;
    private NinePatchDrawable loadingBar;
    private float max , current, progress;
    public HealthBar(AssetManagerX assets) {
        font = assets.getSkin("bunnyBrawl").getFont("candara_25");
    	TextureAtlas skinAtlas = assets.get("data/skins/default.atlas"	,TextureAtlas.class	);
        NinePatch loadingBarBackgroundPatch = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);
        NinePatch loadingBarPatch = new NinePatch(skinAtlas.findRegion("default-round-down"), 5, 5, 4, 4);
        loadingBar = new NinePatchDrawable(loadingBarPatch);
        loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
    }
    
    public HealthBar(AssetManagerX assets, float max) {
    	this.max = this.current = max;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
    	super.draw(batch, parentAlpha);
        loadingBarBackground.draw(batch, getX(), getY(), getPrefWidth() * getScaleX(), getPrefHeight() * getScaleY());
        loadingBar.draw(batch, getX(), getY(), progress * getPrefWidth() * getScaleX(), getPrefHeight() * getScaleY());
        font.draw(batch, current + " / "+ max , getX() , getY() +  font.getLineHeight());
    }
    
    @Override
    public float getPrefWidth() {
    	return getParent().getWidth();
    }
    
    @Override
    public float getPrefHeight() {
    	return 60;
    }
    
	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
		this.progress = current/max;

	}

	public float getCurrent() {
		return current;
	}

	public void setCurrent(float current) {
		this.current = current;
		this.progress = current/max;
	}
    
    
}