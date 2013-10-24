package at.ac.tuwien.motioncapture.util;

import java.math.BigDecimal;

public class MathUtil {
	public static float round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
}
