package com.example.borys.wombatcalendar.data;


import java.util.Calendar;
import java.util.List;

public class WeekData {
    private DayData mMonday;
    private DayData mTuesday;
    private DayData mWednesday;
    private DayData mThursday;
    private DayData mFriday;
    private DayData mSaturday;
    private DayData mSunday;

    public WeekData (List<EventData> events){
        Calendar tmpCalendar = Calendar.getInstance();
        for(EventData event : events){
            tmpCalendar.setTimeInMillis(event.getBegin());
            switch (tmpCalendar.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY:
                    if(mMonday == null) mMonday = new DayData();
                    mMonday.add(event);
                    break;
                case Calendar.TUESDAY:
                    if(mTuesday == null) mTuesday = new DayData();
                    mTuesday.add(event);
                    break;
                case Calendar.WEDNESDAY:
                    if(mWednesday == null) mWednesday = new DayData();
                    mWednesday.add(event);
                    break;
                case Calendar.THURSDAY:
                    if(mThursday == null) mThursday = new DayData();
                    mThursday.add(event);
                    break;
                case Calendar.FRIDAY:
                    if(mFriday == null) mFriday = new DayData();
                    mFriday.add(event);
                    break;
                case Calendar.SATURDAY:
                    if(mSaturday == null) mSaturday = new DayData();
                    mSaturday.add(event);
                    break;
                case Calendar.SUNDAY:
                    if(mSunday == null) mSunday = new DayData();
                    mSunday.add(event);
                    break;
            }
        }
        tmpCalendar = null;
    }
    public DayData get(int day){
        switch (day){
            case Calendar.MONDAY:
                return mMonday;
            case Calendar.TUESDAY:
                return mTuesday;
            case Calendar.WEDNESDAY:
                return mWednesday;
            case Calendar.THURSDAY:
                return mThursday;
            case Calendar.FRIDAY:
                return mFriday;
            case Calendar.SATURDAY:
                return mSaturday;
            case Calendar.SUNDAY:
                return mWednesday;
            default:
                return null;
        }
    }

    public DayData getMonday() {
        return mMonday;
    }

    public DayData getTuesday() {
        return mTuesday;
    }

    public DayData getWednesday() {
        return mWednesday;
    }

    public DayData getThursday() {
        return mThursday;
    }

    public DayData getFriday() {
        return mFriday;
    }

    public DayData getSaturday() {
        return mSaturday;
    }

    public DayData getSunday() {
        return mSunday;
    }
}
