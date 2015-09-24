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

    private RecyclerView mWeekRecyclerView;
    private List<String> mDays;
    private Calendar mCalendar;
    private int num;

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
        mCalendar = Calendar.getInstance();
        mCalendar.setMinimalDaysInFirstWeek(1);
        mDays= new ArrayList<>(Arrays.asList("poniedziałek","wtorek","środa","czwartek","piątek","sobota","niedziela"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //find rootView with RecyclerView
        View rootView =inflater.inflate(R.layout.fragment_week, container, false);
        //get data from Pager
        num = getArguments().getInt("num");
        //create Grid Recycler View
        mWeekRecyclerView = (RecyclerView)rootView.findViewById(R.id.week_recycler_view);
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mWeekRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());

        return rootView;
    }

    public class DayOfWeekViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayName;

        public DayOfWeekViewHolder(View itemView) {
            super(itemView);
            //find view
            mDayName = (TextView)itemView.findViewById(R.id.day_name_text_view);
        }

        public void bindDay (String dayName){
            //bind data to view
            mDayName.setText(dayName);
        }
    }


    public class DayOfWeekRecyclerAdapter extends RecyclerView.Adapter<DayOfWeekViewHolder>{

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
            //int day = mCalendar.get(Calendar.DAY_OF_MONTH) + position;
            String dayName = mDays.get(position);
            holder.bindDay(dayName + num);
        }

        @Override
        public int getItemCount() {
            return mDays.size();
        }
    }
}


