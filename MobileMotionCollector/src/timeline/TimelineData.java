package timeline;

import java.util.Date;

public class TimelineData implements Comparable<TimelineData> {
	private float value;
	private Date date;
	
	
	public TimelineData(Date date , float value) {
		super();
		this.value = value;
		this.date = date;
	}

	public void setValue(float value) {
		this.value = value;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public float getValue() {
		return value;
	}
	public Date getDate() {
		return date;
	}

	@Override
	public int compareTo(TimelineData o) {
		return this.date.compareTo(o.date);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TimelineData))
			return false;
		TimelineData other = (TimelineData) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}
	
	public TimelineData clone(){
		return new TimelineData(getDate(), getValue());
		
	}

	
	
}
