package at.ac.tuwien.motioncollector.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import at.ac.tuwien.motioncollector.model.Device;
import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.model.TimelineData;

public class DeviceDataWindow extends JFrame {

	private static final long serialVersionUID = 6564521101295808760L;

	private Device device;
	
	private TimelinePanel absPanel;
	private TimelinePanel xPanel;
	private TimelinePanel yPanel;
	private TimelinePanel zPanel;
	
	/**
	 * Create the frame.
	 */
	public DeviceDataWindow(Device device ) {
		this.device = device;
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
		
		this.absPanel = new TimelinePanel(Color.black,5000);
		this.xPanel = new TimelinePanel(Color.black,5000);
		this.yPanel = new TimelinePanel(Color.black,5000);
		this.zPanel =  new TimelinePanel(Color.black,5000);
		
		this.getContentPane().add(absPanel);
		this.getContentPane().add(xPanel);
		this.getContentPane().add(yPanel);
		this.getContentPane().add(zPanel);
		
		this.absPanel.setActive(true);
		this.xPanel.setActive(true);
		this.yPanel.setActive(true);
		this.zPanel.setActive(true);
		
	}
	public Device getDevice() {
		return device;
	}
	public void addData(DeviceData data){
		
		absPanel.addData(new TimelineData(data.getReceiveDate(), data.getVelocity().getAbs()));
		xPanel.addData(new TimelineData(data.getReceiveDate(), data.getVelocity().getX()));
		yPanel.addData(new TimelineData(data.getReceiveDate(), data.getVelocity().getY()));
		zPanel.addData(new TimelineData(data.getReceiveDate(), data.getVelocity().getZ()));
	}
	

}
