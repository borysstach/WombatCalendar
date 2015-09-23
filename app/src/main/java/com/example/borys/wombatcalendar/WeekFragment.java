package com.example.borys.wombatcalendar;
import android.os.Bundle;
import  android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WeekFragment extends Fragment {

    static WeekFragment newInstance(int num) {
        WeekFragment f = new WeekFragment();

        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_week, container, false);
        int num = getArguments().getInt("num");
        TextView numTextView = (TextView)rootView.findViewById(R.id.textview_fragment);
        numTextView.setText("Week" + num);
        return rootView;
    }
}


