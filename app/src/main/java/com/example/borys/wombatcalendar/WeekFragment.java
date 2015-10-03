package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WeekFragment extends Fragment {

    private int mCurrentWeek;
    private RecyclerView mWeekRecyclerView;
    private List<String> mDaysStrings;
    private List<String> mMonthStrings;
    private Calendar mCalendar;
    private int mNumberOfFragment;

    static WeekFragment newInstance(int num) {
        WeekFragment f = new WeekFragment();

        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
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
        View rootView = inflater.inflate(R.layout.fragment_week, container, false);
        //create Grid Recycler View
        mWeekRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mWeekRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());

        return rootView;
    }

    public class DayOfWeekViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayName;
        private RecyclerView mDayRecyclerView;

        public DayOfWeekViewHolder(View itemView) {
            super(itemView);
            //find view
            mDayName = (TextView) itemView.findViewById(R.id.day_name_text_view);
            mDayRecyclerView = (RecyclerView) itemView.findViewById(R.id.single_day_recyclerview);
            mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        public void bindDay(String dayName) {
            //bind data to view
            mDayName.setText(dayName);
        }


        public void setAdapter( Calendar mCalendar) {

            CalendarDataSource readerEvents = new CalendarDataSource(getContext());
            Calendar testCalendarStart = Calendar.getInstance();
            testCalendarStart.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), 0, 1);
            Calendar testCalendarStop = Calendar.getInstance();
            testCalendarStop.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), 23, 59);
            //set adapter with all events this day
            mDayRecyclerView.setAdapter(new SingleDayRecyclerAdapter( readerEvents.getEventsFromDay(testCalendarStart, testCalendarStop)));
        }

    }


    public class DayOfWeekRecyclerAdapter extends RecyclerView.Adapter<DayOfWeekViewHolder> {

        @Override
        public DayOfWeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate layout to ViewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.day_of_week, parent, false);
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


            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            String dayName = mDaysStrings.get(position);
            holder.bindDay(dayName + "  " + day);
            holder.setAdapter(mCalendar);

        }

        @Override
        public int getItemCount() {
            return mDaysStrings.size();
        }
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
        private List<EventData> mEvents;

        public SingleDayRecyclerAdapter(List<EventData> events){
            mEvents = events;
        }

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


    /**
     * Method set data to current week
     */
    public void setData() {
        //set calendar to current showing week
        mCalendar = Calendar.getInstance();
        mCurrentWeek = MainActivity.thisWeek + (mNumberOfFragment - MainActivity.MAX_PAGE / 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalendar.add(Calendar.WEEK_OF_YEAR, (mCurrentWeek - MainActivity.thisWeek));
        ((MainActivity)getActivity()).setActionBarTitle(mMonthStrings.get(mCalendar.get(Calendar.MONTH)));
    }
}



