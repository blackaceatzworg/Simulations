package timeline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.model.signal.ZeroCrossing;



public class JumpRecognitionTimeline {
	private TreeMap<Date, DeviceData> dataMap;
	
	private SortedMap<Date, ZeroCrossing> zeroCrossings;
	private List<TimelineData> meanList;
	private TimelineData localMinimum;
	private TimelineData localMaximum;
	
	private int height,width;

	private long timespan;
	private static final float TIMESPAN_PAST =0.9f;
	
	
	
	public JumpRecognitionTimeline() {
		super();
		this.dataMap = new TreeMap<Date,DeviceData>();
		this.zeroCrossings = new TreeMap<Date, ZeroCrossing>();
		this.meanList = new ArrayList<TimelineData>();
	}

	public void appendData(DeviceData data){
		
	Entry<Date,DeviceData> lastEntry = dataMap.lastEntry();
	DeviceData last = lastEntry == null ? null: lastEntry.getValue();
		
		this.dataMap.put(data.getReceiveDate(), data);
		
		if(last != null &&  ((last.getVelocity().getZ()>0 && data.getVelocity().getZ()<0) || (last.getVelocity().getZ()<0 && data.getVelocity().getZ()>0))){
			this.zeroCrossings.put(data.getReceiveDate(), new ZeroCrossing(last.getVelocity().getZ(), data.getVelocity().getZ(), data.getReceiveDate()));
		}
		
		this.meanList.add(new TimelineData(data.getReceiveDate(), data.getVelocity().getZ()));
		if(this.meanList.size()>3){
			Mean mean = new Mean();
			this.meanList.remove(0);
			
			TimelineData dataMax = Collections.max(this.meanList, new Comparator<TimelineData>() {

				@Override
				public int compare(TimelineData o1, TimelineData o2) {
					return Float.compare(o1.getValue(), o2.getValue());
				}
			});
			
			if(dataMax.getValue()>5){
				int index = this.meanList.indexOf(dataMax);
			
			if(index==2){
				this.localMaximum = dataMax;
			}
			}
			
			
			
			
			
			TimelineData dataMin = Collections.min(this.meanList, new Comparator<TimelineData>() {

				@Override
				public int compare(TimelineData o1, TimelineData o2) {
					return Float.compare(o1.getValue(), o2.getValue());
				}
			});
			
			
			if(dataMin.getValue()<-5){
				int index = this.meanList.indexOf(dataMin);
			
				if(index==2){
					this.localMinimum = dataMin;
				}
			}
			
			
			
			
			
			
			
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
		
		if(this.localMaximum!=null){
			float x = normalizeX(localMaximum.getDate().getTime()-now.getTime());
			g.setColor(Color.BLUE);
			g.drawOval((int) x, 2*this.height/4, 5, 5);
		}
		if(this.localMaximum!=null){
			float x = normalizeX(localMinimum.getDate().getTime()-now.getTime());
			g.setColor(Color.GREEN);
			g.drawOval((int) x, 3*this.height/4, 5, 5);
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
