package id.pptik.semutangkot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;
import id.pptik.semutangkot.networking.CommonRest;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.ProfileUtils;

public class BikeMapActivity extends AppCompatActivity {

    private MapView mapView;
    private LoadingIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        indicator = new LoadingIndicator(this);
        indicator.show();
        Profile profile = ProfileUtils.getProfile(this);
        CommonRest.getBike(profile.getToken(), (jResult, type) -> {
            indicator.hide();
            Log.i("bike", jResult.toString());
            RequestStatus status = ProfileUtils.getReqStatus(jResult.toString());
            if(type.equals(CommonRest.ENDPOINT_ERROR)) CommonDialogs.showEndPointError(BikeMapActivity.this);
            else {
                if(!status.getSuccess()) CommonDialogs.showError(BikeMapActivity.this, status.getMessage());
                else {
                    try {
                        JSONArray data = jResult.getJSONArray("data");
                        if(data.length() > 0){
                            double lat = 0, lon = 0;
                            for(int i = 0; i < data.length(); i++){
                                lat = data.getJSONObject(i).getJSONObject("Profile")
                                        .getJSONObject("location")
                                        .getJSONArray("coordinates").getDouble(1);
                                lon = data.getJSONObject(i).getJSONObject("Profile")
                                        .getJSONObject("location")
                                        .getJSONArray("coordinates").getDouble(0);
                                Marker marker = new Marker(mapView);
                                marker.setPosition(new GeoPoint(lat, lon));
                                marker.setIcon(CustomDrawable.googleMaterial(
                                        BikeMapActivity.this,
                                        GoogleMaterial.Icon.gmd_directions_bike,
                                        32,
                                        R.color.colorPrimaryDark
                                ));
                                marker.setTitle("Sekitar "+ProfileUtils.getDistance(
                                        BikeMapActivity.this, lat, lon
                                )+" KM dari lokasi Anda");
                                mapView.getOverlays().add(marker);
                            }
                            mapView.invalidate();
                            mapView.getController().animateTo(new GeoPoint(lat, lon));
                        }else {
                            mapView.getController().setZoom(5);
                            Toast.makeText(BikeMapActivity.this,
                                    "Tidak ditemukan pengguna sepeda", Toast.LENGTH_LONG).show();
                            mapView.getController().animateTo(new GeoPoint(-0.789275, 113.92132700000002));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mapView = findViewById(R.id.mapview);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(20);
        mapView.getController().setZoom(7);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
