package id.pptik.semutangkot;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.wortel.BrokerCallback;
import com.github.hynra.wortel.Consumer;
import com.github.hynra.wortel.Factory;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rabbitmq.client.Channel;

import net.grandcentrix.tray.core.ItemNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import id.pptik.semutangkot.adapters.AngkotListAdapter;
import id.pptik.semutangkot.adapters.PathListAdapter;
import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.helper.BroadcastManager;
import id.pptik.semutangkot.helper.map.MarkerBearing;
import id.pptik.semutangkot.helper.map.osm.GoogleMapProvider;
import id.pptik.semutangkot.helper.map.osm.MarkerClick;
import id.pptik.semutangkot.helper.map.osm.OSMarkerAnimation;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.AngkotPath;
import id.pptik.semutangkot.models.TmbModel;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.networking.CommonRest;
import id.pptik.semutangkot.ui.AnimationView;
import id.pptik.semutangkot.ui.BottomNavigationViewHelper;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.StringResources;

public class MapActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, RestResponHandler,
        Marker.OnMarkerClickListener, BrokerCallback, CompoundButton.OnCheckedChangeListener, BroadcastManager.UIBroadcastListener{



    private Context mContext;
    private MapView mapView;
    RelativeLayout mMarkerDetailLayout;
    private LoadingIndicator indicator;
    private GSONSharedPreferences gPrefs;
    private MarkerClick markerClick;
    private Factory mqFactory;
    private Consumer mqConsumer;
    private final String TAG = this.getClass().getSimpleName();
    private boolean isFirsInit = true;
    private Marker[] angkotMarkers;
    private Angkot[] angkots;
    private TmbModel[] tmbModels;
    private Marker[] tmbMarkers;
    private Marker markerMyLocation;
    private OSMarkerAnimation markerAnimation;
    private FloatingActionButton mClosePopup;
    private Animation slideDown;
    private boolean isActivityPause = false;
    private AppPreferences appPreferences;
    View customView;
    BottomDialog filterDialog;
    private boolean angkotVisible, jalurVisible;

    private boolean isConnected = false;

    private boolean mqIsRunning = false;
    private RecyclerView mPathRecyclerView, mListRecycleView;
    private AngkotListAdapter angkotListAdapter;
    private ImageView toMyLocBtn;
    private double latitude, longitude, speed, altitude;
    BottomNavigationView navigation;
    private BroadcastManager mBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mBroadcastManager = new BroadcastManager(this);
        mBroadcastManager.subscribeToUi(this);

        mMarkerDetailLayout = findViewById(R.id.markerdetail_layout);
        mPathRecyclerView = findViewById(R.id.path_recycleview);
        mListRecycleView = findViewById(R.id.angkot_list_recycleview);

        toMyLocBtn = findViewById(R.id.myloc_btn);
        toMyLocBtn.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_my_location,
                44, R.color.colorPrimaryDark
        ));
        toMyLocBtn.setOnClickListener(view -> {
            if(markerMyLocation != null)
                mapView.getController().animateTo(markerMyLocation.getPosition());
        });

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
        angkotVisible = appPreferences.getBoolean(AppPreferences.KEY_SHOW_ANGKOT, true);
        jalurVisible = appPreferences.getBoolean(AppPreferences.KEY_SHOW_JALUR, true);

        if (!mqIsRunning && angkotVisible)
            connectToRabbit();

        markerAnimation = new OSMarkerAnimation();
        getSupportActionBar().hide();
        indicator = new LoadingIndicator(mContext);

        markerClick = new MarkerClick(mContext, mMarkerDetailLayout);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        Menu menu = navigation.getMenu();
        menu.findItem(R.id.navigation_filter).setIcon(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_layers)
                .color(Color.parseColor("#ffffff"))
                .sizeDp(34));
        menu.findItem(R.id.navigation_path).setIcon(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_directions)
                .color(Color.parseColor("#ffffff"))
                .sizeDp(34));
        menu.findItem(R.id.navigation_angkot_list).setIcon(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_list)
                .color(Color.parseColor("#ffffff"))
                .sizeDp(34));
        menu.findItem(R.id.navigation_profile).setIcon(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .color(Color.parseColor("#ffffff"))
                .sizeDp(34));

        setupMap();
        setFilterLayout();
        setPathList();

    }



    private void setPathList(){
        List<AngkotPath> pathList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(appPreferences.getString(AppPreferences.KEY_STORE_ANGKOT_PATH));
            for(int i = 0; i < array.length(); i++){
                AngkotPath angkotPath = new Gson().fromJson(array.get(i).toString(), AngkotPath.class);
                pathList.add(angkotPath);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mPathRecyclerView.setHasFixedSize(true);
            mPathRecyclerView.setLayoutManager(linearLayoutManager);
            mPathRecyclerView.clearFocus();
            PathListAdapter adapter = new PathListAdapter(this, (view, position) -> {
                indicator.show();
                CommonRest.getPath(
                        pathList.get(position).getTrayekRoute(),
                        this
                );
            }, pathList);
            mPathRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setAngkotFilterList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListRecycleView.setHasFixedSize(true);
        mListRecycleView.setLayoutManager(linearLayoutManager);
        mListRecycleView.clearFocus();
        updateAngkotFilterList();
    }

    private void updateAngkotFilterList(){
        angkotListAdapter = new AngkotListAdapter(this, (view, position) -> {
            mapView.getController().animateTo(new GeoPoint(
                    angkots[position].getAngkot().getLocation().getCoordinates().get(1),
                    angkots[position].getAngkot().getLocation().getCoordinates().get(0)
            ));
            mListRecycleView.setVisibility(View.GONE);
        }, angkots);
        mListRecycleView.setAdapter(angkotListAdapter);
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
        mqIsRunning = true;
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
            JSONArray tmbArray = mainObject.getJSONArray("tmb");
            // angkot
            if(isFirsInit){
                isFirsInit = false;
                indicator.hide();
                angkots = new Angkot[angkotArray.length()];
                angkotMarkers = new Marker[angkotArray.length()];
                for (int i = 0; i < angkotArray.length(); i++) {
                    angkots[i] = new Gson().fromJson(angkotArray.get(i).toString(), Angkot.class);
                    angkotMarkers[i] = new Marker(mapView);
                    angkotMarkers[i].setPosition(new GeoPoint(angkots[i].getAngkot().getLocation().getCoordinates().get(1),
                            angkots[i].getAngkot().getLocation().getCoordinates().get(0)));
                    angkotMarkers[i].setIcon(getResources().getDrawable(R.drawable.tracker_angkot));
                    angkotMarkers[i].setRelatedObject(angkots[i]);
                    angkotMarkers[i].setOnMarkerClickListener(this);
                    angkotMarkers[i].setEnabled(angkotVisible);
                    mapView.getOverlays().add(angkotMarkers[i]);
                    mapView.invalidate();
                }
                setAngkotFilterList();

                // tmb
                tmbModels = new TmbModel[tmbArray.length()];
                tmbMarkers = new Marker[tmbArray.length()];
                for(int i = 0; i < tmbArray.length(); i++){
                    tmbModels[i] = new Gson().fromJson(tmbArray.get(i).toString(), TmbModel.class);
                    tmbMarkers[i] = new Marker(mapView);
                    tmbMarkers[i].setPosition(new GeoPoint(tmbModels[i].getLocation().getCoordinates().get(1),
                            tmbModels[i].getLocation().getCoordinates().get(0)));
                    tmbMarkers[i].setIcon(getResources().getDrawable(R.drawable.tracker_angkot));
                    tmbMarkers[i].setRelatedObject(tmbModels[i]);
                    tmbMarkers[i].setOnMarkerClickListener(this);
                    tmbMarkers[i].setEnabled(angkotVisible);
                    mapView.getOverlays().add(tmbMarkers[i]);
                    mapView.invalidate();
                }

            }else {
                if (angkotArray.length() == angkots.length) {
                    /** animate angkot **/
                    for (int i = 0; i < angkotArray.length(); i++) {
                        JSONObject entity = null;
                        try {
                            entity = angkotArray.getJSONObject(i);
                            Angkot angkot = new Gson().fromJson(entity.toString(), Angkot.class);
                            if (angkots[i].getAngkot().getPlatNomor().equals(angkot.getAngkot().getPlatNomor())) { // update angkotMarkers
                                angkots[i] = new Gson().fromJson(entity.toString(), Angkot.class);
                                if (angkotMarkers[i].getPosition().getLatitude() != angkots[i].getAngkot().getLocation().getCoordinates().get(1) ||
                                        angkotMarkers[i].getPosition().getLongitude() != angkots[i].getAngkot().getLocation().getCoordinates().get(0)) {
                                    double bearing = MarkerBearing.bearing(angkotMarkers[i].getPosition().getLatitude(), angkotMarkers[i].getPosition().getLongitude(),
                                            angkots[i].getAngkot().getLocation().getCoordinates().get(1), angkots[i].getAngkot().getLocation().getCoordinates().get(0));
                                    angkotMarkers[i].setRelatedObject(angkots[i]);
                                    angkotMarkers[i].setRotation((float) bearing);
                                    markerAnimation.animate(mapView, angkotMarkers[i],
                                            new GeoPoint(angkots[i].getAngkot().getLocation().getCoordinates().get(1), angkots[i].getAngkot().getLocation().getCoordinates().get(0)),
                                            1500);
                                } else {
                                    Log.i(TAG, "Same Position");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(mListRecycleView.getVisibility() == View.VISIBLE)
                        updateAngkotFilterList();

                    /** animate tmb **/
                    for (int i = 0; i < tmbArray.length(); i++) {
                        JSONObject entity = null;
                        try {
                            entity = tmbArray.getJSONObject(i);
                            TmbModel tmbModel = new Gson().fromJson(entity.toString(), TmbModel.class);
                            if (tmbModels[i].getId().equals(tmbModel.getId())) { // update tmd markers
                                tmbModels[i] = new Gson().fromJson(entity.toString(), TmbModel.class);
                                if (tmbMarkers[i].getPosition().getLatitude() != tmbModels[i].getLocation().getCoordinates().get(1) ||
                                        tmbMarkers[i].getPosition().getLongitude() != tmbModels[i].getLocation().getCoordinates().get(0)) {
                                    double bearing = MarkerBearing.bearing(tmbMarkers[i].getPosition().getLatitude(), tmbMarkers[i].getPosition().getLongitude(),
                                            tmbModels[i].getLocation().getCoordinates().get(1), tmbModels[i].getLocation().getCoordinates().get(0));
                                    tmbMarkers[i].setRelatedObject(tmbModels[i]);
                                    tmbMarkers[i].setRotation((float) bearing);
                                    markerAnimation.animate(mapView, tmbMarkers[i],
                                            new GeoPoint(tmbModels[i].getLocation().getCoordinates().get(1), tmbModels[i].getLocation().getCoordinates().get(0)),
                                            1500);
                                } else {
                                    Log.i(TAG, "Same Position");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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


    private void setupMap() {
        mapView = findViewById(R.id.mapview);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(20);
        mapView.getController().setZoom(15);
       // mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setTileSource(new GoogleMapProvider("GoogleMapStandart", GoogleMapProvider.STANDARD));
      //  mapView.getTileProvider().setTileRequestCompleteHandler(new SimpleInvalidationHandler(mapView));

        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
        RotationGestureOverlay rotationGestureOverlay= new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(rotationGestureOverlay);

        latitude = (appPreferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0) == 0) ? -6.885719 :
                appPreferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0);
        longitude = (appPreferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0) == 0) ? -107.613622 :
                appPreferences.getFloat(AppPreferences.KEY_MY_LONGITUDE, 0);


        //GeoPoint g1 = new GeoPoint(-6.885719, 107.613622);
        //mapView.getController().animateTo(g1);

        onConnected(latitude, longitude);
        mapView.invalidate();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                //showPopup(navigation);

                break;
            case R.id.navigation_filter:
                filterDialog.show();
                if(mPathRecyclerView.getVisibility() == View.VISIBLE)
                    mPathRecyclerView.setVisibility(View.GONE);
                if(mListRecycleView.getVisibility() == View.VISIBLE)
                    mListRecycleView.setVisibility(View.GONE);
                return true;
            case R.id.navigation_path:
                if(mListRecycleView.getVisibility() == View.VISIBLE)
                    mListRecycleView.setVisibility(View.GONE);
                if(mPathRecyclerView.getVisibility() == View.VISIBLE)
                    mPathRecyclerView.setVisibility(View.GONE);
                else mPathRecyclerView.setVisibility(View.VISIBLE);

                return true;
            case R.id.navigation_angkot_list:
                if(mListRecycleView.getVisibility() == View.VISIBLE)
                    mListRecycleView.setVisibility(View.GONE);
                else {
                    if(angkotVisible)
                        mListRecycleView.setVisibility(View.VISIBLE);
                    else {
                        Toast.makeText(mContext, "Aktifkan menu Angkot pada menu filter", Toast.LENGTH_LONG).show();
                    }
                }

                if(mPathRecyclerView.getVisibility() == View.VISIBLE)
                    mPathRecyclerView.setVisibility(View.GONE);


                return true;
        }
        return false;
    }


    private void setFilterLayout(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.layout_filter, null);
        SwitchCompat angkot = customView.findViewById(R.id.sw_angkot);
        SwitchCompat jalur = customView.findViewById(R.id.sw_jalur);

        angkot.setChecked(angkotVisible);
        jalur.setChecked(jalurVisible);

        angkot.setOnCheckedChangeListener(this);
        jalur.setOnCheckedChangeListener(this);

        filterDialog = new BottomDialog.Builder(this)
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
    }

    @Override
    public void onFinishRequest(JSONObject jResult, String type) {
        indicator.hide();
        switch (type){
            case CommonRest.ENDPOINT_ERROR:
                CommonDialogs.showEndPointError(mContext);
                break;
            case CommonRest.ENDPOINT_GET_PATH:
                Log.i(TAG, jResult.toString());
                mPathRecyclerView.setVisibility(View.GONE);
                for(Overlay overlay : mapView.getOverlayManager().overlays()){
                    if(overlay instanceof Polyline){
                        mapView.getOverlayManager().overlays().remove(overlay);

                    }
                }
                mapView.invalidate();
                try {
                    JSONArray nodeArray = jResult.getJSONObject("geojson")
                            .getJSONObject("geometry").getJSONArray("coordinates")
                            .getJSONArray(0);
                    Polyline line = new Polyline();
                    line.setWidth(6.0f);
                    line.setColor(Color.parseColor("#85000000"));

                    List<GeoPoint> pts = new ArrayList<>();
                    for(int i = 0; i < nodeArray.length(); i++){
                        GeoPoint point = new GeoPoint(
                                nodeArray.getJSONArray(i).getDouble(1),
                                nodeArray.getJSONArray(i).getDouble(0)
                        );
                        pts.add(point);
                    }
                    line.setPoints(pts);
                    line.setGeodesic(true);
                    //mapView.getOverlayManager().add(line);
                    mapView.getOverlays().add(line);
                    mapView.invalidate();
                    for(Overlay overlay : mapView.getOverlayManager().overlays()){
                        if(overlay instanceof Polyline){
                            overlay.setEnabled(jalurVisible);

                        }
                    }
                    if(jalurVisible) {
                        mapView.getController().setZoom(19);
                        mapView.getController().animateTo(line.getPoints().get(0));
                    }
                    mapView.invalidate();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    }

    @Override
    public void onConnectionFailure(String message) {
        Log.i(TAG, message);
        mqIsRunning = false;
        if(isFirsInit)
            CommonDialogs.showEndPointError(mContext);
    }

    @Override
    public void onConnectionClosed(String message) {
        mqIsRunning = false;
        Log.i(TAG, message);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mqIsRunning)
            mqConsumer.stop();
        if(mBroadcastManager.isSubsribed())
            mBroadcastManager.unSubscribeFromUi();
    }

    @Override
    public void onPause(){
        super.onPause();
        isActivityPause = true;
        if(mqIsRunning)
            mqConsumer.stop();

    }


    @Override
    public void onResume(){
        super.onResume();
        isActivityPause = false;
        if(!isFirsInit) {
            if (!mqIsRunning && angkotVisible)
                connectToRabbit();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.sw_angkot:
                appPreferences.put(AppPreferences.KEY_SHOW_ANGKOT, b);
                angkotVisible = b;
                if(!b) {
                    if (mqIsRunning) mqConsumer.stop();
                }
                else {
                    if(!mqIsRunning)
                        connectToRabbit();

                }
                for(Overlay overlay : mapView.getOverlays()){
                    if(overlay instanceof Marker){
                        if(((Marker) overlay).getRelatedObject() instanceof  Angkot){
                            overlay.setEnabled(b);
                        }
                    }
                }
                mapView.invalidate();
                break;
            case R.id.sw_jalur:
                appPreferences.put(AppPreferences.KEY_SHOW_JALUR, b);
                jalurVisible = b;
                for(Overlay overlay : mapView.getOverlayManager().overlays()){
                    if(overlay instanceof Polyline){
                        overlay.setEnabled(b);

                    }
                }
                mapView.invalidate();
                break;
        }
    }


    private void onConnected(double lat, double lon){
        latitude = lat;
        longitude = lon;
        speed = 0;
        altitude = 0;
        appPreferences.put(AppPreferences.KEY_MY_LATITUDE, (float) latitude);
        appPreferences.put(AppPreferences.KEY_MY_LONGITUDE, (float) longitude);
        markerMyLocation = new Marker(mapView);
        markerMyLocation.setPosition(new GeoPoint(latitude, longitude));
        markerMyLocation.setIcon(
                CustomDrawable.googleMaterial(
                        mContext,
                        GoogleMaterial.Icon.gmd_navigation,
                        20, R.color.colorPrimary
                )
        );
        mapView.getOverlayManager().add(markerMyLocation);
        mapView.invalidate();
        mapView.getController().animateTo(markerMyLocation.getPosition());
        isConnected = true;
    }


    private void locationChanged(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        appPreferences.put(AppPreferences.KEY_MY_LATITUDE, (float) latitude);
        appPreferences.put(AppPreferences.KEY_MY_LONGITUDE, (float) longitude);
        if(markerMyLocation != null) {
            markerMyLocation.setRotation((float) MarkerBearing.bearing(markerMyLocation.getPosition().getLatitude(),
                    markerMyLocation.getPosition().getLongitude(), latitude, longitude));
            markerAnimation.animate(mapView, markerMyLocation, new GeoPoint(latitude, longitude), 1500);
            mapView.invalidate();
        }
    }

    @Override
    public void onMessageReceived(String type, Object msg) {
        if(type.equals(AppPreferences.BROADCAST_TYPE_LOCATION)){
            Location location = (Location) msg;
            Log.i(TAG, "Location Update "+location.getLatitude()+", "+location.getLongitude());
            if(!isConnected)
                onConnected(location.getLatitude(), location.getLongitude());
            else locationChanged(location);
        }
    }
}
