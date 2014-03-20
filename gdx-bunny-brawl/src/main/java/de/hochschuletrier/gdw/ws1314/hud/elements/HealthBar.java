package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxBackgroundDecoration;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxFrontDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.DynamicTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.NinePatchSettings;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBox;

public class HealthBar {

	VisualBox visualRepresentation;
	MinMaxValue logicRepresentation;
	DynamicTextElement textElement;
	
	private int maxHealthValue;
	
	public HealthBar(int maxHealthValue) {
		logicRepresentation = new MinMaxValue(0, maxHealthValue, 10);
		logicRepresentation.setValue(maxHealthValue);
		this.maxHealthValue = maxHealthValue;
	}
	
	public void initVisual(AssetManagerX assetManager, float positionX, float positionY,
			float width, float height) {
		Texture barTex = assetManager.getTexture("debugBar");
		Texture backBarTex = assetManager.getTexture("debugTooltip");
		Texture frontBarTex = assetManager.getTexture("debugBarDecorNine");
		BitmapFont hudFont = assetManager.getFont("verdana", 24);
		
		VisualBar healthBarVisual = new VisualBar(barTex, positionX, positionY, width, height,
				logicRepresentation);

		BoxBackgroundDecoration backgroundHealth = new BoxBackgroundDecoration(
				healthBarVisual, backBarTex);
		// BarFrontDecorator frontBar = new BarFrontDecorator(test,
		// frontBarTex);
		
		textElement = new DynamicTextElement(hudFont, "HP: ",
						backgroundHealth.getWidth() * 0.5f,
						backgroundHealth.getHeight() + 2, logicRepresentation);
		visualRepresentation = new BoxOffsetDecorator(backgroundHealth, textElement);
		visualRepresentation = new BoxFrontDecorator(visualRepresentation, frontBarTex,
				new NinePatchSettings(1, 2, 2, 1));
	}
	
	public MinMaxValue get() {
		return logicRepresentation;
	}

	public void draw() {
		visualRepresentation.draw();
	}
	
	public void setDecimalSpace(int n) {
		textElement.setDecimalPLace(n);
	}
	
	public void reset() {
		logicRepresentation.setValue(maxHealthValue);
	}
}
