package com.stach.borys.wombatcalendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stach.borys.wombatcalendar.data.CalendarDataSource;
import com.stach.borys.wombatcalendar.data.EventData;
import com.stach.borys.wombatcalendar.data.WeekData;

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
    private WeekData mWeek;

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
        setDate();
        mCurrentMonth = mCalendar.get(Calendar.MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.week_view_fragment, container, false);
        //mWeek = new WeekData(new ArrayList<EventData>());
        final RecyclerView mWeekRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        Integer gridSize = 2;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridSize = 3;
        }
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridSize));
        mWeekRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());
        new Thread(new Runnable() {
            public void run() {
                Calendar endOfWeek = Calendar.getInstance();
                endOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                endOfWeek.add(Calendar.WEEK_OF_YEAR, (mNumberOfFragment));
                long begin = CalendarDataSource.getBeginInMillis(mCalendar);
                long end = CalendarDataSource.getEndInMillis(endOfWeek);
                CalendarDataSource readerEvents = new CalendarDataSource(getContext());
                mWeek = new WeekData(readerEvents.getEvents(begin, end));
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWeekRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());
                        }
                    });
                }
            }
        }).start();
        return rootView;
    }

    ////////////////////////////             VIEW HOLDER

    public class DayOfWeekViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayName;
        private List<TextView> mEventViews = new ArrayList<>();
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
            mEventViews.add((TextView) itemView.findViewById(R.id.week_view_event_num));
            mNewActivityButton = (TextView) itemView.findViewById(R.id.day_button);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.day_linear_layout);
        }

        public void bindDay(String dayName) {

            cloneCalendar = (Calendar) mCalendar.clone();
            mDayName.setText(dayName);

            //change color depend of month
            String monthString = mMonthColorStrings.get(mCurrentMonth) + "_day_title";
            String eventString = mMonthColorStrings.get(mCurrentMonth) + "_just_text";
            Integer thisMonthColor = ContextCompat.getColor(getActivity(), getActivity().getResources().getIdentifier(monthString, "color", getActivity().getPackageName()));
            Integer eventColor = ContextCompat.getColor(getActivity(), getActivity().getResources().getIdentifier(eventString, "color", getActivity().getPackageName()));
            mDayName.setTextColor(thisMonthColor);
            for (TextView event : mEventViews) {
                event.setTextColor(eventColor);
            }
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
            if (mWeek != null) {
                List<EventData> events = mWeek.get(cloneCalendar.get(Calendar.DAY_OF_WEEK));
                if (events != null) {
                    if (events.size() <= 4) {
                        for (int i = 0; i < events.size(); i++) {
                            mEventViews.get(i).setVisibility(View.VISIBLE);
                            mEventViews.get(i).setText(formatEvent(events.get(i).getTitle()));
                        }
                    } else {
                        for (int i = 0; i < 3; i++) {
                            mEventViews.get(i).setVisibility(View.VISIBLE);
                            mEventViews.get(i).setText(formatEvent(events.get(i).getTitle() ));
                        }
                        mEventViews.get(3).setVisibility(View.VISIBLE);
                        mEventViews.get(3).setText(" +" + (events.size() - 3));
                    }
                }
                events = null;
            }
            strokeDrawable = null;
            monthColorString = null;
            thisMonthColor = null;
            resources = null;
            intPx = null;
        }
    }

    private String formatEvent (String eventTitle){
        return String.format(getActivity().getResources().getString(R.string.format_event), eventTitle);
    }

    ////////////////////////////             ADAPTER

    public class DayOfWeekRecyclerAdapter extends RecyclerView.Adapter<DayOfWeekViewHolder> {

        public DayOfWeekRecyclerAdapter() {
            setDate();
        }

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

    private void setDate() {
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalendar.add(Calendar.WEEK_OF_YEAR, (mNumberOfFragment));
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



