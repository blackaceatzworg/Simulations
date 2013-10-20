package at.ac.tuwien.motioncollector.model.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import at.ac.tuwien.motioncollector.model.Device;

public class DeviceListModel implements ListModel<Device> {

	List<Device> collection ;
	
	public DeviceListModel() {
		this.collection = new ArrayList<Device>();
	}
	
	
	@Override
	public int getSize() {
		return collection.size();
	}

	@Override
	public Device getElementAt(int index) {
		return collection.get(index);
	}
	
	public void addElement(Device device){
		this.collection.add(device);
		
		Collections.sort(this.collection, new Comparator<Device>() {

			@Override
			public int compare(Device o1, Device o2) {
				return o1.getMacAddress().compareTo(o2.getMacAddress());
			}
		});
		
	}
	

	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

}
