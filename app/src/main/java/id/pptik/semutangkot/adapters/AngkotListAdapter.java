package id.pptik.semutangkot.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.helper.AppPreferences;
import id.pptik.semutangkot.helper.TimeHelper;
import id.pptik.semutangkot.models.AngkotPath;
import id.pptik.semutangkot.models.TmbModel;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.models.mapview.Tracker;
import id.pptik.semutangkot.utils.CompareDate;
import id.pptik.semutangkot.utils.CustomDrawable;
import id.pptik.semutangkot.utils.NumUtils;


public class AngkotListAdapter extends RecyclerView.Adapter<AngkotListAdapter.ViewHolder> {

    private ArrayList<Object> trackers;
    private Context context;
    private OnDataSelected  onDataSelected;


    public AngkotListAdapter(Context context, OnDataSelected onDataSelected, ArrayList<Object> trackers) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.trackers = trackers;

    }



    @Override
    public AngkotListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tracker_filter, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AppPreferences preferences = new AppPreferences(context);
        if(trackers.get(position) instanceof  Angkot) {
            final Angkot angkot = (Angkot) trackers.get(position);
            String detail = "<b>Jurusan : </b>" + angkot.getAngkot().getTrayek().getNama();
            detail += "<br><b>Lokasi Tanggal : </b>" + angkot.getAngkot().getLastUpdate();
            detail += " <br> <b><font color='blue'>" + TimeHelper.getTimeAgo(angkot.getAngkot().getLastUpdate()) + "</font></b>";
            detail += "Sekitar <b>"+NumUtils.round(new NumUtils().distance(
                    preferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0),
                    preferences.getFloat(AppPreferences.KEY_MY_LONGITUDE, 0),
                    angkot.getAngkot().getLocation().getCoordinates().get(1),
                    angkot.getAngkot().getLocation().getCoordinates().get(0), "K"
            ),2)+"</b> KM dari lokasi Anda";

            holder.gpsNameText.setText("Angkot | "+angkot.getAngkot().getPlatNomor());
            holder.gpsDetail.setText(Html.fromHtml(detail), TextView.BufferType.SPANNABLE);

            holder.carIcon.setImageDrawable(CustomDrawable.googleMaterial(
                    context, GoogleMaterial.Icon.gmd_airport_shuttle,
                    35, R.color.colorPrimaryDark
            ));

        }else {
            final TmbModel tmbModel = (TmbModel) trackers.get(position);
            String detail = "<b>Detail : </b>" + tmbModel.getKoridor();
            detail += "<br><b>Lokasi Tanggal : </b>" + NumUtils.convertMongoDateToAgo7(tmbModel.getGpsdatetime()) ;
            detail += " <br> <b><font color='blue'>" + TimeHelper.getTimeAgo(NumUtils.convertMongoDateToAgo7(tmbModel.getGpsdatetime())) + "</font></b><br>";
            detail += "Sekitar <b>"+NumUtils.round(new NumUtils().distance(
                    preferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0),
                    preferences.getFloat(AppPreferences.KEY_MY_LONGITUDE, 0),
                    tmbModel.getLocation().getCoordinates().get(1),
                    tmbModel.getLocation().getCoordinates().get(0), "K"
            ),2)+"</b> KM dari lokasi Anda";

            holder.gpsNameText.setText("TMB | "+tmbModel.getBuscode());

            holder.gpsDetail.setText(Html.fromHtml(detail), TextView.BufferType.SPANNABLE);

            holder.carIcon.setImageDrawable(CustomDrawable.googleMaterial(
                    context, GoogleMaterial.Icon.gmd_directions_bus,
                    35, R.color.colorPrimaryDark
            ));
        }

        holder.cardView.setOnClickListener(view1 -> {
            onDataSelected.onDataSelected(view1, position);
        });

    }



    @Override
    public int getItemCount() {
        return trackers.size();
    }

    public interface OnDataSelected {

        void onDataSelected(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gpsNameText, gpsDetail;
        public CardView cardView;
        public ImageView carIcon;

        public ViewHolder(View view) {
            super(view);
            gpsNameText = view.findViewById(R.id.gps_name);
            gpsDetail = view.findViewById(R.id.gps_detail);
            cardView = view.findViewById(R.id.mainView);
            carIcon = view.findViewById(R.id.icon_angkot);

        }
    }

}