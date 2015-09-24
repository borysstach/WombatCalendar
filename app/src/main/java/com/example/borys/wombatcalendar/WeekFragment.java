package com.example.borys.wombatcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WeekFragment extends Fragment {

    private RecyclerView mWeekRecyclerView;

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
        List<String> days= new ArrayList<>(Arrays.asList("pon","wt","śr","czw","pt","sob","ndz"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_week, container, false);
        int num = getArguments().getInt("num");
        mWeekRecyclerView = (RecyclerView)rootView.findViewById(R.id.week_recycler_view);
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return rootView;
    }

    

}


