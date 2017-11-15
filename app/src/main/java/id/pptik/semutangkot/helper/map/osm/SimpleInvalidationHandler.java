package id.pptik.semutangkot.helper.map.osm;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class SimpleInvalidationHandler extends Handler {
    private final View mView;

    public SimpleInvalidationHandler(View paramView) {
        this.mView = paramView;
    }

    public void handleMessage(Message paramMessage) {
        Log.i("HANDLER", ""+paramMessage.what);
        switch (paramMessage.what) {
            default:
                return;
            case 0:
        }
        this.mView.invalidate();
    }
}