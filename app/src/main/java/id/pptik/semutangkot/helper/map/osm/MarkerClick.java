package id.pptik.semutangkot.helper.map.osm;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import org.osmdroid.views.overlay.Marker;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.fragments.map.AngkotReportFragment;
import id.pptik.semutangkot.fragments.map.MapAngkotFragment;
import id.pptik.semutangkot.fragments.map.MapTrackerFragment;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.models.angkot.AngkotPost;
import id.pptik.semutangkot.models.mapview.Tracker;
import id.pptik.semutangkot.ui.AnimationView;
import id.pptik.semutangkot.utils.FragmentTransUtility;


public class MarkerClick {
    private Context context;
    private View frameView;
    private FragmentTransUtility fragmentTransUtility;
    private Animation fromRight;
    private AnimationView animationView;

    public MarkerClick(Context context, View frameView){
        this.context = context;
        this.frameView = frameView;
        fragmentTransUtility = new FragmentTransUtility(context);
        animationView = new AnimationView(context);
        fromRight = animationView.getAnimation(R.anim.slide_up, null);
    }

    public void checkMarker(Marker marker){
        if(marker.getRelatedObject() instanceof Tracker){
            MapTrackerFragment mapTrackerFragment = new MapTrackerFragment();
            mapTrackerFragment.setData((Tracker) marker.getRelatedObject());
            fragmentTransUtility.setTrackerMapFragment(mapTrackerFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }else if(marker.getRelatedObject() instanceof Angkot){
            MapAngkotFragment mapAngkotFragment = new MapAngkotFragment();
            mapAngkotFragment.setData((Angkot) marker.getRelatedObject());
            fragmentTransUtility.setAngkotMapFragment(mapAngkotFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }else if(marker.getRelatedObject() instanceof AngkotPost){
            AngkotReportFragment mapAngkotFragment = new AngkotReportFragment();
            mapAngkotFragment.setData((AngkotPost) marker.getRelatedObject());
            fragmentTransUtility.setAngkotReportFragment(mapAngkotFragment, frameView.getId());
            frameView.setVisibility(View.VISIBLE);
            frameView.startAnimation(fromRight);
        }
    }
}
