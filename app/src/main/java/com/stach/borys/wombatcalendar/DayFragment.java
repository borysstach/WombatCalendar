package com.stach.borys.wombatcalendar;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stach.borys.wombatcalendar.data.CalendarDataSource;
import com.stach.borys.wombatcalendar.data.DayData;
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
        DayData day = new DayData(readerEvents.getEvents(begin, end));
        mEvents = day.getSortedEvents();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.day_fragment, container, false);
        RecyclerView mDayRecyclerView = (RecyclerView) rootView.findViewById(R.id.day_events_recycler);
        mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDayRecyclerView.setAdapter(new SingleDayRecyclerAdapter());

        return rootView;
    }

    public class SingleDayRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

        class DayEventViewHolder extends CustomViewHolder {

            private TextView mEventTitle;
            private TextView mEventStartHour;
            private TextView mEventStartMinute;
            private TextView mEventEndHour;
            private TextView mEventEndMinute;
            private LinearLayout mEventLayout;

            public DayEventViewHolder(View itemView) {
                super(itemView);
                mEventTitle = (TextView) itemView.findViewById(R.id.specific_time_event_name);
                mEventStartHour = (TextView) itemView.findViewById(R.id.starting_hour);
                mEventStartMinute = (TextView) itemView.findViewById(R.id.starting_minute);
                mEventEndHour = (TextView) itemView.findViewById(R.id.ending_hour);
                mEventEndMinute = (TextView) itemView.findViewById(R.id.ending_minute);
                mEventLayout = (LinearLayout) itemView.findViewById(R.id.day_event_linear_layout);
            }

            public void bindEvent(final EventData event) {
                mEventTitle.setText(event.getTitle());
                mEventStartHour.setText("" + event.getStartingHour());
                mEventStartMinute.setText(addZero(event.getStartingMinutes()));
                mEventEndHour.setText("" + event.getEndingHour());
                mEventEndMinute.setText(addZero(event.getEndingMinutes()));
                mEventLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.getId());
                        Intent intent = new Intent(Intent.ACTION_VIEW)
                                .setData(uri);
                        startActivity(intent);
                    }
                });
            }
        }

        private String addZero(Integer min) {
            if (min < 10) {
                return "0" + min;
            }
            return "" + min;
        }

        class AllDayEventViewHolder extends CustomViewHolder {

            private TextView mEventTitle;
            private LinearLayout mEventLayout;

            public AllDayEventViewHolder(View itemView) {
                super(itemView);
                mEventLayout = (LinearLayout) itemView.findViewById(R.id.day_allday_event_linear_layout);
                mEventTitle = (TextView) itemView.findViewById(R.id.all_day_time_event_name);
            }

            public void bindEvent(final EventData event) {
                mEventTitle.setText(event.getTitle());
                mEventLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.getId());
                        Intent intent = new Intent(Intent.ACTION_VIEW)
                                .setData(uri);
                        startActivity(intent);
                    }
                });
            }

        }

        class StandardEventViewHolder extends CustomViewHolder {

            private TextView mEventTitle;

            public StandardEventViewHolder(View itemView) {
                super(itemView);
                mEventTitle = (TextView) itemView.findViewById(R.id.standard_event_name);
            }

            public void bindEvent(EventData event) {
                mEventTitle.setText(event.getTitle());
            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch (viewType) {
                case 1:
                    View view_all = layoutInflater.inflate(R.layout.day_allday_event_layout, parent, false);
                    return new AllDayEventViewHolder(view_all);
                case 2:
                    View view_stan = layoutInflater.inflate(R.layout.day_standard_event_layout, parent, false);
                    return new StandardEventViewHolder(view_stan);
                default:
                    View view = layoutInflater.inflate(R.layout.day_event_layout, parent, false);
                    return new DayEventViewHolder(view);
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
            if (mEvents.get(position).isAllDay()) {
                return 1;
            } else if (mEvents.get(position).isStandard()) {
                return 2;
            } else {
                return 0;
            }
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(View itemView) {
            super(itemView);
        }

        public void bindEvent(EventData event) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCalendar = null;
        mEvents = null;
    }
}
