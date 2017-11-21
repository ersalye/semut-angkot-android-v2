package id.pptik.semutangkot.ui.popups;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.helper.TimeHelper;
import id.pptik.semutangkot.models.TmbModel;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.NumUtils;

public class TmbPopup {

    public static void show(Activity context, TmbModel model){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.map_angkot_fragment, null);

        TextView jurusan = customView.findViewById(R.id.jurusan);
        jurusan.setText(model.getKoridor());

        ImageView icon = customView.findViewById(R.id.icon_jurusan);
        icon.setImageDrawable(CustomDrawable.googleMaterial(
                context,
                GoogleMaterial.Icon.gmd_swap_horiz,
                30, R.color.colorPrimaryDark
        ));

        ImageView plat = customView.findViewById(R.id.image_description);
        plat.setImageDrawable(CustomDrawable.googleMaterial(
                context,
                GoogleMaterial.Icon.gmd_call_to_action,
                50, R.color.colorPrimaryDark
        ));

        TextView platText = customView.findViewById(R.id.plat_text);
        platText.setText(model.getBuscode());

        TextView timeDetail = customView.findViewById(R.id.time_detail);
        String tmp = "<b>Lokasi Terakhir : </b>"+ NumUtils.convertMongoDateToAgo(model.getGpsdatetime());
        timeDetail.setText(Html.fromHtml(tmp));


        BottomDialog dialog = new BottomDialog.Builder(context)
                .setTitle("TMB")
                .setIcon(CustomDrawable.googleMaterial(
                        context,
                        GoogleMaterial.Icon.gmd_directions_bus,
                        46, R.color.colorPrimaryDark
                ))
                .setCustomView(customView)
                .setPositiveText("OK")
                .onPositive(bottomDialog -> bottomDialog.dismiss())
                .build();
        dialog.show();
    }

}
