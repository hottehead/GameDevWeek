package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelList extends Table {

	private List<LevelListElement> levelList;
	private Skin defaultSkin;
	
	public LevelList(Skin skin) {
		this.defaultSkin = skin;
		levelList = new List<LevelListElement>(defaultSkin);
		ScrollPane sp = new ScrollPane(levelList, defaultSkin);
		this.add(sp);
	}
	
	public void addLevel(String levelName) {
		LevelListElement button = new LevelListElement(levelName, defaultSkin);
		levelList.getItems().add(button);
	}
	
	public void removeLevel(int index) {
		levelList.getItems().removeIndex(index);
	}
	
	public LevelListElement getSelected() {
		return levelList.getSelected();
	}
	
	public List<LevelListElement> getList() {
		return levelList;
	}
}
