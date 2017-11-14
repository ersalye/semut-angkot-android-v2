package id.pptik.semutangkot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import android.Manifest;
import android.widget.Toast;

import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;
import com.github.hynra.wortel.BrokerCallback;
import com.github.hynra.wortel.Consumer;
import com.github.hynra.wortel.Factory;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.maksim88.easylogin.EasyLogin;
import com.maksim88.easylogin.networks.SocialNetwork;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rabbitmq.client.Channel;

import net.grandcentrix.tray.core.ItemNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
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
import id.pptik.semutangkot.helper.map.MarkerBearing;
import id.pptik.semutangkot.helper.map.osm.MarkerClick;
import id.pptik.semutangkot.helper.map.osm.OSMarkerAnimation;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.AngkotPath;
import id.pptik.semutangkot.models.Cctv;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.models.angkot.AngkotPost;
import id.pptik.semutangkot.networking.RequestRest;
import id.pptik.semutangkot.ui.AnimationView;
import id.pptik.semutangkot.ui.BottomNavigationViewHelper;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.ui.MainDrawer;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.StringResources;

public class MapActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, RestResponHandler,
        Marker.OnMarkerClickListener, BrokerCallback, CompoundButton.OnCheckedChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {



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
    private Marker markerMyLocation;
    private OSMarkerAnimation markerAnimation;
    private AngkotPost[] angkotPosts;
    private FloatingActionButton mClosePopup;
    private Animation slideDown;
    private boolean isActivityPause = false;
    private AppPreferences appPreferences;
    View customView;
    BottomDialog filterDialog;
    private boolean angkotVisible, cctvVisible, laporanVisible, jalurVisible;
  /*  private static final  int ANGKOT_OVERLAY_ORDER = 3;
    private static final int CCTV_OVERLAY_ORDER = 2;
    private static final int LAPORAN_OVERLAY_ORDER = 1;
    private static final int JALUR_OVERLAY_ORDER = 0; */
    private boolean mqIsRunning = false;
    private RecyclerView mPathRecyclerView, mListRecycleView;
    private AngkotListAdapter angkotListAdapter;
    private ImageView addReportBtn, toMyLocBtn;
    private double latitude, longitude, speed, altitude;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    BottomNavigationView navigation;
    private MainDrawer drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        drawer = new MainDrawer();
        drawer.attach(MapActivity.this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mMarkerDetailLayout = findViewById(R.id.markerdetail_layout);
        mPathRecyclerView = findViewById(R.id.path_recycleview);
        mListRecycleView = findViewById(R.id.angkot_list_recycleview);
        addReportBtn = findViewById(R.id.add_report_btn);
        addReportBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, TagsActivity.class));
        });
        toMyLocBtn = findViewById(R.id.myloc_btn);
        toMyLocBtn.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_my_location,
                44, R.color.colorPrimaryDark
        ));
        toMyLocBtn.setOnClickListener(view -> {
            if(markerMyLocation != null)
                mapView.getController().animateTo(markerMyLocation.getPosition());
        });

        addReportBtn.setImageDrawable(CustomDrawable.googleMaterial(
                this, GoogleMaterial.Icon.gmd_add,
                24, R.color.cpb_white
        ));


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
        setLocationRequest();
        angkotVisible = appPreferences.getBoolean(AppPreferences.KEY_SHOW_ANGKOT, true);
        cctvVisible = appPreferences.getBoolean(AppPreferences.KEY_SHOW_CCTV, true);
        laporanVisible = appPreferences.getBoolean(AppPreferences.KEY_SHOW_LAPORAN, true);
        jalurVisible = appPreferences.getBoolean(AppPreferences.KEY_SHOW_JALUR, true);

        if (!mqIsRunning && (angkotVisible || laporanVisible))
            connectToRabbit();

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

    private void setLocationRequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(5 * 1000);
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

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
                RequestRest.getPath(
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
                    markers[i].setEnabled(angkotVisible);
                    mapView.getOverlays().add(markers[i]);
                    mapView.invalidate();
                }
                setAngkotFilterList();
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
                        marker.setEnabled(laporanVisible);
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
        mapView.setMultiTouchControls(true);
        mapView.setMaxZoomLevel(20);
        mapView.getController().setZoom(15);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
        RotationGestureOverlay rotationGestureOverlay= new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(rotationGestureOverlay);
        GeoPoint g1 = new GeoPoint(-6.885719, 107.613622);
        mapView.getController().animateTo(g1);
        mapView.invalidate();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                //showPopup(navigation);
                drawer.getMiniDrawer().getDrawer().openDrawer();
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
            marker.setEnabled(cctvVisible);
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

        angkot.setChecked(angkotVisible);
        cctv.setChecked(cctvVisible);
        jalur.setChecked(jalurVisible);
        laporan.setChecked(laporanVisible);

        angkot.setOnCheckedChangeListener(this);
        cctv.setOnCheckedChangeListener(this);
        jalur.setOnCheckedChangeListener(this);
        laporan.setOnCheckedChangeListener(this);

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
            case RequestRest.ENDPOINT_GET_PATH:
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
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        isActivityPause = true;
        if(mqIsRunning)
            mqConsumer.stop();

    }


    private void showPopup(View v){
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.inflate(R.menu.menu_main);
        popup.setOnMenuItemClickListener(item1 -> {
            switch (item1.getItemId()) {
                case R.id.logout:
                //    EasyLogin.initialize();
                    EasyLogin easyLogin = EasyLogin.getInstance();
                    for (SocialNetwork socialNetwork : easyLogin.getInitializedSocialNetworks()) {
                        socialNetwork.logout();
                        Log.i("Social Login", socialNetwork.getNetwork().name());
                    }
                    appPreferences.clear();
                    Intent intent = new Intent(mContext, WizardActivity.class);
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    mContext.startActivity(mainIntent);
                    finish();
                    break;
            }
            return false;
        });
        //displaying the popup
        popup.show();
    }

    @Override
    public void onResume(){
        super.onResume();
        isActivityPause = false;
        if(!isFirsInit) {
            if (!mqIsRunning && (angkotVisible || laporanVisible))
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
                    if (mqIsRunning && !laporanVisible) mqConsumer.stop();
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
            case R.id.sw_cctv:
                appPreferences.put(AppPreferences.KEY_SHOW_CCTV, b);
                cctvVisible = b;
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
                jalurVisible = b;
                for(Overlay overlay : mapView.getOverlayManager().overlays()){
                    if(overlay instanceof Polyline){
                        overlay.setEnabled(b);

                    }
                }
                mapView.invalidate();
                break;
            case R.id.sw_laporan:
                appPreferences.put(AppPreferences.KEY_SHOW_LAPORAN, b);
                laporanVisible = b;
                if(!b) {
                    if (mqIsRunning && !angkotVisible) mqConsumer.stop();
                }
                else {
                    if(!mqIsRunning)
                        connectToRabbit();

                }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission Denied");
        }else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i(TAG, "LOCATION CONNECTED");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Log.i(TAG, "location null");

            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                speed = location.getSpeed();
                altitude = location.getAltitude();
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
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
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
}
