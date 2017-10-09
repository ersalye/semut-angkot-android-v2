package id.pptik.semutangkot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

public class MapActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    private Context mContext;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;
        getSupportActionBar().hide();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        setupMap();
    }

    private void setupMap() {
        mapView = findViewById(R.id.mapview);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(false);
        mapView.getController().setZoom(15);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
        GeoPoint g1 = new GeoPoint(-6.885719, 107.613622);
        Marker marker = new Marker(mapView);
        mapView.getOverlayManager().add(marker);
        mapView.getController().animateTo(g1);
        marker.setPosition(g1);
        mapView.invalidate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:

                return true;
            case R.id.navigation_dashboard:

                return true;
            case R.id.navigation_notifications:

                return true;
        }
        return false;
    }
}
