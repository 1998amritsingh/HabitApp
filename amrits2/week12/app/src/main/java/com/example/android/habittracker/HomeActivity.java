package com.example.android.habittracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button calendar;
    private Button habits;
    private DatabaseReference databaseReference;
    private ArrayList<Habit> mHabits = new ArrayList<>();
    private HabitAdapter mHabitAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        calendar = (Button) findViewById(R.id.calendar_home);
        habits = (Button) findViewById(R.id.habits_home);
        listView = (ListView) findViewById(R.id.shared_habit_view);
        /**
         * Reads from Firebase and lists tasks in the HomeActivity if user has made the tasks shareable.
         */
        databaseReference = FirebaseDatabase.getInstance().getReference("habits");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mHabits.clear();
                for(DataSnapshot habitSnapshot: dataSnapshot.getChildren()){
                    Habit value = habitSnapshot.getValue(Habit.class);
                    boolean shared = value.getShared();
                    if(shared){
                        mHabits.add(value);
                    }
                }
                mHabitAdapter = new HabitAdapter(HomeActivity.this, mHabits);
                listView.setAdapter(mHabitAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**
         * Links intent from calendar button on the HomeActivity to the CalendarActivity
         */
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        /**
         * Links intent from habits button on the HomeActivity to the MainActivity
         */
        habits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
