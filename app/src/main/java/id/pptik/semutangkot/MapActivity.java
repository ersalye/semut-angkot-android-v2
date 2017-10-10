package id.pptik.semutangkot;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;
import com.github.hynra.wortel.BrokerCallback;
import com.github.hynra.wortel.Consumer;
import com.github.hynra.wortel.Factory;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.rabbitmq.client.Channel;

import net.grandcentrix.tray.core.OnTrayPreferenceChangeListener;
import net.grandcentrix.tray.core.TrayItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.helper.map.MarkerBearing;
import id.pptik.semutangkot.helper.map.osm.MarkerClick;
import id.pptik.semutangkot.helper.map.osm.OSMarkerAnimation;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.Cctv;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.models.angkot.AngkotPost;
import id.pptik.semutangkot.networking.RequestRest;
import id.pptik.semutangkot.ui.AnimationView;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.StringResources;

public class MapActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, RestResponHandler,
        Marker.OnMarkerClickListener, BrokerCallback, CompoundButton.OnCheckedChangeListener {



    private Context mContext;
    private MapView mapView;
    RelativeLayout mMarkerDetailLayout;
    private LoadingIndicator indicator;
    private GSONSharedPreferences gPrefs;
    private Profile mProfile;
    private ArrayList<Cctv> cctvs = new ArrayList<>();
    private MarkerClick markerClick;
    private Factory mqFactory;
    private Consumer mqConsumer;
    private final String TAG = this.getClass().getSimpleName();
    private boolean isFirsInit = true;
    private Marker[] markers;
    private Angkot[] angkots;
    private OSMarkerAnimation markerAnimation;
    private AngkotPost[] angkotPosts;
    private FloatingActionButton mClosePopup;
    private Animation slideDown;
    private boolean isActivityPause = false;
    private AppPreferences appPreferences;
    View customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mMarkerDetailLayout = findViewById(R.id.markerdetail_layout);
        mClosePopup = findViewById(R.id.close_popup);
        mClosePopup.setImageDrawable(
                CustomDrawable.googleMaterial(
                        this,
                        GoogleMaterial.Icon.gmd_clear,
                        46, R.color.colorPrimaryDark
                )
        );
        mClosePopup.setOnClickListener(view -> mMarkerDetailLayout.startAnimation(slideDown));
        AnimationView animationView = new AnimationView(this);
        slideDown = animationView.getAnimation(R.anim.slide_down, anim -> {
            if(mMarkerDetailLayout.getVisibility() == View.VISIBLE)
                mMarkerDetailLayout.setVisibility(View.GONE);
        });


        mContext = this;
        appPreferences = new AppPreferences(mContext);
        appPreferences.registerOnTrayPreferenceChangeListener(items -> {

        });

        markerAnimation = new OSMarkerAnimation();
        getSupportActionBar().hide();
        indicator = new LoadingIndicator(mContext);
        gPrefs = new GSONSharedPreferences(mContext);
        try {
            mProfile = (Profile) gPrefs.getObject(new Profile());
        } catch (ParsingException e) {
            e.printStackTrace();
        }

        markerClick = new MarkerClick(mContext, mMarkerDetailLayout);

        populateCctvData();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        setupMap();
        setFilterLayout();
    }


    private void connectToRabbit() {
        mqFactory = new Factory(StringResources.get(R.string.MQ_HOSTNAME),
                StringResources.get(R.string.MQ_VIRTUAL_HOST),
                StringResources.get(R.string.MQ_USERNAME),
                StringResources.get(R.string.MQ_PASSWORD),
                StringResources.get(R.string.MQ_EXCHANGE_NAME),
                StringResources.get(R.string.MQ_DEFAULT_ROUTING_KEY),
                Integer.parseInt(StringResources.get(R.string.MQ_PORT)));
        mqConsumer = this.mqFactory.createConsumer(this);
        consume();
    }


    private void consume(){

        mqConsumer.setQueueName("");
        mqConsumer.setExchange(StringResources.get(R.string.MQ_EXCHANGE_NAME_ANGKOT));
        mqConsumer.setRoutingkey(StringResources.get(R.string.MQ_BROADCAST_ROUTING_KEY));
        mqConsumer.subsribe();
        mqConsumer.setMessageListner(delivery -> {
            try {
                final String message = new String(delivery.getBody(), "UTF-8");
                Log.i(TAG, "-------------------------------------");
                Log.i(TAG, "incoming message");
                Log.i(TAG, "-------------------------------------");
                Log.i(TAG, message);
                if(mMarkerDetailLayout.getVisibility() == View.GONE)
                    if(!isActivityPause) populateMsg(message);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }


    private void populateMsg(String msg){
        try {
            JSONObject mainObject = new JSONObject(msg);
            JSONArray angkotArray = mainObject.getJSONArray("angkot");
            JSONArray postArray = mainObject.getJSONArray("laporan");
            // angkot
            if(isFirsInit){
                isFirsInit = false;
                indicator.hide();
                angkots = new Angkot[angkotArray.length()];
                markers = new Marker[angkotArray.length()];
                for (int i = 0; i < angkotArray.length(); i++) {
                    angkots[i] = new Gson().fromJson(angkotArray.get(i).toString(), Angkot.class);
                    markers[i] = new Marker(mapView);
                    markers[i].setPosition(new GeoPoint(angkots[i].getAngkot().getLocation().getCoordinates().get(1),
                            angkots[i].getAngkot().getLocation().getCoordinates().get(0)));
                    markers[i].setIcon(getResources().getDrawable(R.drawable.tracker_angkot));
                    markers[i].setRelatedObject(angkots[i]);
                    markers[i].setOnMarkerClickListener(this);
                    markers[i].setEnabled(
                            appPreferences.getBoolean(AppPreferences.KEY_SHOW_ANGKOT, true)
                    );
                    mapView.getOverlays().add(markers[i]);
                    mapView.invalidate();
                }
                //setListView();
                //animateToSelected();
            }else {
                if (angkotArray.length() == angkots.length) {
                    for (int i = 0; i < angkotArray.length(); i++) {
                        JSONObject entity = null;
                        try {
                            entity = angkotArray.getJSONObject(i);
                            Angkot angkot = new Gson().fromJson(entity.toString(), Angkot.class);
                            if (angkots[i].getAngkot().getPlatNomor().equals(angkot.getAngkot().getPlatNomor())) { // update markers
                                angkots[i] = new Gson().fromJson(entity.toString(), Angkot.class);
                                if (markers[i].getPosition().getLatitude() != angkots[i].getAngkot().getLocation().getCoordinates().get(1) ||
                                        markers[i].getPosition().getLongitude() != angkots[i].getAngkot().getLocation().getCoordinates().get(0)) {
                                    double bearing = MarkerBearing.bearing(markers[i].getPosition().getLatitude(), markers[i].getPosition().getLongitude(),
                                            angkots[i].getAngkot().getLocation().getCoordinates().get(1), angkots[i].getAngkot().getLocation().getCoordinates().get(0));
                                    markers[i].setRelatedObject(angkots[i]);
                                    markers[i].setRotation((float) bearing);
                                    markerAnimation.animate(mapView, markers[i],
                                            new GeoPoint(angkots[i].getAngkot().getLocation().getCoordinates().get(1), angkots[i].getAngkot().getLocation().getCoordinates().get(0)),
                                            1500);
                                    //if (checkedState != -1) mapController.setZoom(17);
                                } else {
                                    Log.i(TAG, "Same Position");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                 //   if(listView.getVisibility() == View.GONE) setListView();
                 //   if(isTracked) animateToSelected();
                    // post
                    angkotPosts = new AngkotPost[postArray.length()];
                    for(int i = 0; i < postArray.length(); i++){
                        angkotPosts[i] = new Gson().fromJson(postArray.get(i).toString(), AngkotPost.class);
                    }
                    for(Overlay overlay : mapView.getOverlays()){
                        if(overlay instanceof Marker){
                            if(((Marker) overlay).getRelatedObject() instanceof  AngkotPost){
                                mapView.getOverlays().remove(overlay);
                                mapView.invalidate();
                            }
                        }
                    }
                    // add markers
                    for(int i = 0; i < angkotPosts.length; i++){
                        Marker marker = new Marker(mapView);
                        marker.setPosition(new GeoPoint(angkotPosts[i].getLocation().getCoordinates().get(1),
                                angkotPosts[i].getLocation().getCoordinates().get(0)));
                        marker.setIcon(getResources().getDrawable(R.drawable.angkot_icon));
                        marker.setRelatedObject(angkotPosts[i]);
                        marker.setOnMarkerClickListener(this);
                        marker.setEnabled(
                                appPreferences.getBoolean(AppPreferences.KEY_SHOW_LAPORAN, true)
                        );
                        mapView.getOverlays().add(marker);
                        mapView.invalidate();
                    }
                }else {
                    // found new data
                    isFirsInit = true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                showFilter();
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
            marker.setIcon(getResources().getDrawable(R.drawable.cctv_icon));
            marker.setRelatedObject(cctvs.get(i));
            marker.setOnMarkerClickListener(this);
            marker.setEnabled(
                    appPreferences.getBoolean(AppPreferences.KEY_SHOW_CCTV, true)
            );
            mapView.getOverlayManager().add(marker);
        }
        mapView.invalidate();
    }

    private void setFilterLayout(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.layout_filter, null);
        SwitchCompat angkot = customView.findViewById(R.id.sw_angkot);
        SwitchCompat cctv = customView.findViewById(R.id.sw_cctv);
        SwitchCompat jalur = customView.findViewById(R.id.sw_jalur);
        SwitchCompat laporan = customView.findViewById(R.id.sw_laporan);

        angkot.setChecked(appPreferences.getBoolean(AppPreferences.KEY_SHOW_ANGKOT, true));
        cctv.setChecked(appPreferences.getBoolean(AppPreferences.KEY_SHOW_CCTV, true));
        jalur.setChecked(appPreferences.getBoolean(AppPreferences.KEY_SHOW_JALUR, true));
        laporan.setChecked(appPreferences.getBoolean(AppPreferences.KEY_SHOW_LAPORAN, true));

        angkot.setOnCheckedChangeListener(this);
        cctv.setOnCheckedChangeListener(this);
        jalur.setOnCheckedChangeListener(this);
        laporan.setOnCheckedChangeListener(this);
    }

    private void showFilter(){
        BottomDialog bottomDialog = new BottomDialog.Builder(this)
                .setTitle("Filter Peta")
                .setContent("Atur item mana yang ditampilkan di dalam peta")
                .setCustomView(customView)
                .setIcon(CustomDrawable.googleMaterial(
                        this,
                        GoogleMaterial.Icon.gmd_widgets,
                        46, R.color.colorPrimary
                ))
                .setPositiveText("OK")
                .onPositive(bottomDialog1 -> bottomDialog1.dismiss())
                .build();
        bottomDialog.show();
    }

    @Override
    public void onFinishRequest(JSONObject jResult, String type) {
        if(!type.equals(RequestRest.ENDPOINT_CCTV))
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
                        connectToRabbit();
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

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        markerClick.checkMarker(marker);
        return false;
    }

    @Override
    public void onConnectionSuccess(Channel channel) {
        indicator.hide();
    }

    @Override
    public void onConnectionFailure(String message) {
        Log.i(TAG, message);
        indicator.hide();
        if(isFirsInit)
            CommonDialogs.showEndPointError(mContext);
    }

    @Override
    public void onConnectionClosed(String message) {
        Log.i(TAG, message);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mqConsumer.isConnected())
            mqConsumer.stop();
    }


    @Override
    public void onPause(){
        super.onPause();
        isActivityPause = true;
        if(mqConsumer.isConnected())
            mqConsumer.stop();

    }

    @Override
    public void onResume(){
        super.onResume();
        isActivityPause = false;
        connectToRabbit();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.sw_angkot:
                appPreferences.put(AppPreferences.KEY_SHOW_ANGKOT, b);
                for(Overlay overlay : mapView.getOverlays()){
                    if(overlay instanceof Marker){
                        if(((Marker) overlay).getRelatedObject() instanceof  Angkot){
                            overlay.setEnabled(b);
                        }
                    }
                }
                mapView.invalidate();
                break;
            case R.id.sw_cctv:
                appPreferences.put(AppPreferences.KEY_SHOW_CCTV, b);
                for(Overlay overlay : mapView.getOverlays()){
                    if(overlay instanceof Marker){
                        if(((Marker) overlay).getRelatedObject() instanceof  Cctv){
                            overlay.setEnabled(b);
                        }
                    }
                }
                mapView.invalidate();
                break;
            case R.id.sw_jalur:
                appPreferences.put(AppPreferences.KEY_SHOW_JALUR, b);

                break;
            case R.id.sw_laporan:
                appPreferences.put(AppPreferences.KEY_SHOW_LAPORAN, b);
                for(Overlay overlay : mapView.getOverlays()){
                    if(overlay instanceof Marker){
                        if(((Marker) overlay).getRelatedObject() instanceof  AngkotPost){
                            overlay.setEnabled(b);
                        }
                    }
                }
                break;
        }
    }
}
