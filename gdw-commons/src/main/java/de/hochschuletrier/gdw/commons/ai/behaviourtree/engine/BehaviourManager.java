package de.hochschuletrier.gdw.commons.ai.behaviourtree.engine;

import java.util.LinkedList;

public class BehaviourManager {

	private LinkedList<Behaviour> behaviours;
	private int finishedBehaviours;
	private Object globalBlackboard;
	private boolean isRunning = true;

	public BehaviourManager(Object globalBlackboard) {
		behaviours = new LinkedList<Behaviour>();
		this.globalBlackboard = globalBlackboard;
	}

	public void update(float delta) {
		if (isRunning && !isFinished()) {
			for (Behaviour tree : behaviours) {
				tree.update(delta);
			}
		}
	}

	public void activate() {
		System.out.println("Behaviour Manager: Starting all behaviours.");
		for (Behaviour b : behaviours) {
			b.activate();
		}
	}

	public void addBehaviour(Behaviour b) {
		b.setGlobalBlackboard(globalBlackboard);
		b.setManager(this);
		behaviours.add(b);
	}

	public void treeFinished(Behaviour t) {
		if ((++finishedBehaviours) == behaviours.size()) {
			System.out.println("Behaviour Manager: All behaviours finished regularly.");
		}
	}

	public Object getGlobalBlackboard() {
		return globalBlackboard;
	}

	public void setGlobalBlackboard(Object globalBlackboard) {
		this.globalBlackboard = globalBlackboard;
	}

	public void deactivate() {
		for (Behaviour b : behaviours) {
			b.deactivate();
		}
		System.out.println("Behaviour Manager: Engine stopped.");
	}

	public void pause() {
		isRunning = false;
		System.out.println("Behaviour Manager: Engine paused.");
	}

	public void resume() {
		isRunning = true;
		System.out.println("Behaviour Manager: Engine resumed.");
	}

	public void reset() {
		deactivate();
		activate();
	}

	public boolean isFinished() {
		return finishedBehaviours == behaviours.size();
	}

	public boolean isRunning() {
		return isRunning;
	}
}
