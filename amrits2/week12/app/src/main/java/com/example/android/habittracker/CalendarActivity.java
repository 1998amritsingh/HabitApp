package com.example.android.habittracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class CalendarActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private Button home;
    private Button habits;
    private DatabaseReference databaseReference;
    private ArrayList<Habit> mHabits = new ArrayList<>();
    private HabitAdapter mHabitAdapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        home = (Button) findViewById(R.id.home_calendar);
        habits = (Button) findViewById(R.id.habits_calendar);

        listView = (ListView) findViewById(R.id.habitview_calendar);
        databaseReference = FirebaseDatabase.getInstance().getReference("habits");

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        long time = System.currentTimeMillis();
        mCalendarView.setDate(time,false,true);

        /**
         * Links intent from home button on the CalendarActivity to the HomeActivity
         */
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Links intent from habits button on the CalendarActivity to the MainActivity
         */
        habits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            /**
             * Reads data from Firebase and loads it into the CalendarView
             * @param year year of the task entered
             * @param month month of the year in mm format
             *@param date day of the month that corresponds to the task
             */
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                final int newMonth=month+1;
                final int newYear=year;
                final int newDate=date;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mHabits.clear();
                        for(DataSnapshot habitSnapshot: dataSnapshot.getChildren()){
                            Habit value = habitSnapshot.getValue(Habit.class);
                            String dayString="";
                            String monthString = "";
                            if(newMonth<10){
                                monthString = "0"+newMonth;
                            }
                            else{
                                monthString = Integer.toString(newMonth);
                            }
                            if(newDate<10){
                                dayString = "0"+newDate;
                            }
                            else{
                                dayString = Integer.toString(newDate);
                            }
                            String date = monthString+"/"+dayString+"/"+newYear;
                            String habitDate = value.getDate();
                            if(date.equals(habitDate)){
                                mHabits.add(value);
                            }
                        }
                        mHabitAdapter = new HabitAdapter(CalendarActivity.this, mHabits);
                        listView.setAdapter(mHabitAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }
        });

}
}
