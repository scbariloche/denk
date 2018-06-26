package de.thinkad.grade_it.gui.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.modeladapter.squareButton.SquareButton;
import de.thinkad.grade_it.modeladapter.spinner.WeightSpinnerAdapter;
import de.thinkad.grade_it.pojos.StudentSubject;
import de.thinkad.grade_it.pojos.Subject;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andenk on 04.10.2017.
 */

public class AddSubjectDialog extends Activity {
    DBHelper db;
    EditText txtTitle;
    Button btnOk;
    SquareButton btnColor;
    TextView tvChosenWeight;
    TextView lblWeight;
    public int chosenColor = Color.parseColor("#1699a2");
    private StudentSubject studSubj;
    private Subject subject;
    private int weightID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
        setContentView(R.layout.dialog_new_subject);

        txtTitle = findViewById(R.id.txt_newSubject_name);
        btnOk = findViewById(R.id.btn_newSubject_done);
        btnColor = findViewById(R.id.btn_newSubject_color);
        tvChosenWeight = findViewById(R.id.tv_newSubject_weight);
        lblWeight = findViewById(R.id.lbl_newSubject_weight);

        weightID = db.getFavouriteWeightId();


        if (getIntent().getExtras() != null) {
            studSubj = (StudentSubject) getIntent().getExtras().getSerializable("studSubj");
            weightID = studSubj.getWeight().getId();
            subject = studSubj.getSubject();
            chosenColor = studSubj.getColor();
            txtTitle.setText(subject.getName());
        } else {
            studSubj = new StudentSubject();
            subject = new Subject();
        }


        btnColor.setBackgroundColor(chosenColor);


        tvChosenWeight.setText(db.getWeight(weightID).getName());
        tvChosenWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSubjectDialog.this, SelectWeightDialog.class);
                ArrayList<Weight> allTopWeights = db.getAllTopWeights();
                int position = 0;
                for (int i = 0; i < allTopWeights.size(); i++) {
                    if (allTopWeights.get(i).getId() == weightID) {
                        position = i;
                    }
                }
                intent.putExtra("position", position);
                startActivityForResult(intent, Constants.RESULTCODE_ONWEIGHTPICKED);

            }
        });

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder
                        .with(AddSubjectDialog.this)
                        .initialColor(chosenColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                chosenColor = selectedColor;
                                btnColor.setBackgroundColor(chosenColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtTitle.getText().length() > 0) {
                    subject.setName(
                            txtTitle.getText().toString());

                    subject.setAdmin(db.getCurrentStudent().getId());

                    int subj_id = (int) db.insertSubject(subject);
                    db.insertCurrentSubjectId(subj_id);
                    db.insertStudentSubject(db.getCurrentStudent().getId(), subj_id, chosenColor,
                            weightID);
                    Intent resultIntent = getIntent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddSubjectDialog.this, "bitte einen Titel einfügen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.RESULTCODE_ONWEIGHTPICKED:
                    weightID = data.getIntExtra("weightID", db.getFavouriteWeightId());
                    tvChosenWeight.setText(db.getWeight(weightID).getName());
            }
        }

    }

}
