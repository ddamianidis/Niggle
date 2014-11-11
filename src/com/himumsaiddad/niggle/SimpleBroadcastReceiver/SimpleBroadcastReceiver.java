package com.himumsaiddad.niggle.SimpleBroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class SimpleBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context rcvContext, Intent rcvIntent) {
	String action = rcvIntent.getAction();
		if (action.equals(Intent.ACTION_SCREEN_OFF)) {
			
			rcvContext.startService(new Intent(rcvContext, SimpleServiceScreenOff.class));
		}else if (action.equals(Intent.ACTION_SCREEN_ON)) {
			rcvContext.startService(new Intent(rcvContext, SimpleServiceScreenOn.class));
		}
	}
}