package com.example.dwarf.alpha;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

import java.lang.reflect.Field;

public class TimePickerFrag extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour =c.get(Calendar.HOUR_OF_DAY);
        int minute =c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,true);
    }

    public void onTimeSet(TimePicker view,int hourOfDay,int minute){
        Toast.makeText(getActivity(),"Time Set: "+hourOfDay+minute,Toast.LENGTH_LONG).show();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}



