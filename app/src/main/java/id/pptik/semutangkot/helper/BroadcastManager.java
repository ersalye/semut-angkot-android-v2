package id.pptik.semutangkot.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.util.Log;


public class BroadcastManager {
    private Context context;
    private boolean isSubsribing = false;

    public interface UIBroadcastListener{
        public void onMessageReceived(String type, Object msg);
    }

    BroadcastReceiver uiReceiver;

    public BroadcastManager(Context ctx){
        context = ctx;
    }


    public void sendBroadcastToUI(String broadcastType, Object msg){
        Intent intent = new Intent();
        intent.setAction(AppPreferences.ACTION_BROADCAST_LOCATION);
        if(broadcastType.equals(AppPreferences.BROADCAST_TYPE_LOCATION)){
            Location location = (Location) msg;
            intent.putExtra(AppPreferences.INTENT_BROADCAST_MSG, location);
        }
        intent.putExtra(AppPreferences.INTENT_BROADCAST_TYPE, broadcastType);
        context.sendBroadcast(intent);
    }

    public boolean isSubsribed(){
        return isSubsribing;
    }

    public void subscribeToUi(final UIBroadcastListener listener){
        isSubsribing = true;
        IntentFilter intentFilter = new IntentFilter(AppPreferences.ACTION_BROADCAST_LOCATION);
        uiReceiver = new BroadcastReceiver() {
            String resType = "";
            Object _msg;
            @Override
            public void onReceive(Context context, Intent intent) {
                resType = intent.getStringExtra(AppPreferences.INTENT_BROADCAST_TYPE);
                if(resType.equals(AppPreferences.BROADCAST_TYPE_LOCATION))
                    _msg = intent.getParcelableExtra(AppPreferences.INTENT_BROADCAST_MSG);
                listener.onMessageReceived(resType, _msg);
            }
        };
        context.registerReceiver(uiReceiver, intentFilter);
    }


    public void unSubscribeFromUi(){
        context.unregisterReceiver(uiReceiver);
        Log.i(this.getClass().getSimpleName(), "Unsubscribe from UI");
    }


}