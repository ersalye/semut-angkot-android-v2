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


public class BookingAngkotListAdapter extends RecyclerView.Adapter<BookingAngkotListAdapter.ViewHolder> {

    private ArrayList<Angkot> trackers;
    private Context context;
    private OnDataSelected  onDataSelected;


    public BookingAngkotListAdapter(Context context, ArrayList<Angkot> trackers) {
        this.context = context;
        this.trackers = trackers;

    }



    @Override
    public BookingAngkotListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_booking_angkot, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       Angkot angkot = trackers.get(position);

       holder.iconAngkot.setImageDrawable(
               CustomDrawable.googleMaterial(context,
                       GoogleMaterial.Icon.gmd_face, 90, R.color.colorPrimaryDark)
       );

        AppPreferences preferences = new AppPreferences(context);

       holder.nameText.setText(angkot.getName());

       String tmp = "<b>Plat Nomor : </b>"+angkot.getAngkot().getPlatNomor()+"<br>";
        tmp += "<b>Jurusan: </b>"+angkot.getAngkot().getTrayek().getNama()+"<br>";
        tmp += "<b>Lokasi Terakhir: </b>"+TimeHelper.getTimeAgo(angkot.getAngkot().getLastUpdate())+"<br>";
        tmp += "Sekitar <b>"+NumUtils.round(new NumUtils().distance(
                preferences.getFloat(AppPreferences.KEY_MY_LATITUDE, 0),
                preferences.getFloat(AppPreferences.KEY_MY_LONGITUDE, 0),
                angkot.getAngkot().getLocation().getCoordinates().get(1),
                angkot.getAngkot().getLocation().getCoordinates().get(0), "K"
        ),2)+"</b> KM dari lokasi Anda";

        holder.detailText.setText(Html.fromHtml(tmp));
        holder.phoneText.setOnClickListener(view -> {

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

        public TextView nameText, detailText, phoneText;
        public ImageView iconAngkot;

        public ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.name_text);
            detailText = view.findViewById(R.id.detail_text);
            phoneText = view.findViewById(R.id.phone_text);
            iconAngkot = view.findViewById(R.id.icon_angkot);

        }
    }

}