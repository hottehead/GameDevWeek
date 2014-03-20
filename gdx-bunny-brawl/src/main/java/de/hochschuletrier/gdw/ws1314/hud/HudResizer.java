package de.hochschuletrier.gdw.ws1314.hud;

import java.util.ArrayList;

public class HudResizer {
	
	static private ArrayList<AutoResizeStage> hudStages = new ArrayList<AutoResizeStage>();
	
	private HudResizer() {
	}

	static public void resize(int width, int height) {
		for(AutoResizeStage hud : hudStages) {
			hud.resize(width, height);
		}
	}
	
	static protected void provide(AutoResizeStage hudStage) {
		hudStages.add(hudStage);
	}
}