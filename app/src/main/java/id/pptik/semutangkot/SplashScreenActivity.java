package id.pptik.semutangkot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.github.hynra.gsonsharedpreferences.GSONSharedPreferences;
import com.github.hynra.gsonsharedpreferences.ParsingException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.networking.CommonRest;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.utils.CheckService;
import wail.splacher.com.splasher.lib.SplasherActivity;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;
import android.Manifest;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

public class SplashScreenActivity extends SplasherActivity {

    private boolean isApprove = false;

    @Override
    public void initSplasher(SplasherConfig config) {
        getSupportActionBar().hide();
        config.setReveal_start(Const.START_CENTER)
                .setAnimationDuration(2000)
                .setLogo(R.drawable.app_icon_plain)
                .setLogo_animation(Techniques.Tada)
                .setAnimationLogoDuration(2000)
                .setLogoWidth(500)
                .setTitle("Semut App")
                .setTitleColor(Color.parseColor("#ffffff"))
                .setTitleAnimation(Techniques.DropOut)
                .setTitleSize(24)
                .setSubtitle("Memuat...")
                .setSubtitleColor(Color.parseColor("#ffffff"))
                .setSubtitleAnimation(Techniques.DropOut)
                .setSubtitleSize(16);
    }

    @Override
    public void onSplasherFinished() {
        if (CheckService.isGpsEnabled(this)) {
            checkPermission();
        }else CommonDialogs.showError(this,
                "Lokasi Anda tidak aktif. Aktifkan pengaturan lokasi Anda terlebih dahulu");
    }


    private void checkPermission(){
        isApprove = false;
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
                    isApprove = true;
                    Log.i("PERMISSION", response.getPermissionName()+" permission granted");

                }

                for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
                    //Log.i("PERMISSION", "permission denied");
                    isApprove = false;
                    new AlertDialog.Builder(SplashScreenActivity.this).setTitle("Persetujuan Dibutuhkan")
                            .setMessage("Aplikasi ini membutuhkan fitur yang memerlukan persetujuan Anda")
                            .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                dialog.dismiss();
                                finish();

                            })
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss();
                                checkPermission();

                            })
                            .setOnDismissListener(dialog -> finish())
                            .show();
                }

                if(isApprove){
                    try {
                        Profile profile = (Profile) new GSONSharedPreferences(SplashScreenActivity.this).getObject(new Profile());
                        CommonRest.checkStatus(profile.getToken(), (jResult, type) -> {
//                            Log.i(this.getClass().getSimpleName(), jResult.toString());
                            switch (type){
                                case CommonRest.ENDPOINT_ERROR:
                                    CommonDialogs.showEndPointError(SplashScreenActivity.this);
                                    break;
                                case CommonRest.ENDPOINT_STATUS:
                                    try {
                                        if(jResult.getBoolean("success")){
                                            // to main
                                            startActivity(new Intent(
                                                    SplashScreenActivity.this,
                                                    MapActivity.class
                                            ));
                                            finish();
                                        }else {
                                            CommonDialogs.showRelateError(
                                                    SplashScreenActivity.this,
                                                    jResult.getString("message"),
                                                    jResult.getString("code")
                                            );
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        });
                    } catch (ParsingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                showPermissionRationale(token);
            }

        }).check();
    }


    public void showPermissionRationale(final PermissionToken token) {
        new AlertDialog.Builder(this).setTitle("Persetujuan Dibutuhkan")
                .setMessage("Aplikasi ini membutuhkan fitur yang memerlukan persetujuan Anda")
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                    token.cancelPermissionRequest();
                })
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    token.continuePermissionRequest();
                })
                .setOnDismissListener(dialog -> token.cancelPermissionRequest())
                .show();
    }
}
