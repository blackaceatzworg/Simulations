package timeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import at.ac.tuwien.motioncollector.model.DeviceData;

public class CorrelationTimline extends ComputedTimeline {

	private SortedMap<Date, DeviceData> data ;
	
	public CorrelationTimline() {
		super();
		this.data = new TreeMap<Date, DeviceData>();
		this.setMaxMin(-1);
		this.setMinMax(1);
	}
	@Override
	public TimelineData computeData(DeviceData data) {
		
		this.data.put(data.getReceiveDate(), data);
		
		this.data = this.data.tailMap(new Date(new Date().getTime()-500));
		List<DeviceData> list = new ArrayList<DeviceData>(this.data.values());
	
		if(list.size()<2){
			return new TimelineData(data.getReceiveDate(), 0);
		}
		double[] axisData = new double[list.size()];
		double[] absData =  new double[list.size()];
		
		for(int i = 0; i< list.size();i++){
			axisData[i] = list.get(i).getVelocity().getZ();
			absData[i] = list.get(i).getVelocity().getAbs();
		}
		
		
		PearsonsCorrelation cor = new PearsonsCorrelation();
//		Mean mean = new Mean();
		
//		if( (mean.evaluate(axisData) < 0.5 && mean.getResult() > 0.5 ) &&  mean.evaluate(absData) >0.5 && mean.getResult() < 0.5 ){
//			return new TimelineData(data.getReceiveDate(),0);
//		}
		
		return new TimelineData(data.getReceiveDate(),(float) cor.correlation(axisData, absData));
		//return new TimelineData(data.getReceiveDate(),(float) axisMean.evaluate(axisData));
		
	}
	

	
	private TimelineData pearsonCorrelation(DeviceData data){
this.data.put(data.getReceiveDate(), data);
		
		this.data = this.data.tailMap(new Date(new Date().getTime()-300));
		List<DeviceData> list = new ArrayList<DeviceData>(this.data.values());
	
		if(list.size()<2){
			return new TimelineData(data.getReceiveDate(), 0);
		}
		double[] axisData = new double[list.size()];
		double[] absData =  new double[list.size()];
		
		for(int i = 0; i< list.size();i++){
			axisData[i] = list.get(i).getVelocity().getZ();
			absData[i] = list.get(i).getVelocity().getAbs();
		}
		
		PearsonsCorrelation cor = new PearsonsCorrelation();
		
		return new TimelineData(data.getReceiveDate(),(float) cor.correlation(axisData, absData));
	}

}
