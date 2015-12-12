package com.example.borys.wombatcalendar.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Instances;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * help with takieng/putting data from Calendar Content Provider
 */
public class CalendarDataSource {
    private Context mContext;

    public CalendarDataSource(Context context) {
        mContext = context;
    }

    public List<EventData> getEvents(long beginMillis, long endMillis) {

        List<EventData> allEvents = new ArrayList<>();

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
        Cursor cursor;
        //cursor get needed data
        cursor = Instances.query(mContext.getContentResolver(), instanceQuery, beginMillis, endMillis);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    //save id of every event this day
                    EventData singleEvent = new EventData();
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

        return allEvents;
    }

    public static long getBeginInMillis (Calendar calendar) {
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        beginCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 1);
        return beginCalendar.getTimeInMillis();
    }

    public static long getEndInMillis(Calendar calendar) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        endCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59);
        return endCalendar.getTimeInMillis();
    }


//    public List<CalendarData> getAllCalendars() {
//        //list of calendars to return
//        List<CalendarData> allCalendars = new ArrayList<>();
//        //list of columns witch we want from provider
//        String[] query =
//                new String[]{
//                        Calendars._ID,
//                        Calendars.NAME,
//                        Calendars.ACCOUNT_NAME,
//                        Calendars.ACCOUNT_TYPE};
//        Cursor cursor;
//
//        //get data from provider
//        cursor =
//                mContext.getContentResolver().
//                        query(Calendars.CONTENT_URI,
//                                query,
//                                Calendars.VISIBLE + " = 1",
//                                null,
//                                Calendars._ID + " ASC");
//        if (cursor != null) {
//            //read cursor
//            if (cursor.moveToFirst()) {
//                do {
//                    CalendarData singleCalendar = new CalendarData();
//                    singleCalendar.setId(cursor.getLong(0));
//                    singleCalendar.setDisplayName(cursor.getString(1));
//                    singleCalendar.setAccountName(cursor.getString(2));
//                    singleCalendar.setAccountType(cursor.getString(3));
//                    allCalendars.add(singleCalendar);
//                } while (cursor.moveToNext());
//            }
//            //always close cursor!!
//            cursor.close();
//        }
//        return allCalendars;
//
//    }

}
