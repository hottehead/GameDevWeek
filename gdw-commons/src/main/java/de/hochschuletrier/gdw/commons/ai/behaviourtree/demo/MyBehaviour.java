package de.hochschuletrier.gdw.commons.ai.behaviourtree.demo;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseTask;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.RandomChoice;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.Sequence;

public class MyBehaviour extends Behaviour {

	public MyBehaviour() {
		setName("Log to Console");
		BaseNode root = new Sequence(this);
		Writer w = new Writer(root, "D");
		new Writer(root, "E");
		new Writer(root, "M");
		new Writer(root, "O");
		RandomChoice random = new RandomChoice(root);
		new Writer(random, "!");
		new Writer(random, "?");
		setLooping(false);

	}


	class Blackboard {
		Object player;
		Object[] enemies;
		boolean isAggressive;
	}



	class Writer extends BaseTask {
		String string;
		public Writer(BaseNode parent, String string) {
			super(parent);
			this.string = string;
		}

		@Override
		public State onRun(float delta) {
			System.out.println(string);
			return State.SUCCESS;
		}

		@Override
		public void onActivate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDeactivate() {
			// TODO Auto-generated method stub

		}

	}
}
