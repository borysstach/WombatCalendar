package com.example.borys.wombatcalendar;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.borys.wombatcalendar.data.CalendarDataSource;
import com.example.borys.wombatcalendar.data.EventData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekFragment extends Fragment {

    private List<String> mDaysStrings;
    private List<String> mMonthColorStrings;
    private Calendar mCalendar;
    private Integer mCurrentMonth;
    private Integer mNumberOfFragment;

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

        mDaysStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday),
                getString(R.string.sunday)));

        mMonthColorStrings = new ArrayList<>(Arrays.asList(
                getString(R.string.p_january),
                getString(R.string.p_february),
                getString(R.string.p_march),
                getString(R.string.p_april),
                getString(R.string.p_may),
                getString(R.string.p_june),
                getString(R.string.p_julay),
                getString(R.string.p_august),
                getString(R.string.p_september),
                getString(R.string.p_october),
                getString(R.string.p_november),
                getString(R.string.p_december)));

        mNumberOfFragment = getArguments().getInt("num");
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalendar.add(Calendar.WEEK_OF_YEAR, (mNumberOfFragment));
        mCurrentMonth = mCalendar.get(Calendar.MONTH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.week_view_fragment, container, false);
        RecyclerView mWeekRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mWeekRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());
        return rootView;
    }

    ////////////////////////////             VIEW HOLDER

    public class DayOfWeekViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayName;
        private List<TextView> mEventViews = new ArrayList<>();
        private TextView mEventNum;
        private LinearLayout mLinearLayout;
        private Calendar cloneCalendar;
        private TextView mNewActivityButton;

        public DayOfWeekViewHolder(View itemView) {
            super(itemView);
            //find view
            mDayName = (TextView) itemView.findViewById(R.id.day_name_text_view);
            mEventViews.add((TextView) itemView.findViewById(R.id.week_view_event_0));
            mEventViews.add((TextView) itemView.findViewById(R.id.week_view_event_1));
            mEventViews.add((TextView) itemView.findViewById(R.id.week_view_event_2));
            mEventNum = (TextView) itemView.findViewById(R.id.week_view_event_num);
            mNewActivityButton= (TextView) itemView.findViewById(R.id.day_button);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.day_linear_layout);

        }

        public void bindDay(String dayName) {

            cloneCalendar = (Calendar) mCalendar.clone();
            mDayName.setText(dayName);

            //change color depend of month
            String monthString = mMonthColorStrings.get(mCurrentMonth) + "_day_title";
            Integer thisMonthColor = ContextCompat.getColor(getActivity(), getActivity().getResources().getIdentifier(monthString, "color", getActivity().getPackageName()));
            mDayName.setTextColor(thisMonthColor);

            //get stroke width
            Resources resources = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
            Integer intPx = Math.round(px);

            //change stroke color depend of month
            GradientDrawable strokeDrawable = (GradientDrawable) ContextCompat.getDrawable(getActivity(), getActivity().getResources().getIdentifier("stroke_shape", "drawable", getActivity().getPackageName()));
            String monthColorString = mMonthColorStrings.get(mCurrentMonth) + "_stroke";
            strokeDrawable.setStroke(intPx, ContextCompat.getColor(getActivity(), getActivity().getResources().getIdentifier(monthColorString, "color", getActivity().getPackageName())));
            mLinearLayout.setBackground(strokeDrawable);

            mNewActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DayActivity.class);
                    intent.putExtra("year", cloneCalendar.get(Calendar.YEAR));
                    intent.putExtra("month", cloneCalendar.get(Calendar.MONTH));
                    intent.putExtra("day", cloneCalendar.get(Calendar.DAY_OF_MONTH));
                    startActivity(intent);
                }
            });

            strokeDrawable = null;
            monthColorString = null;
            thisMonthColor = null;
            resources = null;
            intPx = null;

            //new thread to get data from content provider
            new Thread(new Runnable() {
                public void run() {
                    CalendarDataSource readerEvents = new CalendarDataSource(getContext());
                    final List<EventData> events = readerEvents.getEventsFromDay(cloneCalendar);
                    if (getActivity() == null) return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //set data to views
                            if (events.size() < 3) {
                                for (int i = 0; i < events.size(); i++) {
                                    mEventViews.get(i).setText(events.get(i).getTitle());
                                    Log.d("UI", events.get(i).getTitle());
                                }
                            } else {
                                for (int i = 0; i < 3; i++) {
                                    mEventViews.get(i).setText(events.get(i).getTitle());
                                }
                                if ((events.size() - 3) > 0) {
                                    mEventNum.setText("+" + (events.size() - 3));
                                }
                            }
                        }
                    });
                }
            }).start();
        }
    }

    ////////////////////////////             ADAPTER

    public class DayOfWeekRecyclerAdapter extends RecyclerView.Adapter<DayOfWeekViewHolder> {

        @Override
        public DayOfWeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.week_view_day, parent, false);
            return new DayOfWeekViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayOfWeekViewHolder holder, int position) {

            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            String dayName = mDaysStrings.get(position);
            holder.bindDay(dayName + "  " + day);
            mCalendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        @Override
        public int getItemCount() {
            return mDaysStrings.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMonthColorStrings = null;
        mCalendar = null;
        mDaysStrings = null;
        mNumberOfFragment = null;
        mCurrentMonth = null;
    }
}



