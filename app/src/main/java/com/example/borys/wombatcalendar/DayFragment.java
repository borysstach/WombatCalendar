package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DayFragment extends Fragment{


    static DayFragment newInstance (int num ){
        DayFragment fragment = new DayFragment();

        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //find rootView with RecyclerView
        View rootView = inflater.inflate(R.layout.fragment_week, container, false);
        //create Grid Recycler View
        RecyclerView mDayRecyclerView = (RecyclerView) rootView.findViewById(R.id.week_recycler_view);
        mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mDayRecyclerView.setAdapter(new DayOfWeekRecyclerAdapter());

        return rootView;
    }

}
