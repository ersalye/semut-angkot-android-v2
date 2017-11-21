package id.pptik.semutangkot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.services.LocationUpdatesService;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.ProfileUtils;
import id.pptik.semutangkot.utils.Utils;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationUpdatesService mService = null;
    private boolean mBound = false;
    private GridMenuFragment mGridMenuFragment;
    Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.bg_pasopati);



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

        setDrawer();
    }


    private void setDrawer(){
        SlidingRootNav nav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withContentClickableWhenMenuOpened(true)
                .withToolbarMenuToggle(toolbar)
                .inject();

        SlidingRootNavLayout navLay = nav.getLayout();
        CircleImageView profileImage = navLay.findViewById(R.id.profile_image);
        profileImage.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_account_circle,
                96, R.color.primary_dark
        ));
        Profile profile = ProfileUtils.getProfile(this);
        Picasso.with(this).load(profile.getProfile().getPicture()).into(profileImage);

        String tmp = "<b>"+profile.getProfile().getDisplayName()+"</b>";
        TextView displayName = navLay.findViewById(R.id.name_text);
        displayName.setText(Html.fromHtml(tmp));

        TextView emailText = navLay.findViewById(R.id.email_text);
        emailText.setText(profile.getEmail());

        ImageView settingIcon = navLay.findViewById(R.id.setting_icon);
        ImageView logoutIcon = navLay.findViewById(R.id.logout_icon);
        ImageView aboutIcon = navLay.findViewById(R.id.about_icon);

        aboutIcon.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_lightbulb_outline,
                34, R.color.white
        ));
        settingIcon.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_settings_applications,
                34, R.color.white
        ));
        logoutIcon.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_exit_to_app,
                34, R.color.white
        ));

        navLay.findViewById(R.id.setting_layout).setOnClickListener(view -> {
            // handle settings click
        });

        navLay.findViewById(R.id.logout_layout).setOnClickListener(view -> {
            // handle logout click
        });

    }



    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Public Trans.", R.drawable.ic_directions_bus_white_48dp));
        menus.add(new GridMenu("CCTV Viewer", R.drawable.ic_videocam_white_48dp));
        menus.add(new GridMenu("Cek KIR", R.drawable.ic_rv_hookup_white_48dp));
        menus.add(new GridMenu("Booking Angkot", R.drawable.ic_airport_shuttle_white_48dp));
        menus.add(new GridMenu("Social Report", R.drawable.ic_rate_review_white_48dp));
        menus.add(new GridMenu("Bike Comm.", R.drawable.ic_directions_bike_white_48dp));

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