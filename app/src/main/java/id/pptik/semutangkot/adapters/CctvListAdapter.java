package id.pptik.semutangkot.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.pptik.semutangkot.CctvPlayer;
import id.pptik.semutangkot.R;
import id.pptik.semutangkot.models.Cctv;
import id.pptik.semutangkot.utils.StringResources;

public class CctvListAdapter extends BaseAdapter {
    private ArrayList<Cctv> cctvMaps = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private TextView mTextViewCctvName;
    private ImageView mImagePreview;



    public CctvListAdapter(Context context, ArrayList<Cctv> cctvMaps) {
        this.mContext = context;
        this.cctvMaps = cctvMaps;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (cctvMaps != null) {
            return cctvMaps.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (cctvMaps != null) {
            return cctvMaps.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        Cctv cctv = cctvMaps.get(position);

        boolean isStreaming = (cctv.getItemID().equals("999"));

                convertView = mInflater.inflate(R.layout.layout_list_cctv, null);
        mTextViewCctvName = convertView.findViewById(R.id.cctv_name);
        mImagePreview = convertView.findViewById(R.id.cctv_preview);
        mTextViewCctvName.setText(cctv.getName());

        String urldisplay = cctvMaps.get(position).getUrlImage().replace("push-ios", "247")
                +cctv.getItemID();
        if(isStreaming){
            Picasso.with(mContext).load(R.mipmap.stream_only).into(mImagePreview);
        }else {
            Picasso.with(mContext)
                    .load(urldisplay)
                    .fit()
                    .centerCrop()
                    .placeholder(R.mipmap.loading_image)
                    .error(R.mipmap.kamera_akses_error)
                    .into(mImagePreview);
        }

        convertView.setOnClickListener(view -> {
            if(isStreaming){
                Intent intent = new Intent(mContext, CctvPlayer.class);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_IS_STREAMING), true);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_URL), cctv.getUrlVideo());
                mContext.startActivity(intent);
            }else {
                Intent intent = new Intent(mContext, CctvPlayer.class);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_IS_STREAMING), false);
                intent.putExtra(StringResources.get(R.string.INTENT_VIDEO_URL), cctv.getUrlVideo()+
                        cctv.getItemID());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}