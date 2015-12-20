package com.stach.borys.wombatcalendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment {

    private Integer numberOfDays;
    private Calendar mMonth;
    private List<String> mMonthStrings;

    static MonthFragment newInstance(int num) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Integer numberOfFragment = getArguments().getInt("num");
        mMonth = Calendar.getInstance();
        mMonth.set(Calendar.DAY_OF_MONTH, 1);
        mMonth.add(Calendar.MONTH, numberOfFragment);
        numberOfDays = mMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
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

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_fragment, container, false);
        RecyclerView monthRecyclerView = (RecyclerView) rootView.findViewById(R.id.month_recycler_view);
        monthRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        monthRecyclerView.setAdapter(new MonthRecyclerAdapter());
        TextView monthTitle = (TextView) rootView.findViewById(R.id.month_title);
        TextView yearTitle = (TextView) rootView.findViewById(R.id.month_year_title);
        monthTitle.setText(mMonthStrings.get(mMonth.get(Calendar.MONTH)));
        yearTitle.setText(""+mMonth.get(Calendar.YEAR));
        return rootView;
    }

    //////////////////////////////          VIEW HOLDER

    public class DayOfMonthViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayNumber;
        private FrameLayout mFrameLayout;

        public DayOfMonthViewHolder(View itemView) {
            super(itemView);
            mDayNumber = (TextView) itemView.findViewById(R.id.month_day_number);
            mFrameLayout = (FrameLayout) itemView.findViewById(R.id.month_day_frame);
        }

        public void bindDay(Integer day) {
            mDayNumber.setText("" + (day));
            mFrameLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.day_stroke_back));
        }

    }

    ////////////////////////////             ADAPTER

    public class MonthRecyclerAdapter extends RecyclerView.Adapter<DayOfMonthViewHolder> {

        private Integer day = 1;

        @Override
        public DayOfMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.month_day_view, parent, false);
            return new DayOfMonthViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayOfMonthViewHolder holder, int position) {
            if(!(position < getDayOfWeekFromFirstDayOfMonth(mMonth))) {
                holder.bindDay(day);
                day++;
            }
        }

        @Override
        public int getItemCount() {
            return numberOfDays + getDayOfWeekFromFirstDayOfMonth(mMonth);
        }
    }
private Integer getDayOfWeekFromFirstDayOfMonth(Calendar firstDayOfMonth) {
    switch (firstDayOfMonth.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.MONDAY:
            return 0;
        case Calendar.TUESDAY:
            return 1;
        case Calendar.WEDNESDAY:
            return 2;
        case Calendar.THURSDAY:
            return 3;
        case Calendar.FRIDAY:
            return 4;
        case Calendar.SATURDAY:
            return 5;
        case Calendar.SUNDAY:
            return 6;
        default:
            return 0;
    }
}
}
