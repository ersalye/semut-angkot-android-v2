package id.pptik.semutangkot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.models.AngkotPath;
import id.pptik.semutangkot.utils.CustomDrawable;


public class PathListAdapter extends RecyclerView.Adapter<PathListAdapter.ViewHolder> {

    private List<AngkotPath> apps;
    private Context context;
    private OnDataSelected  onDataSelected;


    public PathListAdapter(Context context, OnDataSelected onDataSelected, List<AngkotPath> apps) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.apps = apps;

    }

    public void addItem(AngkotPath item) {
        this.apps.add(0, item);
        notifyItemInserted(0);
    }


    @Override
    public PathListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_path, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AngkotPath app = apps.get(position);
        holder.mTitle.setText(app.getTrayekName());
        holder.mDesc.setText("Kode Trayek : "+app.getTrayekID()
                +" | Jarak : "+app.getTrayekDistance());
        holder.mThumb.setImageDrawable(CustomDrawable.googleMaterial(
                context,
                GoogleMaterial.Icon.gmd_directions,
                48, R.color.colorPrimaryDark
        ));

        holder.mPathBtb.setOnClickListener(view -> onDataSelected.onDataSelected(view, position));

    }


    public void removeItem(int position) {
        apps.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return apps.size();
    }

    public interface OnDataSelected {

        void onDataSelected(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mThumb;
        public TextView mTitle;
        public TextView mDesc;
        public AppCompatButton mPathBtb;

        public ViewHolder(View view) {
            super(view);

            mThumb = view.findViewById(R.id.thumb);
            mTitle = view.findViewById(R.id.title);
            mDesc = view.findViewById(R.id.desc);
            mPathBtb = view.findViewById(R.id.path_btn);
        }
    }

}