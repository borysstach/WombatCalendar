package com.stach.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class MonthActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    private Integer mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        if(savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("position");
        } else {
            Calendar today = Calendar.getInstance();
            Integer thisMonth = today.get(Calendar.MONTH);
            Integer intentMonth = getIntent().getIntExtra(WeekActivity.MONTH_INTENT, thisMonth);
            Integer thisYear = today.get(Calendar.YEAR);
            Integer intentYear = getIntent().getIntExtra(WeekActivity.YEAR_INTENT, thisYear);
            mPosition = MAX_PAGE/2 + (intentYear - thisYear)*12 + intentMonth - thisMonth ;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MonthPageAdapter monthPageAdapter = new MonthPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.month_viewpager);
        viewPager.setAdapter(monthPageAdapter);
        viewPager.setCurrentItem(mPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
               mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("position", mPosition);
    }

    public class MonthPageAdapter extends FragmentStatePagerAdapter {

        public MonthPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int openWeek = position - MAX_PAGE / 2;
            return MonthFragment.newInstance(openWeek);
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}
