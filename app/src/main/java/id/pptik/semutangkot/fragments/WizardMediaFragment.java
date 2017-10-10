package id.pptik.semutangkot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.utils.CustomDrawable;


public class WizardMediaFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private ImageView icon;
    private TextView title;
    private TextView text;

    public static WizardMediaFragment newInstance(int position) {
        WizardMediaFragment f = new WizardMediaFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_media,
                container, false);
        icon = rootView.findViewById(R.id.fragment_wizard_media_icon);
        title = rootView.findViewById(R.id.fragment_wizard_media_title);
        text = rootView.findViewById(R.id.fragment_wizard_media_text);

        if (position == 0) {
            icon.setImageDrawable(CustomDrawable.googleMaterial(
                    getActivity(),
                    GoogleMaterial.Icon.gmd_map,
                    110,
                    R.color.cpb_white
            ));
            title.setText("Realtime Tracking");
            text.setText("Pantau aktifitas Angkot, lihat secara Realtime lokasi Angkot yang tersedia");
        } else if (position == 1) {
            icon.setImageDrawable(CustomDrawable.googleMaterial(
                    getActivity(),
                    GoogleMaterial.Icon.gmd_videocam,
                    110,
                    R.color.cpb_white
            ));
            title.setText("Live CCTV");
            text.setText("Pantau CCTV yang tersebar dibeberapa ruas jalan");
        } else {
            icon.setImageDrawable(CustomDrawable.googleMaterial(
                    getActivity(),
                    GoogleMaterial.Icon.gmd_directions,
                    110,
                    R.color.cpb_white
            ));
            title.setText("Rute Angkot");
            text.setText("Lihat peta rute Angkot yang tersebar di sekitar Bandung");
        }

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

}