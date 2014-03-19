package de.hochschuletrier.gdw.ws1314.render;

import java.util.Stack;


public abstract class Pool<T> {
	Stack<T> pool;
	
	public Pool() {
		pool = new Stack<T>();
	}
	
	protected abstract void onEmptyPool();
	protected void providePoolObject(T obj) {
		pool.add(obj);
	}
	public T fetch() {
		if(pool.size()==0) {
			onEmptyPool();
		}
		return pool.pop();
	}
	
}