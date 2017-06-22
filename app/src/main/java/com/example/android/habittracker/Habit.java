package com.example.android.habittracker;

/**
 * Created by 1998a on 4/18/2017.
 */

public class Habit {
    String habitid;
    String habit;
    String date;
    boolean shared;

    public Habit(){

    }
    /**
     * Constructors for each tasks listed.
     */
    public Habit(String habitid, String habit, String date, boolean shared) {
        this.habitid = habitid;
        this.habit = habit;
        this.date = date;
        this.shared = shared;
    }

    public String getHabitid() {
        return habitid;
    }

    public String getHabit() {
        return habit;
    }

    public String getDate() {
        return date;
    }
    public boolean getShared(){
        return shared;
    }
}
