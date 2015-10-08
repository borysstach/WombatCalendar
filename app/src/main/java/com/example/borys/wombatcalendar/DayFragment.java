package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.borys.wombatcalendar.data.CalendarDataSource;
import com.example.borys.wombatcalendar.data.EventData;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DayFragment extends Fragment{

    private Calendar mCalendar = Calendar.getInstance();
    private List<EventData> mEvents;

    static DayFragment newInstance (int year, int month, int day, int position){
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
        //set adapter with all events this day
        mEvents = readerEvents.getEventsFromDay(mCalendar);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //find rootView with RecyclerView
        View rootView = inflater.inflate(R.layout.fragment_week, container, false);
        //create Grid Recycler View
       RecyclerView mDayRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDayRecyclerView.setAdapter(new SingleDayRecyclerAdapter());

        return rootView;
    }

    public class SingleDayViewHolder extends RecyclerView.ViewHolder {

        private TextView mEventTitle;


        public SingleDayViewHolder(View itemView) {
            super(itemView);
            //find view
            mEventTitle = (TextView) itemView.findViewById(R.id.single_day_textview);
        }


        public void bindEvent(EventData event) {
            mEventTitle.setText(event.getTitle());
        }
    }

    public class SingleDayRecyclerAdapter extends RecyclerView.Adapter<SingleDayViewHolder> {


        @Override
        public SingleDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            //inflate layout to ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.single_day, parent, false);

            //return new ViewHolder with layout
            return new SingleDayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SingleDayViewHolder holder, int position) {
            //bind data
            if (!mEvents.isEmpty()) {
                EventData event = mEvents.get(position);
                holder.bindEvent(event);
            }
        }


        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }



}
