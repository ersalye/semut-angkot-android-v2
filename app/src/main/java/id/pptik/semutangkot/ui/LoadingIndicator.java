package id.pptik.semutangkot.ui;


import android.app.Activity;
import android.content.Context;
import android.view.View;

import id.pptik.semutangkot.R;

public class LoadingIndicator {

    private Context context;
    private View indicatorView;

    public LoadingIndicator(Context context){
        this.context = context;
        indicatorView = ((Activity) context).findViewById(R.id.indicator);
    }

    public void show(){
        indicatorView.setVisibility(View.VISIBLE);
    }

    public void hide(){
        indicatorView.setVisibility(View.GONE);
    }

}
