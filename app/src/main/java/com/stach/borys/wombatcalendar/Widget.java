package com.stach.borys.wombatcalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.stach.borys.wombatcalendar.data.CalendarDataSource;
import com.stach.borys.wombatcalendar.data.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Widget extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i=0; i<appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

            List<String> daysStrings = new ArrayList<>(Arrays.asList(
                    context.getString(R.string.monday),
                    context.getString(R.string.tuesday),
                    context.getString(R.string.wednesday),
                    context.getString(R.string.thursday),
                    context.getString(R.string.friday),
                    context.getString(R.string.saturday),
                    context.getString(R.string.sunday)));

            List<String> monthStrings = new ArrayList<>(Arrays.asList(
                    context.getString(R.string.w_january),
                    context.getString(R.string.w_february),
                    context.getString(R.string.w_march),
                    context.getString(R.string.w_april),
                    context.getString(R.string.w_may),
                    context.getString(R.string.w_june),
                    context.getString(R.string.w_julay),
                    context.getString(R.string.w_august),
                    context.getString(R.string.w_september),
                    context.getString(R.string.w_october),
                    context.getString(R.string.w_november),
                    context.getString(R.string.w_december)));

            //get events from today
            Calendar calendar = Calendar.getInstance();
            CalendarDataSource readerEvents = new CalendarDataSource(context);
            long begin = CalendarDataSource.getBeginInMillis(calendar);
            long end = CalendarDataSource.getEndInMillis(calendar);
            List<Event> events = readerEvents.getEvents(begin, end);

            // Create an Intent to launch DayActivity from today
            Intent intent = new Intent(context, DayActivity.class);
            intent.putExtra("year", calendar.get(Calendar.YEAR));
            intent.putExtra("month", calendar.get(Calendar.MONTH));
            intent.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.widget_whole, pendingIntent);
            views.setTextViewText(R.id.widget_day_month, "" + calendar.get(Calendar.DAY_OF_MONTH));
            views.setTextViewText(R.id.widget_day_week, "" + daysStrings.get(getDayOfWeek(calendar)));
            views.setTextViewText(R.id.widget_month, "" + monthStrings.get(calendar.get(Calendar.MONTH)));

            if (events.size() != 0){
                views.setImageViewResource(R.id.widget_back_image, R.drawable.kupki);
                List<Integer>mEventViews = new ArrayList<>();
                mEventViews.add(R.id.widget_event_1);
                mEventViews.add(R.id.widget_event_2);
                mEventViews.add(R.id.widget_event_3);
                mEventViews.add(R.id.widget_event_num);

                List<Integer>ImageViews = new ArrayList<>();
                ImageViews.add(R.id.widget_kupka_1);
                ImageViews.add(R.id.widget_kupka_2);
                ImageViews.add(R.id.widget_kupka_3);
                ImageViews.add(R.id.widget_kupka_4);

                if (events.size() <= 4) {
                    //bind only this events
                    for (int j = 0; j < events.size(); j++) {
                        views.setTextViewText(mEventViews.get(j), events.get(j).getTitle());
                        views.setViewVisibility(ImageViews.get(j), View.VISIBLE);
                    }
                } else {
                    //binds first events 3
                    for (int j = 0; j < 3; j++) {
                        views.setTextViewText(mEventViews.get(j), events.get(j).getTitle());
                        views.setViewVisibility(ImageViews.get(j), View.VISIBLE);
                    }
                    //on last show number of not showing events
                    views.setTextViewText(mEventViews.get(3)," +" + (events.size() - 3));
                    views.setViewVisibility(ImageViews.get(3), View.VISIBLE);
                }
            }
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    private Integer getDayOfWeek(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
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
