package timeline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.model.signal.ZeroCrossing;



public class JumpRecognitionTimeline {
	private TreeMap<Date, DeviceData> dataMap;
	
	private SortedMap<Date, ZeroCrossing> zeroCrossings;
	
	
	private int height,width;

	private long timespan;
	private static final float TIMESPAN_PAST =0.9f;
	
	
	
	public JumpRecognitionTimeline() {
		super();
		this.dataMap = new TreeMap<Date,DeviceData>();
		this.zeroCrossings = new TreeMap<Date, ZeroCrossing>();
	}

	public void appendData(DeviceData data){
		
	Entry<Date,DeviceData> lastEntry = dataMap.lastEntry();
	DeviceData last = lastEntry == null ? null: lastEntry.getValue();
		
		this.dataMap.put(data.getReceiveDate(), data);
		
		
		
		if(last != null &&  ((last.getVelocity().getX()>0 && data.getVelocity().getX()<0) || (last.getVelocity().getX()<0 && data.getVelocity().getX()>0))){
			this.zeroCrossings.put(data.getReceiveDate(), new ZeroCrossing(last.getVelocity().getX(), data.getVelocity().getX(), data.getReceiveDate()));
		}
		
		
	}

	public void paint(Graphics g){
		Date now = new Date();
		
		this.zeroCrossings = this.zeroCrossings.tailMap(new Date(now.getTime()-5000));
		
		for(ZeroCrossing crossing : this.zeroCrossings.values()){
			float x = normalizeX(crossing.getDate().getTime()-now.getTime());
			g.setColor(Color.red);
			g.drawOval((int) x, this.height/4, 10, 10);
		}

	}
	
	

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	private boolean active = false;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	public long getTimespan() {
		return timespan;
	}

	public void setTimespan(long timespan) {
		this.timespan = timespan;
	}

	
	private float normalizeX(long x){
		float point = (((float)this.timespan)*TIMESPAN_PAST)+x;
		float factor = (this.width/((float)this.timespan));
		float temp = point*factor;
		return temp;
	}


	
	
	
	
	
	
	
	
}
