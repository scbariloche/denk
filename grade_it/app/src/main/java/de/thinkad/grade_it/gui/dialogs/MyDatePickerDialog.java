package de.thinkad.grade_it.gui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Calendar;

import de.thinkad.grade_it.R;

/**
 * Created by andreas on 19.10.2017.
 */

public class MyDatePickerDialog extends Activity {


    private CalendarView calendarView;
    private Button btnCancel;
    private Button btnOK;
    private Calendar myCal = Calendar.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_datepicker);

        calendarView = findViewById(R.id.calendarView);
        btnCancel = findViewById(R.id.datepicker_btn_cancel);
        btnOK = findViewById(R.id.datepicker_btn_ok);
        myCal.setTimeInMillis(getIntent().getLongExtra("date", myCal.getTimeInMillis()));
        calendarView.setDate(myCal.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                myCal.set(i, i1, i2);
            }
        });


        // Use the Builder class for convenient dialog construction
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = getIntent();
                resultIntent.putExtra("date", myCal.getTimeInMillis());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = getIntent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });
//        finish();
    }


}
