package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class HudRendering {

	//FIXME: Inverse beheben sobald Camera geflippt
	public static void drawElement(Texture tex, float positionX,
			float positionY, float width, float height) {
		DrawUtil.batch.draw(tex, positionX, positionY + height, width, -height);
	}

	public static void drawNinePatch(NinePatch ninepatch, float positionX, float positionY, float width, float height) {
		ninepatch.draw(DrawUtil.batch, positionX, positionY, width, height);
	}
}
