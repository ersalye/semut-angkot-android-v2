package id.pptik.semutangkot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import id.pptik.semutangkot.fragments.WizardMediaFragment;
import id.pptik.semutangkot.helper.AppPreferences;


public class WizardActivity extends AppCompatActivity {

    private MyPagerAdapter adapter;
    private ViewPager pager;
    private TextView previousButton;
    private TextView nextButton;
    private TextView navigator;
    private int currentItem;
    private AppPreferences mAppPreferences;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mAppPreferences = new AppPreferences(mContext);
        boolean isLoggedIn = mAppPreferences.getBoolean(AppPreferences.KEY_IS_FIRST_LAUNCH, false);
        if(isLoggedIn) toLoginActivity();

        currentItem = 0;

        pager = findViewById(R.id.activity_wizard_media_pager);
        previousButton = findViewById(R.id.activity_wizard_media_previous);
        nextButton = findViewById(R.id.activity_wizard_media_next);
        navigator = findViewById(R.id.activity_wizard_media_possition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        previousButton.setVisibility(View.INVISIBLE);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentItem);
        setNavigator();

        pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int position) {
                if (pager.getCurrentItem() == 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                } else {
                    previousButton.setVisibility(View.VISIBLE);
                }
                if (pager.getCurrentItem() == (pager.getAdapter().getCount() - 1)) {
                    nextButton.setText("LOGIN");
                } else {
                    nextButton.setText("NEXT");
                }
                setNavigator();
            }
        });

        previousButton.setOnClickListener(v -> {
            if (pager.getCurrentItem() != 0) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
            setNavigator();
        });

        nextButton.setOnClickListener(v -> {
            if (pager.getCurrentItem() != (pager.getAdapter().getCount() - 1)) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            } else {
                toLoginActivity();
                mAppPreferences.put(AppPreferences.KEY_IS_FIRST_LAUNCH, true);
            }
            setNavigator();
        });

    }

    public void setNavigator() {
        String navigation = "";
        for (int i = 0; i < adapter.getCount(); i++) {
            if (i == pager.getCurrentItem()) {
                navigation += getString(R.string.material_icon_point_full)
                        + "  ";
            } else {
                navigation += getString(R.string.material_icon_point_empty)
                        + "  ";
            }
        }
        navigator.setText(navigation);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return WizardMediaFragment.newInstance(position);
            } else if (position == 1) {
                return WizardMediaFragment.newInstance(position);
            } else {
                return WizardMediaFragment.newInstance(position);
            }
        }
    }


    private void toLoginActivity(){
        finish();
    }
}