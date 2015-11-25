package com.example.borys.wombatcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.borys.wombatcalendar.data.CalendarDataSource;
import com.example.borys.wombatcalendar.data.EventData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WeekFragment extends Fragment {

    private List<String> mDaysStrings;
    private List<String> mMonthStrings;
    private Calendar mCalendar;
    private int mNumberOfFragment;

    static WeekFragment newInstance(int num) {
        WeekFragment fragment = new WeekFragment();

        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get data from Pager
        mNumberOfFragment = getArguments().getInt("num");
        mDaysStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday),
                getString(R.string.sunday)));

        mMonthStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.january),
                getString(R.string.february),
                getString(R.string.march),
                getString(R.string.april),
                getString(R.string.may),
                getString(R.string.june),
                getString(R.string.julay),
                getString(R.string.august),
                getString(R.string.september),
                getString(R.string.october),
                getString(R.string.november),
                getString(R.string.december)));
        setData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //find rootView with RecyclerView
        View rootView = inflater.inflate(R.layout.week_view_fragment, container, false);
        //create Grid Recycler View for whole week
        RecyclerView mWeekRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mWeekRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());

        return rootView;
    }

    ///////////////////////////////////////////////////  RecyclerView for Week Grid  ////////////////////////////////

    public class DayOfWeekViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayName;
        private RecyclerView mDayRecyclerView;
        private Calendar cloneCalendar;

        public DayOfWeekViewHolder(View itemView) {
            super(itemView);
            //find view
            mDayName = (TextView) itemView.findViewById(R.id.day_name_text_view);
            TextView newActivityButton = (TextView) itemView.findViewById(R.id.day_button);
            newActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DayActivity.class);
                    intent.putExtra("year", cloneCalendar.get(Calendar.YEAR));
                    intent.putExtra("month", cloneCalendar.get(Calendar.MONTH));
                    intent.putExtra("day", cloneCalendar.get(Calendar.DAY_OF_MONTH));
                    startActivity(intent);
                }
            });
            //find recycler view for Day Grid Item
            mDayRecyclerView = (RecyclerView) itemView.findViewById(R.id.single_day_recyclerview);
            mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mDayRecyclerView.setAdapter(new SingleDayRecyclerAdapter(new ArrayList<EventData>()));
        }

        public void bindDay(String dayName) {
            //bind data to view
            mDayName.setText(dayName);
        }

        /**
         * Set adapter on RecyclerView for Day Grid Item
         */
        public void setAdapter() {
            //get current calendar data to start activty intent
            cloneCalendar = (Calendar) mCalendar.clone();
            //new thread to get data from content provider
            new Thread(new Runnable() {
                public void run() {
                    //getting data
                    CalendarDataSource readerEvents = new CalendarDataSource(getContext());
                    final List<EventData> events = readerEvents.getEventsFromDay(cloneCalendar);

                    //back to UI thread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //set adapter with all events this day
                            mDayRecyclerView.setAdapter(new SingleDayRecyclerAdapter(events));
                        }
                    });
                }
            }).start();
        }

    }


    public class DayOfWeekRecyclerAdapter extends RecyclerView.Adapter<DayOfWeekViewHolder> {

        @Override
        public DayOfWeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate layout to ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.week_view_day, parent, false);

            //return new ViewHolder with layout
            return new DayOfWeekViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayOfWeekViewHolder holder, int position) {
            //bind data from mDays list
            if (position != 0){
                //add one day in next day after monday
            mCalendar.add(Calendar.DAY_OF_WEEK, 1);
            }
            //set number of day for this day of week
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            String dayName = mDaysStrings.get(position);
            holder.bindDay(dayName + "  " + day);
            //make recyclerView with list of this day events
            holder.setAdapter();

        }

        @Override
        public int getItemCount() {
            return mDaysStrings.size();
        }
    }



    //////////////////////////////////////////   RecyclerView inside Day Grid Item  ///////////////////////////////////////


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
        private List<EventData> mEvents;

        public SingleDayRecyclerAdapter(List<EventData> events){
            mEvents = events;
        }

        @Override
        public SingleDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            //inflate layout to ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.week_view_single_event, parent, false);

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


    /**
     * Method set data to current week
     */
    public void setData() {
        //set calendar to current showing week
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalendar.add(Calendar.WEEK_OF_YEAR, (mNumberOfFragment));
        ((WeekActivity)getActivity()).setActionBarTitle(mMonthStrings.get(mCalendar.get(Calendar.MONTH)));
    }
}



