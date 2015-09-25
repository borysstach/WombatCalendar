package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
    private int mCurrentYear;
    private RecyclerView mWeekRecyclerView;
    private List<String> mDaysStrings;
    private List<Integer> mDaysCalendar;
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
        mCalendar = Calendar.getInstance();
        setData();
        mDaysStrings = new ArrayList<>(Arrays.asList("poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota", "niedziela"));
        mDaysCalendar = new ArrayList<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY));
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

        public DayOfWeekViewHolder(View itemView) {
            super(itemView);
            //find view
            mDayName = (TextView) itemView.findViewById(R.id.day_name_text_view);
        }

        public void bindDay(String dayName) {
            //bind data to view
            mDayName.setText(dayName);
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
            holder.bindDay(dayName + "  " + day + "  " + mCalendar.get(Calendar.YEAR) + "  " + mCalendar.get(Calendar.WEEK_OF_YEAR));

        }

        @Override
        public int getItemCount() {
            return mDaysStrings.size();
        }
    }

    /**
     * Method set data to current week
     */
    public void setData() {
        //set calendar to current showing week
        mCurrentWeek = MainActivity.thisWeek + (mNumberOfFragment - MainActivity.MAX_PAGE / 2);
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalendar.add(Calendar.WEEK_OF_YEAR, (mCurrentWeek - MainActivity.thisWeek));
    }
}



