package at.ac.tuwien.motioncollector.ui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JList;

import at.ac.tuwien.motioncollector.model.*;
import at.ac.tuwien.motioncollector.model.ui.DeviceListModel;

public class DevicesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1007686197787286366L;

	JList<Device> deviceList;
	
	/**
	 * Create the panel.
	 */
	public DevicesPanel() {
		initialize();

	}
	
	private void initialize(){
		deviceList = new JList<Device>(new DeviceListModel());
		
		
		setLayout(new BorderLayout(0, 0));
		add(deviceList, BorderLayout.CENTER);
	}

}
