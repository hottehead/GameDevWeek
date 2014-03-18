package de.hochschuletrier.gdw.commons.ai.behaviourtree.engine;

import java.util.ArrayList;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces.Leaf;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces.Root;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode.State;

public class Behaviour extends BaseNode implements Root {

	private Object localBlackboard;
	private Object globalBlackboard;
	private String name;
	private BehaviourManager manager;
	private BaseNode child;
	private boolean isLooping = false;
	private boolean isRunning = true;
	private ArrayList<Leaf> activeTasks = new ArrayList<Leaf>(),
			removeTasks = new ArrayList<Leaf>(), addTasks = new ArrayList<Leaf>();

	public Behaviour() {
		super(null);
	}

	public Behaviour(String name, Object localBlackboard, boolean isLooping) {
		super(null);
		this.localBlackboard = localBlackboard;
		setLooping(isLooping);
	}

	public final void update(float delta) {
		if (isRunning) {
			activeTasks.removeAll(removeTasks);
			activeTasks.addAll(addTasks);
			addTasks.clear();
			removeTasks.clear();
			for (Leaf t : activeTasks) {
				t.run(delta);
			}
		}
	}

	@Override
	public final void addTask(Leaf t) {
		addTasks.add(t);
	}

	@Override
	public final void deleteTask(Leaf t) {
		removeTasks.add(t);
	}

	@Override
	public final void addChild(BaseNode child) {
		setRoot(child);
	}

	public final void setRoot(BaseNode child) {
		if (this.child != null) {
			System.out.println("Behaviour " + name
					+ " : Warning, changing root of BehaviourTree!");
		}
		this.child = child;
	}

	@Override
	public final void childTerminated(BaseNode child, State state) {
		deactivate();
		if (isLooping()) {
			activate();
		} else {
			System.out.println("Behaviour " + name + " finished regularly.");
			manager.treeFinished(this);
		}
	}

	@Override
	public final void activate() {
		System.out.println("Behaviour " + name + " activated.");
		if (child != null) {
			child.activate();
		}
	}

	@Override
	public final void deactivate() {
		System.out.println("Behaviour " + name + " deactivated.");
		if (child != null) {
			child.deactivate();
		}
	}

	public void pause() {
		isRunning = false;
		System.out.println("Behaviour " + name + " paused.");
	}

	public void resume() {
		isRunning = true;
		System.out.println("Behaviour " + name + " resumed.");
	}

	public void reset() {
		deactivate();
		activate();
		System.out.println("Behaviour " + name + " reset.");
	}

	public final Object getLocalBlackboard() {
		return localBlackboard;
	}

	public final void setLocalBlackboard(Object localBlackboard) {
		this.localBlackboard = localBlackboard;
	}

	public final Object getGlobalBlackboard() {
		return globalBlackboard;
	}

	public final void setGlobalBlackboard(Object globalBlackboard) {
		this.globalBlackboard = globalBlackboard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

	public BehaviourManager getManager() {
		return manager;
	}

	public void setManager(BehaviourManager manager) {
		this.manager = manager;
	}

}
