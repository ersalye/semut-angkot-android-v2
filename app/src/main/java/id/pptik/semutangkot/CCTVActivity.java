package id.pptik.semutangkot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.pptik.semutangkot.fragments.CctvListFragment;
import id.pptik.semutangkot.fragments.map.CctvMapFragment;
import id.pptik.semutangkot.interfaces.RestResponHandler;
import id.pptik.semutangkot.models.Cctv;
import id.pptik.semutangkot.models.Profile;
import id.pptik.semutangkot.models.RequestStatus;
import id.pptik.semutangkot.networking.CommonRest;
import id.pptik.semutangkot.ui.CommonDialogs;
import id.pptik.semutangkot.ui.LoadingIndicator;
import id.pptik.semutangkot.utils.ProfileUtils;

public class CCTVActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabs;
    private ArrayList<Cctv> list = new ArrayList<>();
    LoadingIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.viewpager);
        mTabs = findViewById(R.id.tabs);

        indicator = new LoadingIndicator(this);

        Profile profile = ProfileUtils.getProfile(getApplicationContext());
        indicator.show();
        CommonRest.bandungCctv(profile.getToken(), (jResult, type) -> {
            indicator.hide();
            switch (type){
                case CommonRest.ENDPOINT_ERROR:
                    CommonDialogs.showEndPointError(getParent());
                    break;
                case CommonRest.ENDPOINT_CCTV:
                    RequestStatus status = ProfileUtils.getReqStatus(jResult.toString());
                    if(status.getSuccess()){
                        try {
                            JSONArray array = jResult.getJSONArray("data");
                            for(int i = 0; i < array.length(); i++){
                                Cctv cctv = new Gson().fromJson(array.get(i).toString(), Cctv.class);
                                list.add(cctv);
                            }
                            initTab();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        CommonDialogs.showError(getParent(), status.getMessage());
                    }
                    break;
            }
        });
    }


    void initTab(){
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CctvListFragment cctvListFragment = new CctvListFragment();
        cctvListFragment.setData(list);
        adapter.addFragment(cctvListFragment, "LIST");

        CctvMapFragment cctvMapFragment = new CctvMapFragment();
        cctvMapFragment.setData(list);
        adapter.addFragment(cctvMapFragment, "MAP");


        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
