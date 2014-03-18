package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;


public abstract class BaseNode {
	public static enum State {
		READY, SUCCESS, FAILURE, RUNNING
	};

	protected BaseNode parent;
	protected Behaviour behaviour;
	protected State state = State.READY;

	public BaseNode(BaseNode parent) {
		if (parent != null) {
			this.parent = parent;
			this.parent.addChild(this);
		}
		this.behaviour = getBehaviour(this);
	}
	public abstract void activate();

	public abstract void deactivate();

	public abstract void addChild(BaseNode child);

	public abstract void childTerminated(BaseNode child, State state);

	public final void setParent(BaseNode parent) {
		this.parent = parent;
	}

	public static Behaviour getBehaviour(BaseNode node) {
		BaseNode tmp = node;
		while (tmp.parent != null) {
			tmp = tmp.parent;
		}
		return (Behaviour) tmp;
	}
}
