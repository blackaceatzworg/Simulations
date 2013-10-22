package at.ac.tuwien.motioncollector.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Timeline {
	private SortedMap<Date, TimelineData> dataMap;
	private LinkedList<TimelineData> dataList;
	private Color color;
	private Shape shape;

	
	private int height,width;
	
	private TimelineData minimum, maximum;
	private long timespan;
	private static final float TIMESPAN_PAST =0.9f;
	
	
	public Timeline() {
		super();
		this.dataMap = new TreeMap<Date,TimelineData>();
		this.dataList = new LinkedList<TimelineData>();
		this.minimum = this.maximum = null;
	}
	
	public Timeline(Color color, Shape shape,int height,int width,long timespan) {
		this();
		this.color = color;
		this.shape = shape;
		this.height = height;
		this.width = width;
		this.timespan = timespan;
	}
	

	public Timeline(Color color,int height, int width,long timespan) {
		this();
		this.color = color;
		Ellipse2D.Float ellipse = new Ellipse2D.Float();
		ellipse.height = 5;
		ellipse.width = 5;
		this.shape = ellipse;
		this.height = height;
		this.width = width;
		this.timespan = timespan;
	}

	public void appendData(TimelineData data){
		if(this.dataMap.size() == 0){
			initialize(data);
			this.dataMap.put(data.getDate(), data);
			this.dataList = new LinkedList<TimelineData>(this.dataMap.values());
			return;
		}
		
		this.dataMap.put(data.getDate(), data);
		this.dataMap = this.dataMap.tailMap(new Date(data.getDate().getTime()-this.timespan));
		this.dataList = new LinkedList<TimelineData>(this.dataMap.values());
		

		boolean resetMinimum = false;
		boolean resetMaximum = false;
		if(data.getDate().getTime()-this.minimum.getDate().getTime()> this.timespan){
			resetMinimum = true;
		}
		if(data.getDate().getTime()-this.maximum.getDate().getTime()> this.timespan){
			resetMaximum = true;
		}
		
		if(resetMaximum||resetMinimum){
			TimelineData tempMax = new TimelineData(new Date(), 2);
			TimelineData tempMin = new TimelineData(new Date(),-2);
			
			for(TimelineData t : dataList){
				tempMax= tempMax.getValue()>=t.getValue()?tempMax:t;
				tempMin = tempMin.getValue()<=t.getValue()?tempMin:t;
			}
			
			if(resetMaximum){
				this.maximum = tempMax.clone();
				this.minimum = tempMin.clone();
			}
		}else{
			this.maximum = maximum.getValue()>=data.getValue()?maximum:data.clone();
			this.minimum = minimum.getValue()<=data.getValue()?minimum:data.clone();
		}
		
		
		
	}

	public void appendData(Date date, float value){
		appendData(new TimelineData(date, value));
	}
	
	private void initialize(TimelineData data){
		this.minimum = data.clone();
		this.minimum.setValue(-2);
		this.maximum = data.clone();
		this.maximum.setValue(+2);
	}


	public void paint(Graphics g){
		if(this.dataMap.size()==0){
			return;
		}
		Date latestKey = this.dataMap.lastKey();
		for(TimelineData data:this.dataList){
			float x,y;
			
			y = normalizeY(data.getValue());
			x = normalizeX(data.getDate().getTime()-latestKey.getTime());
			
			Rectangle2D bounds = new Rectangle2D.Float((float)(x-shape.getBounds2D().getWidth()/2), (float)(y-shape.getBounds2D().getHeight ()/2), (float)(shape.getBounds2D().getWidth()), (float)(shape.getBounds2D().getHeight()));
			paintShape(g, bounds);
		}
	}
	
	private void paintShape(Graphics g,Rectangle2D bounds){
		g.setColor(color);
		
		if(this.shape instanceof Ellipse2D){
			g.fillOval((int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getWidth(), (int)bounds.getHeight());
		}
	}
	
	private Point2D getRoot(){
		return new Point2D.Float(normalizeX(0), normalizeY(0));
	}
	
	private float normalizeY(float y){
		return this.height - (y-minimum.getValue())*(this.height/(this.maximum.getValue()-this.minimum.getValue()));
	}
	private float normalizeX(long x){
		float point = (((float)this.timespan)*TIMESPAN_PAST)+x;
		float factor = (this.width/((float)this.timespan));
		float temp = point*factor;
		return temp;
	}
	
	
	
}
