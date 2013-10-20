package com.pfeiffer.motioncapture.util;

public class Filter {
	public static float[] lowPass( float[] input, float[] output,float alpha ) {
	    if ( output == null ) return input;     
	    for ( int i=0; i<input.length; i++ ) {
	        output[i] = output[i] + alpha * (input[i] - output[i]);
	    }
	    return output;
	}
}
