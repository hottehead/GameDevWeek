package de.hochschuletrier.gdw.ws1314.hud;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelList extends Table {

	private List<LevelElement> levelList;
	private Skin defaultSkin;
	
	public LevelList(Skin skin) {
		this.defaultSkin = skin;
		levelList = new List<LevelElement>(defaultSkin);
		ScrollPane sp = new ScrollPane(levelList, defaultSkin);
		this.add(sp);
	}
	
	public void addLevel(String levelName) {
		LevelElement button = new LevelElement(levelName, defaultSkin);
		levelList.getItems().add(button);
	}
	
	public void removeLevel(int index) {
		levelList.getItems().removeIndex(index);
	}
	
	public LevelElement getSelected() {
		return levelList.getSelected();
	}
	
	public List<LevelElement> getList() {
		return levelList;
	}
}
