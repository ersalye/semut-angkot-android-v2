package id.pptik.semutangkot.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.maksim88.easylogin.EasyLogin;
import com.maksim88.easylogin.networks.SocialNetwork;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.WizardActivity;
import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.utils.CustomDrawable;

public class CommonDialogs {

    public static void showEndPointError(Context context){
        BottomDialog bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Server Tidak merespon!")
                .setContent("Server sedang mengalami gangguan atau koneksi internet Anda sedang tidak stabil")
                .setIcon(CustomDrawable.googleMaterial(
                        context,
                        GoogleMaterial.Icon.gmd_cloud_off,
                        46, R.color.colorPrimary
                ))
                .setPositiveText("Keluar")
                .setCancelable(false)
                .onPositive(bottomDialog1 -> {
                    bottomDialog1.dismiss();
                    ((Activity)context).finish();
                })
                .build();
        bottomDialog.show();
    }


    public static void showError(Context context, String content){
        BottomDialog bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Ups! Gagal memuat permintaan")
                .setContent(content)
                .setIcon(CustomDrawable.googleMaterial(
                        context,
                        GoogleMaterial.Icon.gmd_do_not_disturb_off,
                        46, R.color.colorPrimary
                ))
                .setPositiveText("Keluar")
                .setCancelable(false)
                .onPositive(bottomDialog1 -> {
                    bottomDialog1.dismiss();
                    ((Activity)context).finish();
                })
                .build();
        bottomDialog.show();
    }

    public static void showRelateError(Context context, String content, String code){
        if(code.equals("009")){
            logout(context);
        }else {
            BottomDialog bottomDialog = new BottomDialog.Builder(context)
                    .setTitle("Ups! Gagal memuat permintaan")
                    .setContent(content)
                    .setCancelable(false)
                    .setIcon(CustomDrawable.googleMaterial(
                            context,
                            GoogleMaterial.Icon.gmd_do_not_disturb_off,
                            46, R.color.colorPrimary
                    ))
                    .setPositiveText("Keluar")
                    .onPositive(bottomDialog1 -> {
                        bottomDialog1.dismiss();
                        ((Activity) context).finish();
                    })
                    .build();
            bottomDialog.show();
        }
    }

    private static void logout(Context context){
        BottomDialog bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Sesi Habis")
                .setCancelable(false)
                .setContent("Sesi Anda telah berakhir, silahkan melakukan login kembali")
                .setIcon(CustomDrawable.googleMaterial(
                        context,
                        GoogleMaterial.Icon.gmd_hourglass_empty,
                        46, R.color.colorPrimary
                ))
                .setPositiveText("Keluar")
                .onPositive(bottomDialog1 -> {
                    bottomDialog1.dismiss();
                    EasyLogin easyLogin = EasyLogin.getInstance();
                    for (SocialNetwork socialNetwork : easyLogin.getInitializedSocialNetworks()) {
                        socialNetwork.logout();
                        Log.i("Social Login", socialNetwork.getNetwork().name());
                    }
                    AppPreferences pref = new AppPreferences(context);
                    pref.put(AppPreferences.KEY_IS_LOGGED_IN, false);
                    pref.put(AppPreferences.KEY_IS_FIRST_LAUNCH, false);
                    context.startActivity(new Intent(context, WizardActivity.class));
                    ((Activity) context).finish();
                })
                .build();
        bottomDialog.show();
    }


    public static void showFilter(Context context, AppPreferences preferences){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.layout_filter, null);
        SwitchCompat angkot = customView.findViewById(R.id.sw_angkot);
        SwitchCompat cctv = customView.findViewById(R.id.sw_cctv);
        SwitchCompat jalur = customView.findViewById(R.id.sw_jalur);
        SwitchCompat laporan = customView.findViewById(R.id.sw_laporan);

        angkot.setChecked(preferences.getBoolean(AppPreferences.KEY_SHOW_ANGKOT, true));
        cctv.setChecked(preferences.getBoolean(AppPreferences.KEY_SHOW_CCTV, true));
        jalur.setChecked(preferences.getBoolean(AppPreferences.KEY_SHOW_JALUR, true));
        laporan.setChecked(preferences.getBoolean(AppPreferences.KEY_SHOW_LAPORAN, true));

        angkot.setOnCheckedChangeListener((compoundButton, b) -> preferences.put(AppPreferences.KEY_SHOW_ANGKOT, b));
        cctv.setOnCheckedChangeListener((compoundButton, b) -> preferences.put(AppPreferences.KEY_SHOW_CCTV, b));
        jalur.setOnCheckedChangeListener((compoundButton, b) -> preferences.put(AppPreferences.KEY_SHOW_JALUR, b));
        laporan.setOnCheckedChangeListener((compoundButton, b) -> preferences.put(AppPreferences.KEY_SHOW_LAPORAN, b));

        BottomDialog bottomDialog = new BottomDialog.Builder(context)
                .setTitle("Filter Peta")
                .setContent("Atur item mana yang ditampilkan di dalam peta")
                .setCustomView(customView)
                .setIcon(CustomDrawable.googleMaterial(
                        context,
                        GoogleMaterial.Icon.gmd_sort,
                        46, R.color.colorPrimary
                ))
                .setPositiveText("OK")
                .onPositive(bottomDialog1 -> bottomDialog1.dismiss())
                .build();
        bottomDialog.show();
    }


}
