package at.ac.tuwien.motioncollector.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import timeline.AbsVelocityTimeline;
import timeline.ComputedTimeline;
import timeline.CorrelationTimline;
import timeline.JumpRecognitionTimeline;
import timeline.XVelocityTimeline;
import timeline.ZVelocityTimeline;
import at.ac.tuwien.motioncollector.model.Device;
import at.ac.tuwien.motioncollector.model.DeviceData;

public class DeviceDataWindow extends JFrame {

	private static final long serialVersionUID = 6564521101295808760L;

	private Device device;
	
	private List<TimelinePanel<? extends ComputedTimeline>> panels;
	private ComputationPanel compPanel;
	
	public DeviceDataWindow(Device device ) {
		this.device = device;
		
		this.panels = new LinkedList<TimelinePanel<? extends ComputedTimeline>>();
		
		this.panels.add(new TimelinePanel<AbsVelocityTimeline>(AbsVelocityTimeline.class, Color.black, 5000));
		this.panels.add(new TimelinePanel<ZVelocityTimeline>(ZVelocityTimeline.class, Color.black, 5000));
		this.panels.add(new TimelinePanel<CorrelationTimline>(CorrelationTimline.class, Color.black, 5000));
		
		compPanel = new ComputationPanel(Color.black,5000);
		initialize();
	}
	
	private void initialize(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ApplicationContainer.getInstance().getDeviceDataWindowHandler().unregisterWindow(DeviceDataWindow.this);
				super.windowClosing(e);
			}
		});
		
		for(TimelinePanel<? extends ComputedTimeline> panel : this.panels){
			this.getContentPane().add(panel);
			panel.setActive(true);
		}
		
		this.getContentPane().add(this.compPanel);
		
		
	}
	public Device getDevice() {
		return device;
	}
	public void addData(DeviceData data){
		
		for(TimelinePanel<? extends ComputedTimeline> panel : this.panels){
			panel.addData(data);
		}
		
		this.compPanel.addData(data);
	}
	

}
