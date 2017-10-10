package id.pptik.semutangkot.utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import id.pptik.semutangkot.fragments.map.AngkotReportFragment;
import id.pptik.semutangkot.fragments.map.MapAngkotFragment;
import id.pptik.semutangkot.fragments.map.MapTrackerFragment;


public class FragmentTransUtility {
    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FragmentTransUtility(Context context){
        this.context = context;
        fragmentManager = ((Activity)context).getFragmentManager();

    }




    public void setTrackerMapFragment(MapTrackerFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }


    public void setAngkotMapFragment(MapAngkotFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    public void setAngkotReportFragment(AngkotReportFragment fragment, int id){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }
}
