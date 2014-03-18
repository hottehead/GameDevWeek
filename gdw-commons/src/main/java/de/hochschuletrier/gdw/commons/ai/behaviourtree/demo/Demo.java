package de.hochschuletrier.gdw.commons.ai.behaviourtree.demo;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;

public class Demo {
	public static void main(String[] args) {
		Demo demo = new Demo();
	}

	public Demo() {
		BehaviourManager bManager = new BehaviourManager(null);
		MyBehaviour myB = new MyBehaviour();
		bManager.addBehaviour(myB);
		bManager.activate();
		while (!bManager.isFinished()) {
			bManager.update(System.currentTimeMillis());
		}
	}

}
