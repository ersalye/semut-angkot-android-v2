package id.pptik.semutangkot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.helper.BroadcastManager;
import id.pptik.semutangkot.services.LocationUpdatesService;
import id.pptik.semutangkot.utils.Utils;

public class MainActivity extends AppCompatActivity implements BroadcastManager.UIBroadcastListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationUpdatesService mService = null;
    private boolean mBound = false;
    private BroadcastManager mBroadcastManager;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mBroadcastManager = new BroadcastManager(this);
        mBroadcastManager.subscribeToUi(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);



        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (Utils.requestingLocationUpdates(MainActivity.this)) {
                    Log.i(TAG, "Location update already instance, restart");
                    mService.removeLocationUpdates();
                    mService.requestLocationUpdates();
                }else mService.requestLocationUpdates();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }

        super.onStop();
    }

    @Override
    public void onMessageReceived(String type, Object msg) {
        if(type.equals(AppPreferences.BROADCAST_TYPE_LOCATION)){
            Location location = (Location) msg;
            Log.i(TAG, "Location Update "+location.getLatitude()+", "+location.getLongitude());
        }
    }
}