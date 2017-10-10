package id.pptik.semutangkot.fragments.map;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.models.angkot.Angkot;

public class MapAngkotFragment extends Fragment {

    ImageView mapIconType;
    TextView reportTitleText;
    ImageView imageDescription;
    TextView reportSubTitleText;
    TextView postDateText;
    TextView detailText;
    TextView contentDescriptionText;
    String title, subTitle, postDate, contentDescription, detail;

    Angkot angkot;

    public void setData(Angkot object){
        this.angkot = object;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_angkot_fragment, container, false);

        mapIconType = (ImageView)view.findViewById(R.id.map_icon_type);
        reportTitleText = (TextView)view.findViewById(R.id.report_title);
        imageDescription = (ImageView)view.findViewById(R.id.image_description);
        reportSubTitleText = (TextView)view.findViewById(R.id.report_sub_title);
        postDateText = (TextView)view.findViewById(R.id.post_date);
        contentDescriptionText = (TextView)view.findViewById(R.id.content_description);
        detailText = (TextView)view.findViewById(R.id.post_detail);


        mapIconType.setImageDrawable(new IconicsDrawable(getActivity())
                .color(getActivity().getResources().getColor(R.color.lynch))
                .sizeDp(34)
                .icon(GoogleMaterial.Icon.gmd_place));

        title = "Angkot";
        subTitle = "Plat Nomor : "+ angkot.getAngkot().getPlatNomor();
        postDate = angkot.getAngkot().getLastUpdate();
        contentDescription = angkot.getAngkot().getJumlahPenumpang().toString();
        detail = angkot.getAngkot().getTrayek().getNama();

        imageDescription.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.angkot_icon));
        reportTitleText.setText(title);
        reportSubTitleText.setText(subTitle);
        String tmp = "<b>Lokasi Tanggal : </b>"+postDate;
        postDateText.setText(Html.fromHtml(tmp));
        tmp = "<b>Jurusan : </b>"+detail;
        detailText.setText(Html.fromHtml(tmp));
        tmp = "<b>Jumlah Penumpang : </b>"+contentDescription;
        contentDescriptionText.setText(Html.fromHtml(tmp));

        return view;
    }

}
