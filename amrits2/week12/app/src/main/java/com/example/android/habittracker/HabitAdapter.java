package com.example.android.habittracker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1998a on 4/30/2017.
 */

public class HabitAdapter extends ArrayAdapter<Habit> {

    private Activity context;
    private List<Habit> habitList;

    public HabitAdapter(Activity context, List<Habit> habitList){
        super(context, R.layout.checkbox, habitList);
        this.context = context;
        this.habitList = habitList;
    }
    /**
     * Sets up the ListView that is used in Main, Calendar, and Home.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.checkbox, null, true);

        CheckBox checkBox = (CheckBox) listViewItem.findViewById(R.id.checkBox);
        Habit habit = habitList.get(position);
        checkBox.setText(habit.getHabit());

        return listViewItem;
    }


}