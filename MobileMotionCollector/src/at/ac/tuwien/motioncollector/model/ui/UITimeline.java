package at.ac.tuwien.motioncollector.model.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

import at.ac.tuwien.motioncollector.model.Timeline;

public class UITimeline extends Timeline {
	private boolean active = false;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public void paint(Graphics g) {
		if(isActive()){
			super.paint(g);
		}
		
	}

	public UITimeline(Color color, int height, int width,
			long timespan) {
		super( color, height, width, timespan);
	}

	public UITimeline(Color color, Shape shape, int height,
			int width, long timespan) {
		super( color, shape, height, width, timespan);
	}
	
	
	
}
