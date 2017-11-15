package id.pptik.semutangkot.networking;


import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.utils.StringResources;

public class KirRest {

    public static final String ENDPOINT_CHECK_KIR = "/lihatkendaraandetail.php?plat=";
    public static final String ENDPOINT_ERROR = "endpoint.error";

    private static String pathByNo = "getlatestbynouji";
    private static String pathByPlat = "getlatestbyplat";

    public static void check(String nomor, int state, RestResponHandler handler){

        String path = (state == 0) ? pathByPlat : pathByNo;

        String endPoint = StringResources.get(R.string.kirEndpoint);
        AndroidNetworking.post(endPoint+ path)
                .setTag(ENDPOINT_CHECK_KIR)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("NO_UJI", nomor)
                .addBodyParameter("PLAT", nomor)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("data", response);
                            handler.onFinishRequest(object, ENDPOINT_CHECK_KIR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        handler.onFinishRequest(null, ENDPOINT_ERROR);
                    }
                });
    }

}
