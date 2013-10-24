package at.ac.tuwien.motioncapture;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.motioncapture.listeners.ImageHighlightOnTouchListener;

public class Main extends Activity {
	private boolean running;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView startButtonImage = (ImageView) findViewById(R.id.startButtonImage);
		Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnTouchListener(new ImageHighlightOnTouchListener(
				startButtonImage));
		this.running = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void startButtonClicked(View view) {
		this.running = !running;
		TextView startlabel = (TextView) findViewById(R.id.startLabel);
		if (running) {
			startlabel.setText(R.string.zum_stoppen_antippen);
			
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Capture running!")
					.setContentText("Turn it off to save Battery.");
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(this, Main.class);

			// The stack builder object will contain an artificial back stack
			// for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out
			// of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(Main.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			mBuilder.setProgress(1, 0, true);
			mBuilder.setOngoing(true);
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(1, mBuilder.build());
		}else{
			startlabel.setText(R.string.zum_starten_antippen);
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(1);
		}
	}

}
