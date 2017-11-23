package id.pptik.semutangkot;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.pptik.semutangkot.adapters.BookingAngkotListAdapter;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.networking.CommonRest;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.utils.ProfileUtils;

public class BookingAngkotActivity extends AppCompatActivity {

    LoadingIndicator indicator;
    private Context context;
    private final static String TAG = BookingAngkotActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<Angkot> angkotArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_angkot);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycleview);

        context = this;
        indicator = new LoadingIndicator(context);
        indicator.show();
        angkotArrayList = new ArrayList<>();
        Profile profile = ProfileUtils.getProfile(context);
        CommonRest.getAngkot(profile.getToken(), (jResult, type) -> {
            indicator.hide();
            Log.i(TAG, jResult.toString());
            if(type.equals(CommonRest.ENDPOINT_ERROR)){
                CommonDialogs.showEndPointError(context);
            }else {
                RequestStatus status = ProfileUtils.getReqStatus(jResult.toString());
                if (status.getSuccess()) {
                    try {
                        JSONArray array = jResult.getJSONArray("data");
                        for(int i = 0; i < array.length(); i++){
                            Angkot angkot = new Gson().fromJson(array.get(i).toString(), Angkot.class);
                            angkotArrayList.add(angkot);
                        }
                        setAngkotList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else CommonDialogs.showError(context, status.getMessage());
            }
        });
    }


    private void setAngkotList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.clearFocus();
        BookingAngkotListAdapter adapter = new BookingAngkotListAdapter(context, angkotArrayList);
        recyclerView.setAdapter(adapter);
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
