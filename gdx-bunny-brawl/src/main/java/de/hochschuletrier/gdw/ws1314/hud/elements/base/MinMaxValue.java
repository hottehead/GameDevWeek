package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.math.MathUtils;


public class MinMaxValue {
	protected float stepSize;
	protected float minValue;
	protected float maxValue;
	private float value;
	private float valueFactor; 
	
	public MinMaxValue(float minValue, float maxValue, float stepSize) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.stepSize = stepSize;
		
		this.value = this.minValue;
		this.valueFactor = 0.0f;
	}
	
	public float getValueFactor() {
		return valueFactor;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = MathUtils.clamp(value, minValue, maxValue);
		this.valueFactor = this.value / this.maxValue;
	}
	
	public void stepValue() {
		setValue(this.value + this.stepSize);
	}
	
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
}
