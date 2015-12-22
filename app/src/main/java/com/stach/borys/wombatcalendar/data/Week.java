package com.stach.borys.wombatcalendar.data;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Week {
    private List<Event> mMonday;
    private List<Event> mTuesday;
    private List<Event> mWednesday;
    private List<Event> mThursday;
    private List<Event> mFriday;
    private List<Event> mSaturday;
    private List<Event> mSunday;

    public Week(List<Event> events){
        Calendar tmpCalendar = Calendar.getInstance();
        for(Event event : events){
            tmpCalendar.setTimeInMillis(event.getBegin());
            switch (tmpCalendar.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY:
                    if(mMonday == null) mMonday = new ArrayList<>();
                    mMonday.add(event);
                    break;
                case Calendar.TUESDAY:
                    if(mTuesday == null) mTuesday = new ArrayList<>();
                    mTuesday.add(event);
                    break;
                case Calendar.WEDNESDAY:
                    if(mWednesday == null) mWednesday =new  ArrayList<>();
                    mWednesday.add(event);
                    break;
                case Calendar.THURSDAY:
                    if(mThursday == null) mThursday = new ArrayList<>();
                    mThursday.add(event);
                    break;
                case Calendar.FRIDAY:
                    if(mFriday == null) mFriday = new ArrayList<>();
                    mFriday.add(event);
                    break;
                case Calendar.SATURDAY:
                    if(mSaturday == null) mSaturday = new ArrayList<>();
                    mSaturday.add(event);
                    break;
                case Calendar.SUNDAY:
                    if(mSunday == null) mSunday = new ArrayList<>();
                    mSunday.add(event);
                    break;
            }
        }
        tmpCalendar = null;
    }
    public List<Event> get(int day){
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
                return mSunday;
            default:
                return null;
        }
    }

}
