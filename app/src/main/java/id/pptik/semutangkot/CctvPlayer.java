package id.pptik.semutangkot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import id.pptik.semutangkot.utils.StringResources;


public class CctvPlayer extends Activity implements OnPreparedListener, OnErrorListener, OnCompletionListener {
    private VideoView videoView;
    private String urlStr;
    private ProgressDialog pDialog;
    private boolean isStream = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv_player);

        videoView = findViewById(R.id.videoView);
        Bundle bundle = this.getIntent().getExtras();
        isStream = bundle.getBoolean(StringResources.get(R.string.INTENT_VIDEO_IS_STREAMING), false);

        String vUrl = bundle.getString(StringResources.get(R.string.INTENT_VIDEO_URL));
        setUrlStr(vUrl);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        showDialog();



        Uri video = (!isStream) ? Uri.parse(getUrlStr()) : Uri.parse(vUrl);
        Log.i(this.getClass().getSimpleName(), String.valueOf(video));

        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnCompletionListener(this);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // setter dan getter

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
        this.urlStr  = this.urlStr .replace("push-ios", "247");
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();
        finish();
    }

    @Override
    public void onCompletion() {
        finish();
    }

    @Override
    public boolean onError(Exception e) {
        hideDialog();
        e.printStackTrace();
        Toast.makeText(getApplicationContext(),
                "CCTV untuk sementara tidak dapat diakses", Toast.LENGTH_LONG)
                .show();
        finish();
        return false;
    }

    @Override
    public void onPrepared() {
        hideDialog();
        videoView.start();
    }
}