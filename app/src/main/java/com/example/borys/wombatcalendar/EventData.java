package com.example.borys.wombatcalendar;


public class EventData {
    private long mId;
    private long mCalendarId;
    private String mTitle;
    private String mColor;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getCalendarId() {
        return mCalendarId;
    }

    public void setCalendarId(long calendarId) {
        mCalendarId = calendarId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
