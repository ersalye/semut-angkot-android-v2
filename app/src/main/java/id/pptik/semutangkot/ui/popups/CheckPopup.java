package id.pptik.semutangkot.ui.popups;


import android.app.Activity;
import android.content.Context;

import org.osmdroid.views.overlay.Marker;

import id.pptik.semutangkot.models.TmbModel;
import id.pptik.semutangkot.models.angkot.Angkot;

public class CheckPopup {


    public static void check(Marker marker, Activity context){
        Object object = marker.getRelatedObject();
        if(object instanceof Angkot){
            Angkot angkot = (Angkot) object;
            AngkotPopUp.show(context, angkot);
        }else if(object instanceof TmbModel){
            TmbModel model = (TmbModel) object;
            TmbPopup.show(context, model);
        }
    }

}
