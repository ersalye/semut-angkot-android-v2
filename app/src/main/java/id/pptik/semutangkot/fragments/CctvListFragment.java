package id.pptik.semutangkot.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import id.pptik.semutangkot.R;
import id.pptik.semutangkot.adapters.CctvListAdapter;
import id.pptik.semutangkot.models.Cctv;


public class CctvListFragment extends Fragment {

    ListView mListViewCctv;
    private ArrayList<Cctv> list = new ArrayList<Cctv>();

    CctvListAdapter cctvListAdapter;

    public CctvListFragment(){

    }

    public void setData(ArrayList<Cctv> list){
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View convertView = inflater.inflate(R.layout.fragment_cctv_list, container, false);
        mListViewCctv = convertView.findViewById(R.id.list_cctv);
        cctvListAdapter = new CctvListAdapter(getActivity(), list);
        mListViewCctv.setAdapter(cctvListAdapter);

        return convertView;
    }
}