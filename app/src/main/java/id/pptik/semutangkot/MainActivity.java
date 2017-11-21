package id.pptik.semutangkot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import java.util.ArrayList;
import java.util.List;

import id.pptik.semutangkot.helper.BroadcastManager;
import id.pptik.semutangkot.services.LocationUpdatesService;
import id.pptik.semutangkot.utils.Utils;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationUpdatesService mService = null;
    private boolean mBound = false;
    private GridMenuFragment mGridMenuFragment;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.bg_burn);



        setupGridMenu();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main_frame, mGridMenuFragment);
        tx.commit();

        mGridMenuFragment.setOnClickMenuListener((gridMenu, position) -> {
            switch (position){
                case 0:
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                    break;
            }
        });

        SlidingRootNav nav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withContentClickableWhenMenuOpened(true)
                .withToolbarMenuToggle(toolbar)
                .inject();

        SlidingRootNavLayout navLay = nav.getLayout();
        TextView test = navLay.findViewById(R.id.test);
        test.setText("TEST");



    }



    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Public Trans.", R.drawable.common_full_open_on_phone));
        menus.add(new GridMenu("CCTV Viewer", R.drawable.common_full_open_on_phone));
        menus.add(new GridMenu("Cek KIR", R.drawable.common_full_open_on_phone));
        menus.add(new GridMenu("Booking Angkot", R.drawable.common_full_open_on_phone));
        menus.add(new GridMenu("Social Report", R.drawable.common_full_open_on_phone));
        menus.add(new GridMenu("Bike Community", R.drawable.common_full_open_on_phone));

        mGridMenuFragment.setupMenu(menus);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
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
}