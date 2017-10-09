package id.pptik.semutangkot.networking;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.interfaces.Constants;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.utils.StringResources;

public class RequestRest {

    public RequestRest(){

    }

    public static void login(String token, String strategy, String id, String name,
                             String email, RestResponHandler handler){
        String endPoint = StringResources.get(R.string.apiEndPoint);
        AndroidNetworking.post(endPoint+ Constants.ENDPOINT_LOGIN)
                .addBodyParameter("Token", token)
                .addBodyParameter("Strategy", strategy)
                .addBodyParameter("id", id)
                .addBodyParameter("Name", name)
                .addBodyParameter("Email", email)
                .setTag(Constants.ENDPOINT_LOGIN)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.onFinishRequest(response, Constants.ENDPOINT_LOGIN);
                    }
                    @Override
                    public void onError(ANError error) {
                        handler.onFinishRequest(null, Constants.ENDPOINT_ERROR);
                    }
                });
    }

}
