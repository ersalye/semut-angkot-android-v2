package id.pptik.semutangkot.utils;



import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;


public class CustomDrawable {
    public static Drawable googleMaterial(Context context, GoogleMaterial.Icon icon, int size, int colorID){
        return new IconicsDrawable(context)
                .color(context.getResources().getColor(colorID))
                .sizeDp(size)
                .icon(icon);
    }
    public static Drawable fontAwesome(Context context, FontAwesome.Icon icon, int size, int colorID){
        return new IconicsDrawable(context)
                .color(context.getResources().getColor(colorID))
                .sizeDp(size)
                .icon(icon);
    }

}