package com.example.borys.wombatcalendar.data;


public class EventData {
    private long mId;
    private String mTitle;
    private long mBegin;
    private long mEnd;
    private boolean mAllDay;


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

}