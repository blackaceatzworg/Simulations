package timeline;

import at.ac.tuwien.motioncollector.model.DeviceData;

public class ZVelocityTimeline extends ComputedTimeline {

	@Override
	public TimelineData computeData(DeviceData data) {
		return new TimelineData(data.getReceiveDate(),data.getVelocity().getZ());
	}

}
