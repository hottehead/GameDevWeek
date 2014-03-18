package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import java.util.ArrayList;
import java.util.Random;

public class RandomChoice extends BaseNode {

	ArrayList<BaseNode> children = new ArrayList<BaseNode>();
	int activeChild = 0;
	Random r = new java.util.Random(System.currentTimeMillis());
	public RandomChoice(BaseNode parent) {
		super(parent);
	}

	@Override
	public void activate() {
		int count = 0;
		if ((count = this.children.size()) > 0) {
			this.activeChild = this.r.nextInt(count);
			this.children.get(this.activeChild).activate();
		} else {
			throw new NullPointerException(
					"Activating Random node has no effect! There is no child attached.");
		}
	}

	@Override
	public void deactivate() {
		this.activeChild = -1;
	}

	@Override
	public void addChild(BaseNode child) {
		this.children.add(child);
	}

	@Override
	public void childTerminated(BaseNode child, State state) {
		this.parent.childTerminated(this, state);
	}

}
