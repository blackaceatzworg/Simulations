package timeline;

import at.ac.tuwien.motioncollector.model.DeviceData;

public class AbsVelocityTimeline extends ComputedTimeline {

	public AbsVelocityTimeline() {
		super();
		this.setMaxMin(0);
		this.setMinMax(2);
	}
	
	@Override
	public TimelineData computeData(DeviceData data) {
		return new TimelineData(data.getReceiveDate(), data.getVelocity().getAbs());
	}

}
