package com.stach.borys.wombatcalendar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    public static final String DAY_INTENT_PICTURE = "picture";

    private ViewPager mViewPager;
    private ImageView mToolBarImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mToolbarSubtitle;
    private List<String> mMonthStrings;
    private List<String> mMonthPictureStrings;
    private LruCache<String, Bitmap> mMemoryCache;
    private Integer mPosition;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.week_view_pager);
        WeekPageAdapter mWeekPageAdapter = new WeekPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.week_viewpager);
        mViewPager.setAdapter(mWeekPageAdapter);

        mToolBarImage = (ImageView) findViewById(R.id.tool_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbarSubtitle = (TextView) findViewById(R.id.week_subtitle);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);

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

        //make cash for images
        final int cacheSize = 55 * 1024 * 1024;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize);

        if(savedInstanceState != null) {
            setActionBarStyle(savedInstanceState.getInt("position"));
            mViewPager.setCurrentItem(savedInstanceState.getInt("position"));
        } else {
            setActionBarStyle(MAX_PAGE / 2);
            mViewPager.setCurrentItem(MAX_PAGE / 2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        mPosition = position;
        Integer currentMonth = getCalendarFromPosition(position).get(Calendar.MONTH);
        String title = mMonthStrings.get(currentMonth);
        final String picture = mMonthPictureStrings.get(currentMonth);
        String subtitle = "" + (getCalendarFromPosition(position).get(Calendar.YEAR));

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, position - MAX_PAGE / 2);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
                startActivity(intent);
            }
        });

        mCollapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra(DAY_INTENT_PICTURE, picture);
                startActivity(intent);
            }
        });
        //check if change layout is needed
        if (mCollapsingToolbarLayout.getTitle() != title) {

            mCollapsingToolbarLayout.setTitle(title);
            mToolbarSubtitle.setText(subtitle);
            String monthString = picture + "_day_title";

            Integer color = ContextCompat.getColor(this, getResources().getIdentifier(monthString, "color", getPackageName()));
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(color);
            mCollapsingToolbarLayout.setExpandedTitleColor(color);
            mToolbarSubtitle.setTextColor(color);
            mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));

            Bitmap bitmap = getBitmapFromMemCache(picture);
            if (bitmap != null) {
                mToolBarImage.setImageBitmap(bitmap);
            } else {
                mToolBarImage.setImageResource(getResources().getIdentifier(picture, "drawable", getPackageName()));
            }
            final int MONTH = currentMonth;
            color = null;
            bitmap = null;
            monthString = null;
            title = null;
            subtitle = null;
            currentMonth = null;

            //in new thread, cash images for next months
            new Thread(new Runnable() {
                public void run() {
                    Integer removePreviousMonth = (MONTH +10)%12;
                    String removePreviousMonthStr = mMonthPictureStrings.get(removePreviousMonth);
                    removeBitmapFromMemoryCache(removePreviousMonthStr);
                    String removeNextMonthStr = mMonthPictureStrings.get((MONTH + 2) % 12);
                    removeBitmapFromMemoryCache(removeNextMonthStr);
                    Integer addPreviousMonth = (MONTH +11)%12;
                    String previousMonthStr = mMonthPictureStrings.get(addPreviousMonth);
                    Bitmap previousMonthPic = BitmapFactory.decodeResource(getResources(),
                            getResources().getIdentifier(previousMonthStr, "drawable", getPackageName()));
                    addBitmapToMemoryCache(previousMonthStr, previousMonthPic);
                    String nextMonthStr = mMonthPictureStrings.get((MONTH + 1) % 12);
                    Bitmap nextMonthPic = BitmapFactory.decodeResource(getResources(),
                            getResources().getIdentifier(nextMonthStr, "drawable", getPackageName()));
                    addBitmapToMemoryCache(nextMonthStr, nextMonthPic);

                    addPreviousMonth = null;
                    removePreviousMonth = null;
                    removePreviousMonthStr = null;
                    removeNextMonthStr = null;
                    previousMonthStr = null;
                    nextMonthStr = null;
                    previousMonthPic = null;
                    nextMonthPic = null;
                }
            }).start();
        }
    }


    public static Calendar getCalendarFromPosition(int position) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        tempCalendar.add(Calendar.WEEK_OF_YEAR, position - MAX_PAGE / 2);
        return tempCalendar;
    }

    //LruCache support methods:
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("position", mPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolbarSubtitle = null;
        mViewPager = null;
        mCollapsingToolbarLayout = null;
        mMonthPictureStrings = null;
        mMemoryCache = null;
        mMonthStrings = null;
        mPosition = null;
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


