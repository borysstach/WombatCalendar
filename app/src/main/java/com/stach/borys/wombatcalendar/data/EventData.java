package com.stach.borys.wombatcalendar.data;


import java.util.Calendar;

public class EventData {
    private long mId;
    private String mTitle;
    private long mBegin;
    private long mEnd;
    private boolean mAllDay;
    private boolean mStandard;

    public EventData(){
        mStandard = false;
    }

    public static EventData standardEventData(String title){
         EventData event = new EventData();
         event.setTitle(title);
        event.setAllDay(0);
        event.setStandard(true);
        return event;
    }


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getBegin() {
        return mBegin;
    }

    public void setBegin(long begin) {
        mBegin = begin;
    }

    public long getEnd() {
        return mEnd;
    }

    public void setEnd(long end) {
        mEnd = end;
    }

    public boolean isAllDay() {
        return mAllDay;
    }

    public void setAllDay(int allDay) {
        mAllDay = (allDay == 1);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isStandard() {
        return mStandard;
    }

    private void setStandard(boolean standard) {
        mStandard = standard;
    }

    public Integer getStartingHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mBegin);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public Integer getStartingMinutes(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mBegin);
        return calendar.get(Calendar.MINUTE);
    }

    public Integer getEndingHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mEnd);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public Integer getEndingMinutes(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mEnd);
        return calendar.get(Calendar.MINUTE);
    }
}