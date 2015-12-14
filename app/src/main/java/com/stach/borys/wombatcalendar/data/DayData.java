package com.stach.borys.wombatcalendar.data;


import java.util.ArrayList;
import java.util.List;

public class DayData {

    private List<EventData> mEvents;

    public DayData(){
        mEvents = new ArrayList<>();
    }

    public List<EventData> getEvents() {
        return mEvents;
    }

    public void add(EventData event) {
        mEvents.add(event);
    }
}
