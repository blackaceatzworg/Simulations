package at.ac.tuwien.motioncapture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.motioncapture.listeners.ImageHighlightOnTouchListener;

public class Main extends Activity {

	Intent senderService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView startButtonImage = (ImageView) findViewById(R.id.startButtonImage);
		Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnTouchListener(new ImageHighlightOnTouchListener(
				startButtonImage));
		
		
		senderService = new Intent(this,OSCSenderService.class);
		
		
		setRunningState(OSCSenderService.isRunning());
		
	}

	private void setRunningState(boolean isRunning){
		TextView startlabel = (TextView) findViewById(R.id.startLabel);
		if(isRunning){
			startlabel.setText(R.string.zum_stoppen_antippen);
		}else{
			startlabel.setText(R.string.zum_starten_antippen);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void startButtonClicked(View view) {
		
		if (!OSCSenderService.isRunning()) {
			startService(senderService);
			setRunningState(true);
		}else{
			stopService(senderService);
			setRunningState(false);
		}
	}

}
