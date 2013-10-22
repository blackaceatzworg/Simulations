package at.ac.tuwien.motioncollector.model.ui;

import java.awt.Color;
import java.awt.Shape;

import at.ac.tuwien.motioncollector.model.Axis;

public class VelocityTimeline extends UITimeline {
	Axis axis;

	public Axis getAxis() {
		return axis;
	}

	public void setAxis(Axis axis) {
		this.axis = axis;
	}

	public VelocityTimeline(Color color, int height, int width,
			long timespan,Axis axis) {
		super(color, height, width, timespan);
		this.axis = axis;
	}

	public VelocityTimeline(Color color, Shape shape,
			int height, int width, long timespan,Axis axis) {
		super(color, shape, height, width, timespan);
		this.axis = axis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((axis == null) ? 0 : axis.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VelocityTimeline))
			return false;
		VelocityTimeline other = (VelocityTimeline) obj;
		if (axis != other.axis)
			return false;
		return true;
	}

	
	
	
}
