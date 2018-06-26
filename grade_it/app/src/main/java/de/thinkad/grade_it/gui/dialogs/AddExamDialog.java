package de.thinkad.grade_it.gui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.modeladapter.spinner.CategorySpinnerAdapter;
import de.thinkad.grade_it.pojos.Category;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.StudentExam;
import de.thinkad.grade_it.pojos.Subject;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andenk on 04.10.2017.
 */

public class AddExamDialog extends Activity {
    DBHelper db;
    Calendar myCal = Calendar.getInstance();
    TextView tvDate;
    TextView tvTitle;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Spinner spCate;
    Button btnOk;
    Exam exam;
    StudentExam studExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
        setContentView(R.layout.dialog_new_exam);
        spCate = findViewById(R.id.spinner_newExam_category);
        tvDate = findViewById(R.id.textview_newExam_date);
        tvTitle = findViewById(R.id.textview_newExam_title);
        btnOk = findViewById(R.id.btn_newExam_ok);

        tvDate.setText(sdf.format(myCal.getTime()));
        ArrayList<Category> spinnerArray = new ArrayList<>();
        final Subject subject = db.getCurrentSubject();
        tvTitle.setText(subject.getName());

        Weight weight = db.getStudentSubjectBySubjectId(subject.getId()).getWeight();
        Helper.buildWeight(db, weight.getId()).getUsedCategories(spinnerArray);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, spinnerArray);

        spCate.setAdapter(adapter);

        if (getIntent().getExtras() != null) {
            exam = (Exam) getIntent().getExtras().getSerializable("exam");
            spCate.setSelection(exam.getCategory().getId() - 1);
            myCal.setTimeInMillis(exam.getDate());
            updateDateLabel();
            studExam = db.getStudExam(db.getCurrentStudent().getId(), exam.getId());
        } else {
            exam = new Exam();
            studExam = new StudentExam(db.getCurrentStudent(), exam, "-1");

        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exam.setSubject(subject);
                exam.setCategory((Category) spCate.getSelectedItem());
                exam.setDate(myCal.getTimeInMillis());
                long examId = db.insertExam(exam);

                db.insertStudentExam(db.getCurrentStudent().getId(), (int) examId,
                        Integer.parseInt(studExam.getGrade()));

                Intent resultIntent = getIntent();
                resultIntent.putExtra("exam", db.getExam((int) examId));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddExamDialog.this, MyDatePickerDialog.class);
                intent.putExtra("date", myCal.getTimeInMillis());
                startActivityForResult(intent,
                        Constants.RESULTCODE_ONDATEPICKED);


            }
        });


    }

    private void updateDateLabel() {

        String myFormat = "dd.MM.yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tvDate.setText(sdf.format(myCal.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            if (requestCode == Constants.RESULTCODE_ONDATEPICKED) {
                myCal.setTimeInMillis(data.getLongExtra("date", myCal.getTimeInMillis()));
                updateDateLabel();

            }

        }
    }
}
