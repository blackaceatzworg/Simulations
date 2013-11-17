package timeline;

import at.ac.tuwien.motioncollector.model.DeviceData;

public class XVelocityTimeline extends ComputedTimeline {

	@Override
	public TimelineData computeData(DeviceData data) {
		return new TimelineData(data.getReceiveDate(), data.getVelocity().getX());
	}

}
