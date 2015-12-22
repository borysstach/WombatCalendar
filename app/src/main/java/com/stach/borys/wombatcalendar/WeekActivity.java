package com.stach.borys.wombatcalendar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    public static final int MAX_PAGE = 200;
    public static final String PICTURE_INTENT = "picture";
    public static final String MONTH_INTENT = "month";
    public static final String YEAR_INTENT = "year";
    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 123;
    public static final String PREFS_NAME = "MyPrefsFile";

    private ViewPager mViewPager;
    private List<String> mMonthStrings;
    private List<String> mMonthPictureStrings;
    private LruCache<String, Bitmap> mMemoryCache;
    private Integer mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_activity);

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

        makeCashForImages();
        setPosition(savedInstanceState);
        setStyle(mPosition);
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewPager();
    }

    /////////////////////////////// ADAPTER

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

    ////////////////////////// ENDING METHODS

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("position", mPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager = null;
        mMonthPictureStrings = null;
        mMemoryCache = null;
        mMonthStrings = null;
        mPosition = null;
    }

    ///////////////////////  HELPING METHODS

    private void setPosition(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("position");
        } else {
            mPosition = MAX_PAGE / 2;
        }
    }

    private void makeCashForImages() {
        Bitmap biggestPicture = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier("lipiec", "drawable", getPackageName()));
        final int cacheSize = biggestPicture.getByteCount()*3;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CALENDAR)) {
                    Toast.makeText(this, getResources().getString(R.string.permission_toast), Toast.LENGTH_LONG)
                            .show();

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CALENDAR},
                            MY_PERMISSIONS_REQUEST_READ_CALENDAR);
                }
            }
        } else {
            // Pre-Marshmallow
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("permission", true);
            editor.commit();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("permission", true);
                    editor.commit();
                } else {

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("permission", false);
                    editor.commit();
                }
                return;
            }
        }
    }

    private void setViewPager() {
        WeekPageAdapter weekPageAdapter = new WeekPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.week_viewpager);
        mViewPager.setAdapter(weekPageAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                setStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setStyle(int position) {

        final Calendar calendar = getCalendarFromPosition(position);
        Integer currentMonth = calendar.get(Calendar.MONTH);
        Integer currentYear = calendar.get(Calendar.YEAR);
        String colorString = mMonthPictureStrings.get(currentMonth) + "_day_title";
        Integer color = ContextCompat.getColor(this, getResources().getIdentifier(colorString, "color", getPackageName()));

        setMenuStyle(calendar, color);
        setActionBar(currentMonth, currentYear, color);
    }

    private void setActionBar(Integer currentMonth, Integer currentYear, Integer color) {

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        String title = mMonthStrings.get(currentMonth);
        final String picture = mMonthPictureStrings.get(currentMonth);
        String subtitle = "" + currentYear;

        mCollapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra(PICTURE_INTENT, picture);
                startActivity(intent);
            }
        });

        //check if change layout is needed
        if (mCollapsingToolbarLayout.getTitle() != title) {

            ImageView mToolBarImage = (ImageView) findViewById(R.id.tool_image);
            TextView mToolbarSubtitle = (TextView) findViewById(R.id.week_subtitle);

            mCollapsingToolbarLayout.setTitle(title);
            mToolbarSubtitle.setText(subtitle);
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(color);
            mCollapsingToolbarLayout.setExpandedTitleColor(color);
            mToolbarSubtitle.setTextColor(color);

            setPicture(picture, mToolBarImage);

            final int MONTH = currentMonth;
            cashPictures(MONTH);
        }
    }

    private void cashPictures(final int MONTH) {
        new Thread(new Runnable() {
            public void run() {
                //remove not needed  pictures
                Integer removePreviousMonth = (MONTH + 10) % 12;
                String removePreviousMonthStr = mMonthPictureStrings.get(removePreviousMonth);
                removeBitmapFromMemoryCache(removePreviousMonthStr);

                String removeNextMonthStr = mMonthPictureStrings.get((MONTH + 2) % 12);
                removeBitmapFromMemoryCache(removeNextMonthStr);
                Integer addPreviousMonth = (MONTH + 11) % 12;

                //add needed pictures
                String previousMonthStr = mMonthPictureStrings.get(addPreviousMonth);
                Bitmap previousMonthPic = BitmapFactory.decodeResource(getResources(),
                        getResources().getIdentifier(previousMonthStr, "drawable", getPackageName()));
                addBitmapToMemoryCache(previousMonthStr, previousMonthPic);

                String nextMonthStr = mMonthPictureStrings.get((MONTH + 1) % 12);
                Bitmap nextMonthPic = BitmapFactory.decodeResource(getResources(),
                        getResources().getIdentifier(nextMonthStr, "drawable", getPackageName()));
                addBitmapToMemoryCache(nextMonthStr, nextMonthPic);
            }
        }).start();
    }

    private void setPicture(String picture, ImageView mToolBarImage) {
        Bitmap bitmap = getBitmapFromMemCache(picture);
        if (bitmap != null) {
            mToolBarImage.setImageBitmap(bitmap);
        } else {
            mToolBarImage.setImageResource(getResources().getIdentifier(picture, "drawable", getPackageName()));
        }
    }

    private void setMenuStyle(final Calendar calendar, Integer color) {
        final FloatingActionMenu mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        final FloatingActionButton mFABadd = (FloatingActionButton) findViewById(R.id.fab_menu_add);
        final FloatingActionButton mFABback = (FloatingActionButton) findViewById(R.id.fab_menu_back);
        final FloatingActionButton mFABmonth = (FloatingActionButton) findViewById(R.id.fab_menu_month);
        mFloatingActionMenu.close(true);

        mFloatingActionMenu.setMenuButtonColorNormal(color);
        mFloatingActionMenu.setMenuButtonColorPressed(color);
        mFABadd.setColorNormal(color);
        mFABadd.setColorPressed(color);
        mFABback.setColorNormal(color);
        mFABback.setColorPressed(color);
        mFABmonth.setColorNormal(color);
        mFABmonth.setColorPressed(color);

        mFABadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionMenu.close(true);
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
                intent.putExtra(MONTH_INTENT, getCalendarFromPosition(mPosition).get(Calendar.MONTH));
                intent.putExtra(YEAR_INTENT, getCalendarFromPosition(mPosition).get(Calendar.YEAR));
                startActivity(intent);
            }
        });
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
}


