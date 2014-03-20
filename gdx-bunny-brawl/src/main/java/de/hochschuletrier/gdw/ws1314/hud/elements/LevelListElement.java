package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LevelListElement extends TextButton {

	public LevelListElement(String text, Skin skin) {
		super(text, skin);
	}
	
	public String toString() {
		return super.getText().toString();
	}

}
