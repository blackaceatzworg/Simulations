package at.ac.tuwien.motioncollector.model.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import at.ac.tuwien.motioncollector.model.Device;

public class DeviceListModel extends AbstractListModel<Device> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2317768443315824981L;

	private List<Device> collection ;
	
	
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
		
		fireEvent(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, this.collection.indexOf(device), this.collection.indexOf(device)));
		
	}
	
	public List<Device> getElements(){
		return this.collection;
	}
	

	@Override
	public void addListDataListener(ListDataListener l) {
		this.listenerList.add(ListDataListener.class, l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		this.listenerList.remove(ListDataListener.class, l);
	}
	
	private void fireEvent(ListDataEvent event){
		for(ListDataListener listener: this.listenerList.getListeners(ListDataListener.class)){
			listener.contentsChanged(event);
		}
	}

}
