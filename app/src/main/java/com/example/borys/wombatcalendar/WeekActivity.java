package com.example.borys.wombatcalendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    public static int thisWeek;

    private ViewPager mViewPager;
    private ImageView toolBarImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mToolbarSubtittle;
    private List<String> mMonthStrings;
    private List<String> mMonthPictureStrings;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.week_view_pager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        //get image for toolbar
        toolBarImage = (ImageView) findViewById(R.id.tool_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbarSubtittle = (TextView) findViewById(R.id.week_subtitle);
        //make cash for images
        final int cacheSize = 2 * 1024 * 1024;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
        //put first image to cash
        String currentMonthStr = mMonthPictureStrings.get(calendar.get(Calendar.MONTH));
        Bitmap currentMonthPic = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(currentMonthStr, "drawable", getPackageName()));
        addBitmapToMemoryCache(currentMonthStr, currentMonthPic);
        currentMonthPic = null;
        currentMonthStr = null;
        setActionBarStyle(MAX_PAGE/2);

        //set ViewPager
        WeekPageAdapter mWeekPageAdapter = new WeekPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.week_viewpager);
        mViewPager.setAdapter(mWeekPageAdapter);
        mViewPager.setCurrentItem(MAX_PAGE / 2);

        //listen change fragments
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //change collapsing appbar layout
                setActionBarStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //LruCach support methods:
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {

        return mMemoryCache.get(key);
    }

    public void removeBitmapFromMemoryCache(String key) {
            mMemoryCache.remove(key);
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
        public int getCount() {
            return MAX_PAGE;
        }
    }

    public void setActionBarStyle(int position) {
        int currentMonth = getCalendarFromPosition(position).get(Calendar.MONTH);
        String title = mMonthStrings.get(currentMonth);
        String picture = mMonthPictureStrings.get(currentMonth);
        String subtitle = "" + (getCalendarFromPosition(position).get(Calendar.YEAR));
        //check if change layout is needed
        if (mCollapsingToolbarLayout.getTitle() != title) {
            int weekOfTheYear = position - MAX_PAGE / 2;
            //set title
            mCollapsingToolbarLayout.setTitle(title);
            mToolbarSubtittle.setText(subtitle);
            Log.d("year",subtitle);
            //get month color
            String monthString = picture + "_day_title";
            int color = ContextCompat.getColor(this, getResources().getIdentifier(monthString, "color", getPackageName()));
            //set colors
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(color);
            mCollapsingToolbarLayout.setExpandedTitleColor(color);
            mToolbarSubtittle.setTextColor(color);
            //set image
            toolBarImage.setImageBitmap(getBitmapFromMemCache(picture));
            monthString = null;
            //in new thread cash images for nest months
            final int MONTH = currentMonth;
            new Thread(new Runnable() {
                public void run() {
                    int removePreviousMonth = (MONTH -2)%12;
                    if (removePreviousMonth < 0){
                        removePreviousMonth = -removePreviousMonth;
                    }
                    String removePreviousMonthStr = mMonthPictureStrings.get(removePreviousMonth);
                    removeBitmapFromMemoryCache(removePreviousMonthStr);
                    String removeNextMonthStr = mMonthPictureStrings.get((MONTH + 2) % 12);
                    removeBitmapFromMemoryCache(removeNextMonthStr);

                    int addPreviousMonth = (MONTH -1)%12;
                    if (addPreviousMonth < 0){
                        addPreviousMonth = -addPreviousMonth;
                    }

                    String previousMonthStr = mMonthPictureStrings.get(addPreviousMonth);
                    Bitmap previousMonthPic = BitmapFactory.decodeResource(getResources(),
                            getResources().getIdentifier(previousMonthStr, "drawable", getPackageName()));
                    addBitmapToMemoryCache(previousMonthStr, previousMonthPic);

                    String nextMonthStr = mMonthPictureStrings.get((MONTH + 1) % 12);
                    Bitmap nextMonthPic = BitmapFactory.decodeResource(getResources(),
                            getResources().getIdentifier(nextMonthStr, "drawable", getPackageName()));
                    addBitmapToMemoryCache(nextMonthStr, nextMonthPic);
                    //without i have memory leak
                    removePreviousMonthStr = null;
                    removeNextMonthStr = null;
                    previousMonthStr = null;
                    nextMonthStr = null;
                    previousMonthPic = null;
                    nextMonthPic = null;
                }
            }).start();
        }
        picture = null;
        title = null;
    }

    //get month from ViewPagerPosition
    public static Calendar getCalendarFromPosition(int position) {
        //get data from first page
        Calendar tempCalendar = Calendar.getInstance();
        //set first day
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //add scrolled pages
        tempCalendar.add(Calendar.WEEK_OF_YEAR, position - MAX_PAGE / 2);
        return tempCalendar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolbarSubtittle = null;
        mViewPager = null;
        mCollapsingToolbarLayout = null;
        mMonthPictureStrings = null;
        mMonthStrings = null;
    }
}


