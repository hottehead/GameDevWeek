package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces.Leaf;

public abstract class BaseTask extends BaseNode implements Leaf {



	public BaseTask(BaseNode parent) {
		super(parent);
	}

	@Override
	public final void run(float delta) {
		State result = onRun(delta);
		switch (result) {
		case SUCCESS:
			deactivate();
			parent.childTerminated(this, State.SUCCESS);
			break;
		case FAILURE:
			deactivate();
			parent.childTerminated(this, State.FAILURE);
			break;
		default:
			break;
		}
	}

	public abstract State onRun(float delta);

	@Override
	public final void activate() {
		behaviour.addTask(this);
		onActivate();
	}

	public abstract void onActivate();

	@Override
	public final void deactivate() {
		behaviour.deleteTask(this);
		onDeactivate();
	}

	public abstract void onDeactivate();

	@Override
	public final void addChild(BaseNode child) {
	}

	@Override
	public final void childTerminated(BaseNode child, State state) {
	}
}
