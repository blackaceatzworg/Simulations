package movement;

import util.Units;


public class MovementSpeed {
	//unit of all values is kmh
	private double minValue;
	private double maxValue;
	private double currentValue;
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public double getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}
	public MovementSpeed(double minValue, double maxValue, double currentValue) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.currentValue = currentValue;
	}
	
	public double getDistance(double timespan,Units.Time timeUnit, Units.Distance distanceUnit){
		double factorTime = 1;
		switch(timeUnit){
			case Milliseconds:
				factorTime = 3600000;
				break;
			case Seconds:
				factorTime = 3600;
				break;
			case Minutes:
				factorTime = 60;
				break;
			case Hours:
				factorTime = 1;
				break;
			default:
				factorTime = 1;
		}
		
		double factorDistance =1;
		switch(distanceUnit){
		case Millimeter:
			factorDistance = 1000000;
			break;
		case Centimeter:
			factorDistance= 100000;
			break;
		case Meter:
			factorDistance = 1000;
			break;
		default:
			factorDistance = 1;
		
		}
		return this.currentValue*(factorDistance/factorTime);
		
	}
	
	
}
