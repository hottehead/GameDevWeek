package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import java.util.ArrayList;

public class Selector extends BaseNode {

	ArrayList<BaseNode> children;
	int currentChild = -1;

	public Selector(BaseNode parent) {
		super(parent);
		this.children = new ArrayList<BaseNode>(10);
	}

	@Override
	public void addChild(BaseNode child) {
		this.children.add(child);
	}

	@Override
	public void childTerminated(BaseNode child,State state) {
		switch(state){
		case SUCCESS:
			this.deactivate();
			this.parent.childTerminated(this, state);
			break;
		case FAILURE:
			if((this.currentChild +1) >= this.children.size()){
				this.deactivate();
				this.parent.childTerminated(this, state);
			}else {
				++this.currentChild;
				this.children.get(this.currentChild).activate();
			}
		default:
			break;
		}
	}

	@Override
	public void activate() {
		if(this.children.size() > 0){
			this.currentChild = 0;
			this.children.get(this.currentChild).activate();
		} else {
			throw new NullPointerException(
					"Activating Selector node has no effect! There is no child attached.");
		}
	}

	@Override
	public void deactivate() {
		BaseNode node = this.children.get(this.currentChild);
		if(node != null){
			node.deactivate();
			this.currentChild = -1;
		} else {
			throw new NullPointerException(
					"Deactivating Selector node has no effect! There is no child attached.");
		}
	}

}
