package com.stach.borys.wombatcalendar;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stach.borys.wombatcalendar.data.CalendarDataSource;
import com.stach.borys.wombatcalendar.data.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MonthFragment extends Fragment {

    private Integer numberOfDays;
    private Calendar mCalendar;
    private List<String> mMonthStrings;
    private List<String> mMonthColorStrings;
    private Boolean[] mEventsModel;
    private Integer mMonthColor;
    private RecyclerView mMonthRecyclerView;

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

        setCalendar();
        setEventsModel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_fragment, container, false);
        mMonthRecyclerView = (RecyclerView) rootView.findViewById(R.id.month_recycler_view);
        mMonthRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mMonthRecyclerView.setAdapter(new MonthRecyclerAdapter());
        setStyle(rootView);
        getData();

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

        public void bindDay(final Integer day) {

            mDayNumber.setText("" + (day));
            setColor(mEventsModel[day - 1]);
            setonClickIntent(day);
        }

        private void setonClickIntent(final Integer day) {
            mFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DayActivity.class);
                    intent.putExtra("year", mCalendar.get(Calendar.YEAR));
                    intent.putExtra("month", mCalendar.get(Calendar.MONTH));
                    intent.putExtra("day", day);
                    startActivity(intent);
                }
            });
        }

        private void setColor(Boolean aBoolean) {
            Integer oppositeColor = ContextCompat.getColor(getActivity(), R.color.day_stroke_back);
            if (aBoolean){
                mDayNumber.setTextColor(oppositeColor);
                mFrameLayout.setBackgroundColor(mMonthColor);
            } else {
                mDayNumber.setTextColor(mMonthColor);
                mFrameLayout.setBackgroundColor(oppositeColor);
            }
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
            if (thisMonth(position)) {
                holder.bindDay(day);
                day++;
            }
        }

        @Override
        public int getItemCount() {
            return numberOfDays + getDayOfWeekFromFirstDayOfMonth(mCalendar);
        }

        private boolean thisMonth(int position) {
            return position >= getDayOfWeekFromFirstDayOfMonth(mCalendar);
        }
    }

    ////////////////////////// ENDING METHODS

    @Override
    public void onDestroy() {
        super.onDestroy();
        numberOfDays = null;
        mCalendar = null;
        mMonthStrings = null;
        mMonthColorStrings = null;
        mMonthColor = null;
        mEventsModel = null;
    }

    ///////////////////////  HELPING METHODS

    private void setEventsModel() {
        numberOfDays = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mEventsModel = new Boolean[numberOfDays];
        for (int i = 0; i<numberOfDays;i++){
            mEventsModel[i] = false;
        }
    }

    private void setCalendar() {
        Integer numberOfFragment = getArguments().getInt("num");
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mCalendar.add(Calendar.MONTH, numberOfFragment);
    }

    private void setStyle(View rootView) {
        setMonthColor();
        setElementsBackground(rootView);
        setMonthTitles(rootView);
        setMonthPicture(rootView);
    }

    private void getData() {
        new Thread(new Runnable() {
            public void run() {
                Calendar cloneCalendar = (Calendar) mCalendar.clone();
                cloneCalendar.set(Calendar.DAY_OF_MONTH, 1);
                long begin = CalendarDataSource.getBeginInMillis(cloneCalendar);
                cloneCalendar.set(Calendar.DAY_OF_MONTH, numberOfDays);
                long end = CalendarDataSource.getEndInMillis(cloneCalendar);
                CalendarDataSource readerEvents = new CalendarDataSource(getContext());
                List<Event> WholeMonthEvents = readerEvents.getEvents(begin, end);

                setEventsArray(WholeMonthEvents);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMonthRecyclerView.setAdapter(new MonthRecyclerAdapter());
                        }
                    });
                }
            }
        }).start();
    }

    private void setMonthTitles(View rootView) {
        TextView monthTitle = (TextView) rootView.findViewById(R.id.month_title);
        monthTitle.setText(mMonthStrings.get(mCalendar.get(Calendar.MONTH)).toUpperCase());
        monthTitle.setTextColor(mMonthColor);

        TextView yearTitle = (TextView) rootView.findViewById(R.id.month_year_title);
        yearTitle.setText("" + mCalendar.get(Calendar.YEAR));
        yearTitle.setTextColor(mMonthColor);
    }

    private void setElementsBackground(View rootView) {
        TextView line = (TextView) rootView.findViewById(R.id.month_line);
        line.setBackgroundColor(mMonthColor);
        LinearLayout weekPanel = (LinearLayout) rootView.findViewById(R.id.month_week_panel);
        weekPanel.setBackgroundColor(mMonthColor);
    }

    private void setMonthColor() {
        String monthColorString = mMonthColorStrings.get(mCalendar.get(Calendar.MONTH)) + "_day_title";
        mMonthColor = ContextCompat.getColor(getActivity(), getActivity().getResources().getIdentifier(monthColorString, "color", getActivity().getPackageName()));
    }

    private void setMonthPicture(View rootView) {
        ImageView monthImage = (ImageView) rootView.findViewById(R.id.month_image);
        final String monthPictureString = mMonthColorStrings.get(mCalendar.get(Calendar.MONTH));
        Bitmap monthPic = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(monthPictureString, "drawable", getActivity().getPackageName()));
        monthImage.setImageBitmap(monthPic);
        monthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ImageActivity.class);
                intent.putExtra(WeekActivity.PICTURE_INTENT, monthPictureString);
                startActivity(intent);
            }
        });
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

    private void setEventsArray(List<Event> events){
        for(Event event : events){
            mEventsModel[dayOfMonth(event)] = true ;
        }
    }

    private Integer dayOfMonth(Event event){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(event.getBegin());
        return calendar.get(Calendar.DAY_OF_MONTH) - 1;
    }

}
