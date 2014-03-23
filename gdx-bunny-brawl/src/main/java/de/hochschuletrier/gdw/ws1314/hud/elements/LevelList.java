package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelList extends Table {

	private List<ListElement> levelList;
	private Skin defaultSkin;
	
	public LevelList(Skin skin) {
		this.defaultSkin = skin;
		levelList = new List<ListElement>(defaultSkin);
		ScrollPane sp = new ScrollPane(levelList, defaultSkin);
		this.add(sp);
	}
	
	public void addLevel(String levelName) {
		ListElement button = new ListElement(levelName, defaultSkin);
		levelList.getItems().add(button);
	}
	
	public void removeLevel(int index) {
		levelList.getItems().removeIndex(index);
	}
	
	public ListElement getSelected() {
		return levelList.getSelected();
	}
	
	public List<ListElement> getList() {
		return levelList;
	}
	
	public void setSelectedIndex(int index) {
		levelList.setSelectedIndex(index);
	}
}
