package id.pptik.semutangkot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.KirModel;
import id.pptik.semutangkot.networking.KirRest;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.utils.CustomDrawable;

public class KirActivity extends AppCompatActivity implements RestResponHandler {

    private Button checkButton;
    private RadioRealButtonGroup buttonGroup;
    private RadioRealButton platCheck, noCheck;
    private EditText nomorEdit;
    private Activity context;
    private static final String TAG = KirActivity.class.getSimpleName();
    private int state = 0;
    private LoadingIndicator indicator;
    private KirModel kirModel;
    private TextView detailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kir);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        indicator = new LoadingIndicator(context);
        bindViews();

        buttonGroup.setOnPositionChangedListener((button, currentPosition, lastPosition) -> {
            state = currentPosition;
            if(currentPosition == 0)
                nomorEdit.setHint("Plat Nomor (mis. D 0000 XX)");
            else
                nomorEdit.setHint("Nomor Uji (mis. BD 00000");
        });

        checkButton.setOnClickListener(view -> {
            String nomor = nomorEdit.getText().toString();

            if(nomor.equals("")){
                Snackbar.make(view, "Isi kolom nomor terlebih dahulu", Snackbar.LENGTH_LONG).show();
            }else {
                indicator.show();
                KirRest.check(nomor, state, this);
            }
        });
    }

    private void bindViews() {
        checkButton = findViewById(R.id.check_button);
        platCheck = findViewById(R.id.check_plat);
        noCheck = findViewById(R.id.no_check);
        buttonGroup = findViewById(R.id.group_check);
        nomorEdit = findViewById(R.id.nomor_edit);
        detailText = findViewById(R.id.detail_text);
    }

    private void setDetail(){
        String tmp = "<b> No. Kendaraan : </b>"+kirModel.getNOKENDARAAN()+"<br>";
        tmp += "<b> No. Uji : </b>"+kirModel.getNOUJI()+"<br>";
        tmp += "<b> Merek : </b>"+kirModel.getMEREK()+"<br>";
        tmp += "<b> Tahun : </b>"+kirModel.getTAHUN()+"<br>";
        tmp += "<b> No. Chasis : </b>"+kirModel.getNOCHASIS()+"<br>";
        tmp += "<b> No. Mesin : </b>"+kirModel.getNOMESIN()+"<br>";
        tmp += "<b> Jenis : </b>"+kirModel.getJENIS()+" | "+kirModel.getJENIS1()+"<br>";
        tmp += "<b> Jenis BBM : </b>"+kirModel.getBBM()+"<br>";
        tmp += "<b> Status Kendaraan: </b>"+kirModel.getSTATUS()+"<br>";
        tmp += "<b> Tgl. Habis Uji : </b>"+kirModel.getHABISUJI()+"<br>";
        tmp += "<b> Tgl. Habis Uji Lalu : </b>"+kirModel.getHABISUJILALU()+"<br>";
        tmp += "<b> No. Uji : </b>"+kirModel.getNOUJI()+"<br>";
        String x = (kirModel.getDONE().equals("1")) ? "SUDAH" : "BELUM";
        tmp += "<b> Status Pengujian : </b>"+x+"<br>";

        detailText.setText(Html.fromHtml(tmp));
    }

    @Override
    public void onFinishRequest(JSONObject jResult, String type) {
        indicator.hide();
        switch (type){
            case KirRest.ENDPOINT_CHECK_KIR:
                Log.i(TAG, jResult.toString());
                try {
                    JSONArray array = jResult.getJSONArray("data");
                    if(array.length() == 0){
                        CommonDialogs.showWarning(context, "Data KIR tidak ditemukan");
                    }else {
                        kirModel = new Gson().fromJson(array.get(0).toString(), KirModel.class);
                        setDetail();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case KirRest.ENDPOINT_ERROR:
                CommonDialogs.showEndPointError(this);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
