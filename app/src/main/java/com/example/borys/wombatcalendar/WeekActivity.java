package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    public static int thisWeek;

    private WeekPageAdapter mWeekPageAdapter;
    private ViewPager mViewPager;
    private Calendar mCalendar;
    private ImageView toolBarImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private List<String> mMonthStrings;
    private List<String> mMonthPictureStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view_pager);
        // Set Collapsing Toolbar layout to the screen
        mCalendar = Calendar.getInstance();
        thisWeek = mCalendar.get(Calendar.WEEK_OF_YEAR);
        mWeekPageAdapter = new WeekPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mWeekPageAdapter);
        mViewPager.setCurrentItem(MAX_PAGE / 2);

        //get image for toolbar
        toolBarImage = (ImageView) findViewById(R.id.tool_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mMonthStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.january),
                getString(R.string.february),
                getString(R.string.march),
                getString(R.string.april),
                getString(R.string.may),
                getString(R.string.june),
                getString(R.string.julay),
                getString(R.string.august),
                getString(R.string.september),
                getString(R.string.october),
                getString(R.string.november),
                getString(R.string.december)));

        mMonthPictureStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.p_january),
                getString(R.string.p_february),
                getString(R.string.p_march),
                getString(R.string.p_april),
                getString(R.string.p_may),
                getString(R.string.p_june),
                getString(R.string.p_julay),
                getString(R.string.p_august),
                getString(R.string.p_september),
                getString(R.string.p_october),
                getString(R.string.p_november),
                getString(R.string.p_december)));

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

    public class WeekPageAdapter extends FragmentStatePagerAdapter {


        public WeekPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int openWeek = position - MAX_PAGE / 2;
            return WeekFragment.newInstance(openWeek);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            WeekFragment primaryFragment = (WeekFragment)object;
            int currentMonth = primaryFragment.currentMonth;
            setActionBarStyle(mMonthStrings.get(currentMonth), mMonthPictureStrings.get(currentMonth));
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }

    public void setActionBarStyle(String title, String picture) {
        collapsingToolbarLayout.setTitle(title);
        String monthString = picture + "_day_title";
        int color = ContextCompat.getColor(this, getResources().getIdentifier(monthString, "color", getPackageName()));
        collapsingToolbarLayout.setCollapsedTitleTextColor(color);
        collapsingToolbarLayout.setExpandedTitleColor(color);
        toolBarImage.setImageResource(getResources().getIdentifier(picture, "drawable", getPackageName()));
        supportInvalidateOptionsMenu();
    }

}


