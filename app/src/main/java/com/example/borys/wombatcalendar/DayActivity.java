package com.example.borys.wombatcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;

    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private List<String> mDaysStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_view_pager);

        mDaysStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.sunday),
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday)));


        mCalendar = Calendar.getInstance();
        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", 2015);
        mMonth = intent.getIntExtra("month", 0);
        mDay = intent.getIntExtra("day",1);
        mCalendar.set(mYear, mMonth, mDay);

        DayPageAdapter mDayPageAdapter = new DayPageAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.day_viewpager);
        mViewPager.setAdapter(mDayPageAdapter);
        mViewPager.setCurrentItem(MAX_PAGE / 2);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }

    public class DayPageAdapter extends FragmentStatePagerAdapter {

        public DayPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayFragment.newInstance(mYear, mMonth, mDay, position - MAX_PAGE / 2);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            Calendar calendar = (Calendar) mCalendar.clone();
            calendar.add(Calendar.DAY_OF_MONTH, position - MAX_PAGE / 2);
            mCollapsingToolbarLayout.setTitle((mDaysStrings.get(calendar.get(Calendar.DAY_OF_WEEK) - 1) + " " + calendar.get(Calendar.DAY_OF_MONTH)));
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
