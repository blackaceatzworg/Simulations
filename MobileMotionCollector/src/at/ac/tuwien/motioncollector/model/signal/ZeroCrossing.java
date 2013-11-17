package at.ac.tuwien.motioncollector.model.signal;

import java.util.Date;

public class ZeroCrossing {
	private double previous, next;
	private Date date;
	
	public ZeroCrossing() {
		// TODO Auto-generated constructor stub
	}
	
	public ZeroCrossing(double previous, double next, Date date) {
		super();
		this.previous = previous;
		this.next = next;
		this.date = date;
	}

	public double getPrevious() {
		return previous;
	}

	public void setPrevious(double previous) {
		this.previous = previous;
	}

	public double getNext() {
		return next;
	}

	public void setNext(double next) {
		this.next = next;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Type getType(){
		if(previous>0 && next<0){
			return Type.DESC;
		}else if(previous<0 && next>0){
			return Type.ASC;
		}else{
			return Type.NO_CROSSING;
		}
	}
	
	public static enum Type{
		ASC,DESC, NO_CROSSING;
	}
	
	
	
}
