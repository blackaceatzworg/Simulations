package at.ac.tuwien.motioncollector.ui;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.border.EmptyBorder;

import at.ac.tuwien.motioncollector.model.Axis;
import at.ac.tuwien.motioncollector.model.Device;
import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.model.Timeline;
import at.ac.tuwien.motioncollector.model.TimelineData;
import at.ac.tuwien.motioncollector.model.ui.VelocityTimeline;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TimelinePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Device, ArrayList<VelocityTimeline>> velocityTimelines;
	JPanel drawingPanel;
	private int deviceCount;
	private Color[] colors= {Color.black,Color.cyan,Color.magenta};;
	/**
	 * Create the panel.
	 */
	public TimelinePanel() {
		
		velocityTimelines = new TreeMap<Device, ArrayList<VelocityTimeline>>();
		deviceCount = 0;
		initialize();
	}
	
	private void initialize(){
		setBorder(new EmptyBorder(10, 0, 10, 10));
		setLayout(new BorderLayout(0, 0));
		
		drawingPanel = new JPanel(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				for(ArrayList<VelocityTimeline> timelineList: velocityTimelines.values()){
					for(Timeline timeline: timelineList){
						timeline.paint(g);
					}
				}
				
			}
			
		};
		drawingPanel.setBackground(Color.WHITE);
		add(drawingPanel, BorderLayout.CENTER);
		drawingPanel.setLayout(null);
	}
	
	
	
	public void removeDevice(Device device){
		velocityTimelines.remove(device);
	}
	
	public void addDevice(Device device){
		
		if(!velocityTimelines.containsKey(device)){
			ArrayList<VelocityTimeline> timelines = new ArrayList<VelocityTimeline>();
			VelocityTimeline absTimeline =new VelocityTimeline(this.colors[this.deviceCount], drawingPanel.getWidth(), drawingPanel.getHeight(), 5000, Axis.Abs);
			absTimeline.setActive(true);
			timelines.add(absTimeline);
			velocityTimelines.put(device, timelines);
			this.deviceCount++;
		}
	}
	
	public void addData(Device device,DeviceData data){
		for(VelocityTimeline vTimeline:this.velocityTimelines.get(device)){
			if(vTimeline.getAxis() == Axis.Abs){
				vTimeline.appendData(new TimelineData(data.getReceiveDate(), data.getVelocity().getAbs()));
			}else if(vTimeline.getAxis() == Axis.X){
				vTimeline.appendData(new TimelineData(data.getReceiveDate(), data.getVelocity().getX()));
			}else if(vTimeline.getAxis() == Axis.Y){
				vTimeline.appendData(new TimelineData(data.getReceiveDate(), data.getVelocity().getY()));
			}else if(vTimeline.getAxis() == Axis.Z){
				vTimeline.appendData(new TimelineData(data.getReceiveDate(), data.getVelocity().getZ()));
			}
		}
		this.drawingPanel.repaint();
	}

}
