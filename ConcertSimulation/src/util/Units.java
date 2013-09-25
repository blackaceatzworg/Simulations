package util;

public interface Units {
	enum Time implements Units{
		Milliseconds,Seconds,Minutes,Hours
	}
	enum Distance{
		Millimeter,Centimeter,Meter,Kilometer
	}
	
}
