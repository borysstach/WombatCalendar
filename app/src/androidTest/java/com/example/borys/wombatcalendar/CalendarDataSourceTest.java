package com.example.borys.wombatcalendar;

import android.test.AndroidTestCase;

import com.example.borys.wombatcalendar.data.CalendarDataSource;

public class CalendarDataSourceTest extends AndroidTestCase{
    public void readCalendar(){
        CalendarDataSource reader = new CalendarDataSource(getContext());
        assertNotNull(reader.getAllCalendars());
    }

//    public void readEvents(){
//        CalendarDataSource readerEvents = new CalendarDataSource(getContext());
//        Calendar testCalendarStart = Calendar.getInstance();
//        testCalendarStart.set(testCalendarStart.get(Calendar.YEAR), testCalendarStart.get(Calendar.MONTH), testCalendarStart.get(Calendar.DAY_OF_MONTH), 0, 0);
//        Calendar testCalendarStop = Calendar.getInstance();
//        testCalendarStart.set(testCalendarStart.get(Calendar.YEAR), testCalendarStart.get(Calendar.MONTH), testCalendarStart.get(Calendar.DAY_OF_MONTH), 24, 0);
//        assertNotNull(readerEvents.getEventsFromDay(testCalendarStart, testCalendarStop));
//    }
}
