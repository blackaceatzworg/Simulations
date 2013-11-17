package timeline;


import at.ac.tuwien.motioncollector.model.DeviceData;

public abstract class ComputedTimeline extends Timeline {
	
	public void appendData(DeviceData data) {
		this.appendData(this.computeData(data));
	}
	
	public abstract TimelineData computeData(DeviceData data);
}
