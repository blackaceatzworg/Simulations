package at.ac.tuwien.motioncapture.listeners;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageHighlightOnTouchListener implements OnTouchListener {

	final ImageView image;
	private static final int TRANSPARENT_GREY = Color.argb(0, 185, 185, 185);
	private static final int FILTERED_GREY = Color.argb(155, 185, 185, 185);
	public ImageHighlightOnTouchListener(ImageView image) {
		super();
		this.image = image;
	}
	
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
	        image.setColorFilter(FILTERED_GREY);
	      } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
	        image.setColorFilter(TRANSPARENT_GREY); // or null
	      }
		return false;
	}

}
