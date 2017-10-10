package id.pptik.semutangkot.helper.map;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.pptik.semutangkot.models.mapview.Tracker;
import id.pptik.semutangkot.models.mapview.TranspostMap;


public class MapViewComponent {

    public static int USER_MAP_COMPONENT = 0;
    public static int CCTV_MAP_COMPONENT = 1;
    public static final int POLICE_MAP_COMPONENT = 2;
    public static final int ACCIDENT_MAP_COMPONENT = 3;
    public static final int TRAFFIC_MAP_COMPONENT = 4;
    public static final int DISASTER_MAP_COMPONENT = 5;
    public static final int CLOSURE_MAP_COMPONENT = 6;
    public static final int OTHER_MAP_COMPONENT = 7;
    //public static final int COMMUTER_MAP_COMPONENT = 8;
    public static final int TRACKER_MAP_COMPONENT = 8;
    public static final int TRANSPORTATION_POST_MAP_COMPONENT = 9;




    public static TranspostMap[] getTransPost(int indexComponent, String jsonString){
        TranspostMap[] transpostMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray polices = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            polices = new JSONObject(array.get(indexComponent).toString()).getJSONArray("PublicTran");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        transpostMaps = new TranspostMap[polices.length()];
        for (int i = 0; i <polices.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                transpostMaps[i] = gson.fromJson(polices.get(i).toString(), TranspostMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return transpostMaps;

    }


    public static Tracker[] getTrackers(int indexComponent, String jsonString){
        Tracker[] trackers;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray trackerArr = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            trackerArr = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Trackers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        trackers = new Tracker[trackerArr.length()];
        for (int i = 0; i <trackerArr.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                trackers[i] = gson.fromJson(trackerArr.get(i).toString(), Tracker.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return trackers;

    }


}
