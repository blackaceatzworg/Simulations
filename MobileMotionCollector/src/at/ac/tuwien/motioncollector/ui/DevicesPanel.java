package at.ac.tuwien.motioncollector.ui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JList;

import at.ac.tuwien.motioncollector.model.*;
import at.ac.tuwien.motioncollector.model.ui.DeviceListModel;
import javax.swing.border.EmptyBorder;

public class DevicesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1007686197787286366L;

	JList<Device> deviceList;
	private DeviceListModel listModel;
	
	/**
	 * Create the panel.
	 */
	public DevicesPanel() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		initialize();
	}
	
	public List<Device> getDeviceList() {
		return listModel.getElements();
	}
	
	public void addDevice(Device device){
		this.listModel.addElement(device);
		this.deviceList.repaint();
	}
	
	private void initialize(){
		this.listModel = new DeviceListModel();
		deviceList = new JList<Device>(this.listModel);
		setLayout(new BorderLayout(0, 0));
		add(deviceList, BorderLayout.CENTER);
	}

}
