package id.pptik.semutangkot.networking;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.utils.StringResources;

public class RequestRest {

    public static final String ENDPOINT_LOGIN = "users/login";
    public static final String ENDPOINT_STATUS = "users/status";
    public static final String ENDPOINT_CCTV = "cctv/bandung";
    public static final String ENDPOINT_ERROR = "endpoint.error";

    public RequestRest(){

    }

    public static void login(String token, String strategy, String id, String name,
                             String email, RestResponHandler handler){
        String endPoint = StringResources.get(R.string.apiEndPoint);
        AndroidNetworking.post(endPoint+ ENDPOINT_LOGIN)
                .addBodyParameter("Token", token)
                .addBodyParameter("Strategy", strategy)
                .addBodyParameter("id", id)
                .addBodyParameter("Name", name)
                .addBodyParameter("Email", email)
                .setTag(ENDPOINT_LOGIN)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.onFinishRequest(response, ENDPOINT_LOGIN);
                    }
                    @Override
                    public void onError(ANError error) {
                        handler.onFinishRequest(null, ENDPOINT_ERROR);
                    }
                });
    }

    public static void checkStatus(String token, RestResponHandler handler){
        String endPoint = StringResources.get(R.string.apiEndPoint);
        AndroidNetworking.post(endPoint+ ENDPOINT_STATUS)
                .addBodyParameter("Token", token)
                .setTag(ENDPOINT_STATUS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.onFinishRequest(response, ENDPOINT_STATUS);
                    }
                    @Override
                    public void onError(ANError error) {
                        handler.onFinishRequest(null, ENDPOINT_ERROR);
                    }
                });
    }



    public static void bandungCctv(String token, RestResponHandler handler){
        String endPoint = StringResources.get(R.string.apiEndPoint);
        AndroidNetworking.post(endPoint+ ENDPOINT_CCTV)
                .addBodyParameter("Token", token)
                .setTag(ENDPOINT_STATUS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.onFinishRequest(response, ENDPOINT_CCTV);
                    }
                    @Override
                    public void onError(ANError error) {
                        handler.onFinishRequest(null, ENDPOINT_ERROR);
                    }
                });
    }

}
