package com.stach.borys.wombatcalendar.data;


import java.util.ArrayList;
import java.util.List;

public class DayData {

    private List<EventData> mSortedEvents;
    public String morning = "Spanko do ";
    public String evening = "Czas zawinąć się w kocyk i iść spać";
    public String afternoon =" przerwy, czas na ciastko!";
    public String freeDay = "Cały dzień wolny!";

    public DayData() {
        mSortedEvents = new ArrayList<>();
        mSortedEvents.add(EventData.standardEventData(freeDay));
    }

    public DayData(List<EventData> events){
        this();
        for(EventData eventData : events){
            this.add(eventData);
        }
    }

    public List<EventData> getSortedEvents() {
        return mSortedEvents;
    }

    private String addZero (Integer min){
        if(min < 10){
            return "0"+ min;
        }
        return ""+min;
    }

    public void add(EventData event) {
        if (event.isAllDay()) {
            if (mSortedEvents.get(0).getTitle().equals(freeDay)) {
                mSortedEvents.remove(0);
            }
            mSortedEvents.add(0, event);
        } else {
            for (int i = 0; i < mSortedEvents.size(); i++) {
                if (i == (mSortedEvents.size() - 1)) {
                    if (mSortedEvents.get(i).isAllDay() || mSortedEvents.get(i).getTitle().equals(freeDay)) {
                        if (mSortedEvents.get(i).getTitle().equals(freeDay)) {
                            mSortedEvents.remove(i);
                        }
                        EventData morningEvent = EventData.standardEventData(morning);
                        String title;
                        if (event.getStartingHour() > 12){
                            title = morning + "późna :D";
                        }else {
                            title = morning + event.getStartingHour() + ":" + addZero(event.getStartingMinutes()) + "!";
                        }
                        morningEvent.setTitle(title);
                        mSortedEvents.add(morningEvent);
                        mSortedEvents.add(event);
                        mSortedEvents.add(EventData.standardEventData(evening));
                        break;
                    } else if (mSortedEvents.get(i).isStandard()) {
                        mSortedEvents.add(i, event);
                        if(!mSortedEvents.get(i-1).isAllDay() && !mSortedEvents.get(i-1).isStandard()){
                            if((mSortedEvents.get(i).getBegin() - mSortedEvents.get(i-1).getEnd()) > 0){
                                Integer hour = mSortedEvents.get(i).getStartingHour() - mSortedEvents.get(i-1).getEndingHour();
                                Integer min = mSortedEvents.get(i).getStartingMinutes() - mSortedEvents.get(i-1).getEndingMinutes();
                                if( min < 0){
                                    if(hour > 0) {hour -= 1;}
                                    min += 60;
                                }
                                EventData morningEvent = EventData.standardEventData(afternoon);
                                morningEvent.setTitle(hour + "." + min + "h" + afternoon);
                                mSortedEvents.add(i, morningEvent);
                            }
                        }
                        break;
                    }
                }
        }
    }
}
}
