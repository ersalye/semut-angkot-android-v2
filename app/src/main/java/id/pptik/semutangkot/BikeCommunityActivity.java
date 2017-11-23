package id.pptik.semutangkot;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.utils.CustomDrawable;

public class BikeCommunityActivity extends AppCompatActivity {

    private LinearLayout shareLayout;
    private ImageView shareIcon;
    private TextView shareText;
    private Context context;
    private AppPreferences preferences;
    private boolean isShared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_community);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shareLayout = findViewById(R.id.share_layout);
        shareIcon = findViewById(R.id.share_icon);
        shareText = findViewById(R.id.title_text);

        context = this;
        preferences = new AppPreferences(context);
        isShared = preferences.getBoolean(AppPreferences.KEY_IS_BIKER, false);

        if(!isShared) {
            enable();
            shareLayout.setOnClickListener(view -> {
                preferences.put(AppPreferences.KEY_IS_BIKER, true);
                Toast.makeText(context, "Sharing Lokasi diaktifkan", Toast.LENGTH_LONG).show();
                disable();
            });
        }else {
            disable();
            shareLayout.setOnClickListener(view -> {
                preferences.put(AppPreferences.KEY_IS_BIKER, false);
                Toast.makeText(context, "Sharing Lokasi dinonaktifkan", Toast.LENGTH_LONG).show();
                enable();
            });
        }
    }


    private void enable(){
        shareIcon.setImageDrawable(CustomDrawable.googleMaterial(
                this,
                GoogleMaterial.Icon.gmd_all_out, 120, R.color.colorPrimaryDark
        ));
        shareText.setText("AKTIFKAN SHARING LOKASI\nBIKE COMMUNITY");
    }

    private void disable(){
        shareIcon.setImageDrawable(CustomDrawable.googleMaterial(
                this,
                GoogleMaterial.Icon.gmd_power_settings_new, 120, R.color.cochineal_red
        ));
        shareText.setText("NONAKTIFKAN SHARING LOKASI\nBIKE COMMUNITY");
    }


}
