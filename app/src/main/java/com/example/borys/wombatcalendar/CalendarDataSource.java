package com.example.borys.wombatcalendar;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.provider.CalendarContract.Events;

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

    public List<CalendarData> getAllCalendars() {
        //list of calendars to return
        List<CalendarData> allCalendars = new ArrayList<>();
        //list of columns witch we want from provider
        String[] query =
                new String[]{
                        Calendars._ID,
                        Calendars.NAME,
                        Calendars.ACCOUNT_NAME,
                        Calendars.ACCOUNT_TYPE};
        //get data from provider
        Cursor cursor =
                mContext.getContentResolver().
                        query(Calendars.CONTENT_URI,
                                query,
                                Calendars.VISIBLE + " = 1",
                                null,
                                Calendars._ID + " ASC");
        //read cursor
        if (cursor.moveToFirst()) {
            do {
                CalendarData singleCalendar = new CalendarData();
                singleCalendar.setId(cursor.getLong(0));
                singleCalendar.setDisplayName(cursor.getString(1));
                singleCalendar.setAccountName(cursor.getString(2));
                singleCalendar.setAccountType(cursor.getString(3));
                allCalendars.add(singleCalendar);
            } while (cursor.moveToNext());
        }
        //always close cursor!!
        cursor.close();
        return allCalendars;
    }

    public List<EventData> getEventsFromDay(Calendar beginTime, Calendar endTime) {
        //convert Callendar data to millis
        long beginMillis = beginTime.getTimeInMillis();
        long endMillis = endTime.getTimeInMillis();
        //list of all events this day
        List<EventData> allEvents = new ArrayList<>();

        //searching for Events id this day from Instances table
        //what we want from data
        String[] instanceQuery =
                new String[]{
                        Instances._ID,
                        Instances.BEGIN,
                        Instances.END,
                        Instances.EVENT_ID};

        //cursor get needed data
        Cursor cursor =
                Instances.query(mContext.getContentResolver(), instanceQuery, beginMillis, endMillis);

        if (cursor.moveToFirst()) {
            do {
                //save id of every event this day
                EventData singleEvent = new EventData();
                singleEvent.setId(cursor.getLong(3));
                allEvents.add(singleEvent);
            } while (cursor.moveToNext());
        }
        //always close cursor!!
        cursor.close();

        /// now searching for details of events from Events table
        //what we want from data
        String[] eventQuery =
                new String[]{
                        Events._ID,
                        Events.TITLE,
                        Events.EVENT_COLOR,
                };

        //Convert IDs this day to String table
        List<String> eventIds = new ArrayList<>();

        for (int i = 0; i < allEvents.size(); i++) {
            eventIds.add(Long.toString(allEvents.get(i).getId()));
        }

        String[] eventStringIds = new String[eventIds.size()];
        eventStringIds = eventIds.toArray(eventStringIds);

        //cursor get needed data
        Cursor eventCursor =
                mContext.getContentResolver().
                        query(
                                Events.CONTENT_URI,
                                eventQuery,
                                Events._ID + " = ? ",
                                eventStringIds,
                                null);
        //convert data from cursor
        if (eventCursor.moveToFirst()) {
            do {

                allEvents.get(eventCursor.getPosition()).setTitle(eventCursor.getString(1));
                allEvents.get(eventCursor.getPosition()).setColor(eventCursor.getString(2));
            } while (eventCursor.moveToNext());
        }
        //always close cursor!!
        eventCursor.close();

        return allEvents;

    }
}
