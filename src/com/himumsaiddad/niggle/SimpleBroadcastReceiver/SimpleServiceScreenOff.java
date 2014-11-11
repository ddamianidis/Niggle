package com.himumsaiddad.niggle.SimpleBroadcastReceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
public class SimpleServiceScreenOff extends Service {
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this,"Service off created ...",
		Toast.LENGTH_LONG).show();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service off destroyed ...",
		Toast.LENGTH_LONG).show();
	}
}