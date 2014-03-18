package de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces;

public interface Root {

	public abstract void addTask(Leaf t);

	public abstract void deleteTask(Leaf t);
}
