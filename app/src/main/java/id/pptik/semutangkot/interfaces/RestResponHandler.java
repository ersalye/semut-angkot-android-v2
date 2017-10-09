package id.pptik.semutangkot.interfaces;


import org.json.JSONObject;

public interface RestResponHandler {
    public void onFinishRequest(JSONObject jResult, String type);
}
