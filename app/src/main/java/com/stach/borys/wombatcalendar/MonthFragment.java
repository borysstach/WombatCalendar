package com.stach.borys.wombatcalendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonthFragment extends Fragment {
    static MonthFragment newInstance(int num) {

        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_fragment, container, false);
        RecyclerView monthRecyclerView = (RecyclerView) rootView.findViewById(R.id.month_recycler_view);

        monthRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        monthRecyclerView.setAdapter(new MonthRecyclerAdapter());

        return rootView;
    }

    //////////////////////////////          VIEW HOLDER

    public class DayOfMonthViewHolder extends RecyclerView.ViewHolder {

        private TextView mDayNumber;

        public DayOfMonthViewHolder(View itemView) {
            super(itemView);
            mDayNumber = (TextView) itemView.findViewById(R.id.month_day_number);
        }

        public void bindDay(Integer position) {
            mDayNumber.setText("" + position);
        }

    }
    ////////////////////////////             ADAPTER

    public class MonthRecyclerAdapter extends RecyclerView.Adapter<DayOfMonthViewHolder> {


        @Override
        public DayOfMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.month_day_view, parent, false);
            return new DayOfMonthViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayOfMonthViewHolder holder, int position) {
          holder.bindDay(position);
        }

        @Override
        public int getItemCount() {
            return 36;
        }
    }
}
