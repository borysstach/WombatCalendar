package com.stach.borys.wombatcalendar.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CalendarContract.Instances;

import com.stach.borys.wombatcalendar.WeekActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * help with takieng/putting data from Calendar Content Provider
 */
public class CalendarDataSource {
    private Context mContext;

    public CalendarDataSource(Context context) {
        mContext = context;
    }

    public List<Event> getEvents(long beginMillis, long endMillis) {

        List<Event> allEvents = new ArrayList<>();

        ///////////searching for Events id this day from Instances table

        //what we want from data
        String[] instanceQuery =
                new String[]{
                        Instances._ID,
                        Instances.BEGIN,
                        Instances.END,
                        Instances.TITLE,
                        Instances.ALL_DAY,
                        Instances.EVENT_ID};

        SharedPreferences settings = mContext.getSharedPreferences(WeekActivity.PREFS_NAME, 0);
        if (settings.getBoolean("permission", false)) {
            Cursor cursor = Instances.query(mContext.getContentResolver(), instanceQuery, beginMillis, endMillis);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        //save id of every event this day
                        Event singleEvent = new Event();
                        singleEvent.setId(cursor.getLong(0));
                        singleEvent.setBegin(cursor.getLong(1));
                        singleEvent.setEnd(cursor.getLong(2));
                        singleEvent.setTitle(cursor.getString(3));
                        singleEvent.setAllDay(cursor.getInt(4));
                        singleEvent.setId(cursor.getLong(5));
                        allEvents.add(singleEvent);
                    } while (cursor.moveToNext());
                }
                //always close cursor!!
                cursor.close();
            }
        }

        return allEvents;
    }

    public static long getBeginInMillis(Calendar calendar) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 1);
        return beginCalendar.getTimeInMillis();
    }

    public static long getEndInMillis(Calendar calendar) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59);
        return endCalendar.getTimeInMillis();
    }

}
