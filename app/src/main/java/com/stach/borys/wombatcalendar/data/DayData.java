package com.stach.borys.wombatcalendar.data;


import java.util.ArrayList;
import java.util.List;

public class DayData {

    private List<EventData> mEvents;
    public String morning = "Spanko do ";
    public String evening = "Czas zawinąć się w kocyk i iść spać";
    public String afternoon =" przerwy, czas na ciastko!";
    public String freeDay = "Cały dzień wolny!";

    public DayData() {
        mEvents = new ArrayList<>();
        mEvents.add(EventData.standardEventData(freeDay));
    }

    public DayData(List<EventData> events){
        this();
        for(EventData eventData : events){
            this.add(eventData);
        }
    }

    public List<EventData> getEvents() {
        return mEvents;
    }

    public void add(EventData event) {
        if (event.isAllDay()) {
            if (mEvents.get(0).getTitle().equals(freeDay)) {
                mEvents.remove(0);
            }
            mEvents.add(0, event);
        } else {
            for (int i = 0; i < mEvents.size(); i++) {
                if (i == (mEvents.size() - 1)) {
                    if (mEvents.get(i).isAllDay() || mEvents.get(i).getTitle().equals(freeDay)) {
                        if (mEvents.get(i).getTitle().equals(freeDay)) {
                            mEvents.remove(i);
                        }
                        EventData morningEvent = EventData.standardEventData(morning);
                        String title;
                        if (event.getStartingHour() > 12){
                            title = morning + "do późna :D";
                        }else title = morning + event.getStartingHour() + ":" + event.getStartingMinutes() + "!";
                        morningEvent.setTitle(title);
                        mEvents.add(morningEvent);
                        mEvents.add(event);
                        mEvents.add(EventData.standardEventData(evening));
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
