package id.pptik.semutangkot.fragments.map;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import id.pptik.semutangkot.R;


public class SubmitTagFragment extends Fragment implements TextWatcher{
    private TextView titleText;
    private TextView dateText;
    private TextView counterText;
    private EditText remarks;
    private ImageView thumb;
    private ImageButton closeButton;
    private Button submitButton;

    private int postID;
    private int subPostID;
    Date currentDate;
    private ProgressDialog dialog;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_tag, container, false);

        titleText = view.findViewById(R.id.title);
        dateText = view.findViewById(R.id.date);
        counterText = view.findViewById(R.id.counter);
        remarks = view.findViewById(R.id.remarks);
        remarks.addTextChangedListener(this);
        thumb = view.findViewById(R.id.thumb);
        submitButton = view.findViewById(R.id.submitButton);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Memuat...");
        dialog.setCancelable(false);

        currentDate = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEEE, dd MMWW yyyy HH:mm:ss");
        String formattedCurrentDate = format.format(currentDate);
        dateText.setText(formattedCurrentDate);


        titleText.setText("Laporan Angkot");
        thumb.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.angkot_icon));

        closeButton = view.findViewById(R.id.closeButton);



        closeButton.setOnClickListener(v -> getActivity().finish());
        submitButton.setOnClickListener(v -> {
            submit();
        });

        return view;
    }



    private void submit() {
        if(remarks.getText().toString().equals("")) Toast.makeText(getActivity(), "Anda belum mengisi keterangan", Toast.LENGTH_LONG).show();
        else {
            dialog.show();

        }

    }



    // listener text
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        counterText.setText(s.length() + " of 128");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}