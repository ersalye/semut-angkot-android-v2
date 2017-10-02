package id.pptik.semutangkot.utils;


import id.pptik.semutangkot.App;

public class StringResources {

    public static String get(int id){
        return App.getContext().getResources().getString(id);
    }

}
