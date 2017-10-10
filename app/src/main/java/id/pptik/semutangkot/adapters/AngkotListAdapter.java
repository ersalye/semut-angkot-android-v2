package id.pptik.semutangkot.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.models.AngkotPath;
import id.pptik.semutangkot.models.angkot.Angkot;
import id.pptik.semutangkot.models.mapview.Tracker;
import id.pptik.semutangkot.utils.CompareDate;


public class AngkotListAdapter extends RecyclerView.Adapter<AngkotListAdapter.ViewHolder> {

    private Angkot[] trackers;
    private Context context;
    private OnDataSelected  onDataSelected;


    public AngkotListAdapter(Context context, OnDataSelected onDataSelected, Angkot[] trackers) {
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
        final Angkot angkot = trackers[position];

        String detail = "<b>Jurusan : </b>"+angkot.getAngkot().getTrayek().getNama();
        detail += "<br><b>Lokasi Tanggal : </b>"+angkot.getAngkot().getLastUpdate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis() - 3600 * 10);
        String dateNow = df.format(date);
        String dateToCompare = angkot.getAngkot().getLastUpdate().replace('-', '/');
        boolean isExpired = CompareDate.compare(dateToCompare, dateNow);
        if(isExpired) detail += " <br> <b><font color='red'>LOKASI TIDAK UPDATE</font></b>";
        else detail += " <br> <b><font color='blue'>LOKASI UPDATE</font></b>";

        holder.gpsNameText.setText(angkot.getAngkot().getPlatNomor());
        holder.gpsLocText.setText(angkot.getAngkot().getJumlahPenumpang().toString());
        holder.gpsDetail.setText(Html.fromHtml(detail), TextView.BufferType.SPANNABLE);

        holder.cardView.setOnClickListener(view1 -> {
            onDataSelected.onDataSelected(view1, position);
        });

    }



    @Override
    public int getItemCount() {
        return trackers.length;
    }

    public interface OnDataSelected {

        void onDataSelected(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gpsNameText, gpsLocText, gpsDetail;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);
            gpsNameText = view.findViewById(R.id.gps_name);
            gpsLocText = view.findViewById(R.id.gps_location);
            gpsDetail = view.findViewById(R.id.gps_detail);
            cardView = view.findViewById(R.id.mainView);
        }
    }

}