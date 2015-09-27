package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    public static int thisWeek;

    private WeekPageAdapter mWeekPageAdapter;
    private ViewPager mViewPager;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendar = Calendar.getInstance();
        thisWeek = mCalendar.get(Calendar.WEEK_OF_YEAR);
        mWeekPageAdapter = new WeekPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mWeekPageAdapter);
        mViewPager.setCurrentItem(MAX_PAGE / 2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WeekPageAdapter extends FragmentStatePagerAdapter {


        public WeekPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeekFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
    public void setActionBarTitle(String title) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(title);
        this.invalidateOptionsMenu();
    }
}


