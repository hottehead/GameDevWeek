package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import java.util.ArrayList;

public class Parallel extends BaseNode {
	ArrayList<BaseNode> children;
	ArrayList<BaseNode> activeChildren;

	public Parallel(BaseNode parent) {
		super(parent);
		this.children = new ArrayList<BaseNode>(10);
		this.activeChildren = new ArrayList<BaseNode>(10);
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
			this.activeChildren.remove(child);
			if (this.activeChildren.size() == 0) {
				this.deactivate();
				this.parent.childTerminated(child, state);
			}
			break;
		case FAILURE:
			this.deactivate();
			this.parent.childTerminated(child, state);
			break;
		default:
			break;
		}
	}

	@Override
	public void activate() {
		for (BaseNode node : this.children) {
			node.activate();
			this.activeChildren.add(node);
		}
	}

	@Override
	public void deactivate() {
		for (BaseNode node : this.activeChildren) {
			node.deactivate();
		}
		this.activeChildren.clear();
	}

}
