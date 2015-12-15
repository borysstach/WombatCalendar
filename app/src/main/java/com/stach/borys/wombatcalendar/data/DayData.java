package com.stach.borys.wombatcalendar.data;


import java.util.ArrayList;
import java.util.List;

public class DayData {

    private List<EventData> mEvents;

    public DayData() {
        mEvents = new ArrayList<>();
        mEvents.add(StandardEvents.freeDay);
    }

    public List<EventData> getEvents() {
        return mEvents;
    }

    public void add(EventData event) {
        if (event.isAllDay()) {
            if (mEvents.get(0) == StandardEvents.freeDay) {
                mEvents.remove(0);
            }
            mEvents.add(0, event);
        } else {
            for (int i = 0; i < mEvents.size(); i++) {
                if (i == (mEvents.size() - 1)) {
                    if (mEvents.get(i).isAllDay() || mEvents.get(i) == StandardEvents.freeDay) {
                        if (mEvents.get(i) == StandardEvents.freeDay) {
                            mEvents.remove(0);
                        }
                        EventData morningEvent = StandardEvents.morning;
                        morningEvent.setTitle(morningEvent.getTitle() + event.getStartingHour() + ":" + event.getStartingMinutes() + "!");
                        mEvents.add(morningEvent);
                        mEvents.add(event);
                        mEvents.add(StandardEvents.evening);
                        break;
                    } else if (mEvents.get(i).isStandard()) {
                        mEvents.add(i, event);
                        break;
                    }
                } else if (mEvents.get(i).isAllDay()) {
                    continue;
                }else if (mEvents.get(i).getBegin() > event.getBegin()){
                    mEvents.add(i, event);
            }
        }

    }
}

}
