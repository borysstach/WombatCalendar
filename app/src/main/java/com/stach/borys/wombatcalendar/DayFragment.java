package com.stach.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stach.borys.wombatcalendar.data.CalendarDataSource;
import com.stach.borys.wombatcalendar.data.EventData;

import java.util.Calendar;
import java.util.List;

public class DayFragment extends Fragment {

    private Calendar mCalendar = Calendar.getInstance();
    private List<EventData> mEvents;

    static DayFragment newInstance(int year, int month, int day, int position) {
        DayFragment fragment = new DayFragment();

        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCalendar.set(getArguments().getInt("year"), getArguments().getInt("month"), getArguments().getInt("day"));
        mCalendar.add(Calendar.DAY_OF_MONTH, getArguments().getInt("position"));
        CalendarDataSource readerEvents = new CalendarDataSource(getContext());
        long begin = CalendarDataSource.getBeginInMillis(mCalendar);
        long end = CalendarDataSource.getEndInMillis(mCalendar);
        mEvents = readerEvents.getEvents(begin, end);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.day_view, container, false);
        RecyclerView mDayRecyclerView = (RecyclerView) rootView.findViewById(R.id.day_events_recycler);
        mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDayRecyclerView.setAdapter(new SingleDayRecyclerAdapter());

        return rootView;
    }

    ////////////////////////////             VIEW HOLDER



    ////////////////////////////             ADAPTER

    public class SingleDayRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

        class DayViewHolder extends CustomViewHolder{

            private TextView mEventTitle;
            private TextView mEventStartHour;
            private TextView mEventStartMinute;
            private TextView mEventEndHour;
            private TextView mEventEndMinute;

            public DayViewHolder(View itemView) {
                super(itemView);
                mEventTitle = (TextView) itemView.findViewById(R.id.specific_time_event_name);
                mEventStartHour = (TextView) itemView.findViewById(R.id.starting_hour);
                mEventStartMinute = (TextView) itemView.findViewById(R.id.starting_minute);
                mEventEndHour = (TextView) itemView.findViewById(R.id.ending_hour);
                mEventEndMinute = (TextView) itemView.findViewById(R.id.ending_minute);
            }
        public void bindEvent(EventData event) {
            mEventTitle.setText(event.getTitle());
            mEventStartHour.setText(event.getStartingHour());
            mEventStartMinute.setText(event.getStartingMinutes());
            mEventEndHour.setText(event.getEndingHour());
            mEventEndMinute.setText(event.getEndingMinutes());
        }
    }

        class AllDayViewHolder extends CustomViewHolder {

            private TextView mEventTitle;

            public AllDayViewHolder(View itemView) {
                super(itemView);
                mEventTitle = (TextView) itemView.findViewById(R.id.all_day_time_event_name);
            }
            public void bindEvent(EventData event) {
                mEventTitle.setText(event.getTitle());
            }

        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch(viewType){
                case 1:
                    View view_all = layoutInflater.inflate(R.layout.day_allday_event_layout, parent, false);
                    return new AllDayViewHolder(view_all);
                default:
                    View view = layoutInflater.inflate(R.layout.day_event_layout, parent, false);
                    return new DayViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {

            if (!mEvents.isEmpty()) {
                EventData event = mEvents.get(position);
                holder.bindEvent(event);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(mEvents.get(position).isAllDay()){
                return 1;
            }else return 0;
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{
        public CustomViewHolder(View itemView){
            super(itemView);
        }
        public void bindEvent(EventData event){

        }
    }



}
