package com.stach.borys.wombatcalendar.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SortEvent {

    private static final String MORNING = "Spanko do ";
    private static final String EVENING = "Czas zawinąć się w kocyk i iść spać";
    private static final String AFTERNOON =" przerwy, czas na ciastko!";
    private static final String FREE_DAY = "Cały dzień wolny!";

    List<Event> mSortedEvents = new ArrayList<>();

    public List<Event> sortEvents(List<Event> unsortedEvents, Calendar fromDay){
        mSortedEvents.add(Event.standardEventData(FREE_DAY));
        for(Event event : unsortedEvents){
            if(!thisDay(event, fromDay))
                continue;
            if (event.isAllDay()) {
                removeFreeDayEvent();
                //put it on top
                mSortedEvents.add(0, event);
            } else {
                //is specific time event
                findPlaceForEvent(event);
            }
        }
        return mSortedEvents;
    }

    private void findPlaceForEvent(Event insertedEvent) {
        if (isFirstSpecificTimeEventToday(getLastEventNumber())) {
            removeFreeDayEvent();
            mSortedEvents.add(getMorningEvent(insertedEvent));
            mSortedEvents.add(insertedEvent);
            mSortedEvents.add(Event.standardEventData(EVENING));

        } else if (isStandard(getLastEventNumber())) {
            mSortedEvents.add(getLastEventNumber(), insertedEvent);
            if(previousIsSpecificTime(getLastEventNumber()) && isFreeTimeBetween(getLastEventNumber())){
                addStandardEventBetween(getLastEventNumber());
            }
        }
    }

    private int getLastEventNumber() {
        return mSortedEvents.size()-1;
    }


    private void addStandardEventBetween(int i) {
        Integer hour = mSortedEvents.get(i).getStartingHour() - mSortedEvents.get(i-1).getEndingHour();
        Integer min = mSortedEvents.get(i).getStartingMinutes() - mSortedEvents.get(i-1).getEndingMinutes();
        if( min < 0){
            if(hour > 0) {hour -= 1;}
            min += 60;
        }
        Event morningEvent = Event.standardEventData(AFTERNOON);
        morningEvent.setTitle(hour + "." + min + "h" + AFTERNOON);
        mSortedEvents.add(i, morningEvent);

    }

    private boolean isFreeTimeBetween(int i) {
        return (mSortedEvents.get(i).getBegin() - mSortedEvents.get(i-1).getEnd()) > 0;
    }

    private boolean previousIsSpecificTime(int i) {
        return !mSortedEvents.get(i-1).isAllDay() && !isStandard(i-1);
    }

    private boolean isStandard(int i) {
        return mSortedEvents.get(i).isStandard();
    }

    @NonNull
    private Event getMorningEvent(Event event) {
        Event morningEvent = Event.standardEventData(MORNING);
        String title = getMorningTitle(event);
        morningEvent.setTitle(title);
        return morningEvent;
    }

    @NonNull
    private String getMorningTitle(Event event) {
        if (event.getStartingHour() > 12){
            return  MORNING + "późna :D";
        }else {
            return MORNING + event.getStartingHour() + ":" + addZero(event.getStartingMinutes()) + "!";
        }
    }

    private boolean isFirstSpecificTimeEventToday(int i) {
        Event event = mSortedEvents.get(i);
        return event.isAllDay() || event.getTitle().equals(FREE_DAY);
    }

    private void removeFreeDayEvent() {
        if (mSortedEvents.get(0).getTitle().equals(FREE_DAY)) {
            mSortedEvents.remove(0);
        }
    }

    private String addZero (Integer min){
        if(min < 10){
            return "0"+ min;
        }
        return ""+min;
    }

    private boolean thisDay(Event event, Calendar thisDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getBegin());;
        return calendar.get(Calendar.DAY_OF_MONTH) == thisDay.get(Calendar.DAY_OF_MONTH) ;
    }
}
