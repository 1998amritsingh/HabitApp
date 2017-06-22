package com.example.android.habittracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by 1998a on 4/30/2017.
 */


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    /**
     * Initializes mm/dd/yyyy variables to corresponding calendar features.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    /**
     * Upon choosing a date from the DatePicker dialog, this method enters the date into a TextView for the user to see before they add on a task.
     * @param year year selected
     * @param month month of the year
     * @param day day of the month
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month+=1;
        String dayString="";
        String monthString = "";
        if(month<10){
            monthString = "0"+month;
        }
        else{
            monthString = Integer.toString(month);
        }
        if(day<10){
            dayString = "0"+day;
        }
        else{
            dayString = Integer.toString(day);
        }
        TextView date = (TextView) getActivity().findViewById(R.id.datepicker_date);
        date.setText(monthString+"/"+dayString+"/"+year);
    }
}
