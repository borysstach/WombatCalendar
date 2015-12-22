package com.stach.borys.wombatcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    public static final String POSITION = "day_pager_position";

    private Calendar mCalendar;
    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;
    Integer mPosition;
    private ViewPager mViewPager;
    private List<String> mDaysStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        mDaysStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.sunday),
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday)));

        setCalendar();
        setPosition(savedInstanceState);
        setToolbarTitle(mCalendar);
        setMenu(mCalendar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewPager();
    }

    /////////////////////////////// ADAPTER

    public class DayPageAdapter extends FragmentStatePagerAdapter {

        public DayPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayFragment.newInstance(mYear, mMonth, mDay, position - MAX_PAGE / 2);
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }

    ////////////////////////// ENDING METHODS

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(POSITION, mPosition);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCalendar = null;
        mYear = null;
        mMonth = null;
        mDay = null;
        mDaysStrings = null;
    }

    ///////////////////////  HELPING METHODS

    private void setCalendar() {
        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", 2015);
        mMonth = intent.getIntExtra("month", 0);
        mDay = intent.getIntExtra("day", 1);
        mCalendar = Calendar.getInstance();
        mCalendar.set(mYear, mMonth, mDay);
    }

    private void setPosition(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(POSITION);
        } else {
            mPosition = MAX_PAGE / 2;
        }
    }

    private void setViewPager() {
        DayPageAdapter mDayPageAdapter = new DayPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.day_viewpager);
        mViewPager.setAdapter(mDayPageAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                final Calendar calendar = (Calendar) mCalendar.clone();
                calendar.add(Calendar.DAY_OF_MONTH, position - MAX_PAGE / 2);
                setToolbarTitle(calendar);
                setMenu(calendar);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setToolbarTitle(Calendar calendar) {
        String dayofWeek = mDaysStrings.get(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        Integer dayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String title = dayofWeek + " " + dayofMonth;
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(title);
    }

    private void setMenu(final Calendar calendar) {

        final FloatingActionMenu mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        FloatingActionButton mFABadd = (FloatingActionButton) findViewById(R.id.fab_menu_add);
        FloatingActionButton mFABback = (FloatingActionButton) findViewById(R.id.fab_menu_back);
        FloatingActionButton mFABmonth = (FloatingActionButton) findViewById(R.id.fab_menu_month);
        mFloatingActionMenu.close(true);

        mFABadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.close(true);;
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
                startActivity(intent);
            }
        });

        mFABback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.close(true);
                mViewPager.setCurrentItem(MAX_PAGE / 2);
            }
        });

        mFABmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.close(true);
                Intent intent = new Intent(v.getContext(), MonthActivity.class);
                intent.putExtra(WeekActivity.MONTH_INTENT, calendar.get(Calendar.MONTH));
                intent.putExtra(WeekActivity.YEAR_INTENT, calendar.get(Calendar.YEAR));
                startActivity(intent);
            }
        });
    }

}
