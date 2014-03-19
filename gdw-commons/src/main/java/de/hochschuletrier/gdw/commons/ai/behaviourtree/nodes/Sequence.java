package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import java.util.ArrayList;

public class Sequence extends BaseNode {

	ArrayList<BaseNode> children;
	int currentChild = 0;

	public Sequence(BaseNode parent) {
		super(parent);
		this.children = new ArrayList<BaseNode>(10);
	}

	@Override
	public void activate() {
		if (this.children.size() > 0) {
			this.currentChild = 0;
			this.children.get(this.currentChild).activate();
		} else {
			throw new NullPointerException(
					"Activating Sequence node no effect! There is no child attached.");
		}
	}

	@Override
	public void deactivate() {
		BaseNode node = this.children.get(this.currentChild);
		if (node != null) {
			node.deactivate();
		} else {
			throw new NullPointerException(
					"Deactivating Sequence node no effect! There is no child attached.");
		}
	}

	@Override
	public void addChild(BaseNode child) {
		this.children.add(child);
		child.setParent(this);

	}

	@Override
	public void childTerminated(BaseNode child, State state) {

		switch (state) {
		case SUCCESS:
			if ((this.currentChild + 1) >= this.children.size()) {
				this.deactivate();
				this.parent.childTerminated(this, state);
			} else {
				this.currentChild++;
				this.children.get(this.currentChild).activate();
			}
			break;
		case FAILURE:
			this.deactivate();
			this.parent.childTerminated(this, state);
			break;
		default:
			break;
		}
	}

	public void reset() {
		this.currentChild = 0;
	}
}
