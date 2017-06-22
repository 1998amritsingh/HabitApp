package com.example.android.habittracker;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private EditText editText;
    private Button addFirebase;
    private TextView datepicker;
    private DatabaseReference databaseReference;
    private ArrayList<Habit> mHabits = new ArrayList<>();
    private HabitAdapter mHabitAdapter;
    private Button home;
    private Button calendar;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = (Button) findViewById(R.id.home_addhabit);
        calendar = (Button) findViewById(R.id.calendar_addhabit);
        spinner = (Spinner) findViewById(R.id.share);

        datepicker = (TextView) findViewById(R.id.datepicker_date);
        editText = (EditText) findViewById(R.id.edit_habit);
        addFirebase = (Button) findViewById(R.id.firebase_add);
        listView = (ListView) findViewById(R.id.list_view);
        /**
         * Reads data from Firebase and loads in the correct date.
         */
        databaseReference = FirebaseDatabase.getInstance().getReference("habits");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mHabits.clear();
                for(DataSnapshot habitSnapshot: dataSnapshot.getChildren()){
                    Habit value = habitSnapshot.getValue(Habit.class);
                    String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                    String habitDate = value.getDate();
                    if(date.equals(habitDate)){
                        mHabits.add(value);
                    }
                }
                mHabitAdapter = new HabitAdapter(MainActivity.this, mHabits);
                listView.setAdapter(mHabitAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        addFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHabit();
            }
        });
        /**
         * Links intent from home button on the MainActivity to the HomeActivity
         */
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        /**
         * Links intent from calendar button on the MainActivity to the CalendarActivity
         */
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * Creates tasks and checks to see if the use has entered in all the necessary information and then assigns values from the database to the tasks.
     */
    private void addHabit(){
        String habit = editText.getText().toString();
        String date = datepicker.getText().toString();


        if(!TextUtils.isEmpty(habit)&&!date.equals("Date")){
            String id = databaseReference.push().getKey();
            Toast.makeText(this,"Habit added",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Please enter a Habit and Date",Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Makes Date Picker dialog show up upon selecting the option to choose a date.
     * @param view
     */
    public void showDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
