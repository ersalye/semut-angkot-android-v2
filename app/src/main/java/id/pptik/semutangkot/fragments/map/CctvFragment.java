package id.pptik.semutangkot.fragments.map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.pptik.semutangkot.CctvPlayer;
import id.pptik.semutangkot.R;
import id.pptik.semutangkot.models.Cctv;
import id.pptik.semutangkot.utils.StringResources;


public class CctvFragment extends Fragment {

    private ImageView thumb;
    private TextView detail;
    private Button watchBtn;

    Cctv cctvMap;
    boolean isStream = false;


    public void setData(Cctv cctvMap){
        this.cctvMap = cctvMap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cctv, container, false);

        thumb = view.findViewById(R.id.thumb);
        detail = view.findViewById(R.id.cctv_location);
        watchBtn = view.findViewById(R.id.watch_btn);

        isStream = cctvMap.getItemID().equals("999");

        detail.setText(cctvMap.getName());
        String urldisplay = cctvMap.getUrlImage().replace("push-ios", "247")+
                cctvMap.getItemID();
        Log.i("URL", urldisplay);
        if(isStream)
            Picasso.with(getActivity()).load(R.mipmap.stream_only).into(thumb);
        else {
            Picasso.with(getActivity())
                    .load(urldisplay)
                    .fit()
                    .centerCrop()
                    .placeholder(R.mipmap.loading_image)
                    .error(R.mipmap.kamera_akses_error)
                    .into(thumb);
        }

        watchBtn.setOnClickListener(view1 -> {
            if(isStream){
                Intent intent = new Intent(getActivity(), CctvPlayer.class);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_IS_STREAMING), true);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_URL), cctvMap.getUrlVideo());
                getActivity().startActivity(intent);
            }else {
                Intent intent = new Intent(getActivity(), CctvPlayer.class);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_IS_STREAMING), false);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_URL), cctvMap.getUrlVideo()+
                        cctvMap.getItemID());
                getActivity().startActivity(intent);
            }
        });



        return view;
    }
}
