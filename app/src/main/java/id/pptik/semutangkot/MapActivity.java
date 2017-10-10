package id.pptik.semutangkot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;

import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.Cctv;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;
import id.pptik.semutangkot.networking.RequestRest;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;

public class MapActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, RestResponHandler {



    private Context mContext;
    private MapView mapView;
    private LoadingIndicator indicator;
    private GSONSharedPreferences gPrefs;
    private Profile mProfile;
    private ArrayList<Cctv> cctvs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;
        getSupportActionBar().hide();
        indicator = new LoadingIndicator(mContext);
        gPrefs = new GSONSharedPreferences(mContext);
        try {
            mProfile = (Profile) gPrefs.getObject(new Profile());
        } catch (ParsingException e) {
            e.printStackTrace();
        }

        populateCctvData();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        setupMap();
    }

    private void populateCctvData() {
        indicator.show();
        RequestRest.bandungCctv(mProfile.getToken(), this);

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

    private void addCctvtoMap(){
        for(int i = 0; i < cctvs.size(); i++){
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(
                    cctvs.get(i).getLatitude(),
                    cctvs.get(i).getLongitude()
            ));
            marker.setRelatedObject(cctvs.get(i));
            mapView.getOverlayManager().add(marker);
        }
        mapView.invalidate();
    }


    @Override
    public void onFinishRequest(JSONObject jResult, String type) {
        indicator.hide();
        switch (type){
            case RequestRest.ENDPOINT_ERROR:
                CommonDialogs.showEndPointError(mContext);
                break;
            case RequestRest.ENDPOINT_CCTV:
                RequestStatus status = new Gson().fromJson(jResult.toString(), RequestStatus.class);
                if(status.getSuccess()){
                    try {
                        JSONArray array = jResult.getJSONArray("data");
                        for(int i = 0; i < array.length(); i++){
                            Cctv cctv = new Gson().fromJson(array.get(i).toString(), Cctv.class);
                            cctvs.add(cctv);
                        }
                        addCctvtoMap();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else CommonDialogs.showRelateError(
                        mContext,
                        status.getMessage(),
                        status.getCode()
                );
                break;
        }
    }
}
