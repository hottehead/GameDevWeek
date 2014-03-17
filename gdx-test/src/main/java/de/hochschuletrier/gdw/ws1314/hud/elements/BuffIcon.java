package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BuffIcon {
	
	private Skin skin;
	
	public BuffIcon(Skin skin) {
		this.skin = skin;
	}
	
	public Button createButton(String styleName) {
		return new Button(skin, styleName);
	}
}
