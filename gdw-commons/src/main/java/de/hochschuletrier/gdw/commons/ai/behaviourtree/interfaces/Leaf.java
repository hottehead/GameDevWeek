package de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces;

public interface Leaf {

	/**
	 * Runs the update code assigned to this task
	 * 
	 * @param delta
	 *            - elapsed time since last update
	 */
	public void run(float delta);
}
