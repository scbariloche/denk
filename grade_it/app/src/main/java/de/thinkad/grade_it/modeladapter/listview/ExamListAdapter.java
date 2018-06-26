package de.thinkad.grade_it.modeladapter.listview;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.dialogs.AddExamDialog;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.StudentExam;

/**
 * Created by andreas on 25.08.2017.
 */

public class ExamListAdapter extends ArrayAdapter<Exam> {
    private final Vibrator vibrator;
    private ArrayList<Exam> objects;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    DBHelper db;
    Activity context;
    MainNotifier mainNotifier;
    boolean deletable;

    public ExamListAdapter(Context context, ArrayList<Exam> exams, MainNotifier mainNotifier, boolean deletable) {
        super(context, 0, exams);
        db = new DBHelper(context);
        this.objects = exams;
        this.context = (Activity) context;
        this.mainNotifier = mainNotifier;
        this.deletable = deletable;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final Exam item = objects.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_exam_list, parent, false);
        }

        if (deletable) {
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    vibrator.vibrate(40);
                    Helper.createAlert(context, context.getString(R.string.delete), "löschen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.delete(db.EXAM_TABLE, new String[]{db.EXAM_ID}, new String[]{String.valueOf(item.getId())});
                            db.delete(db.STUDENTEXAM_TABLE, new String[]{db.EXAM_ID}, new String[]{String.valueOf(item.getId())});
                            mainNotifier.notifyDataChanged();
                        }
                    }, "ändern", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(context, AddExamDialog.class);
                            Bundle args = new Bundle();
                            args.putSerializable("exam", item);
                            intent.putExtras(args);
                            context.startActivityForResult(intent, Constants.RESULTCODE_NEWEXAM_FINISHED);
                        }
                    }).show();

                    return true;
                }
            });
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exam chosenExam = objects.get(position);
                mainNotifier.onExamChosen(chosenExam);
            }
        });


        // Lookup view for data population
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_imprint_titel);
        TextView tvDate = (TextView) convertView.findViewById(R.id.textview_studentexam_date);
        TextView tvGrade = convertView.findViewById(R.id.textview_studentexam_grade);
        int color = db.getStudentSubjectBySubjectId(item.getSubject().getId()).getColor();

        if (color >= Color.parseColor("#808080")) {
            int red = (int) (Color.red(color) * 0.7);
            int green = (int) (Color.green(color) * 0.7);
            int blue = (int) (Color.blue(color) * 0.7);
            color = Color.rgb(red, green, blue);
        }
        tvCategory.setTextColor(color);


        tvCategory.setText(item.getCategory().getName());
        tvDate.setText(String.valueOf(sdf.format(item.getDate())));
        StudentExam studentExam = db.getStudExam(db.getStudent(1).getId(), item.getId());
        if (studentExam.getGrade() != null) {
            if (Integer.parseInt(studentExam.getGrade()) > -1) {
                if (db.getCurrentStudent().getGradesys() == 0) {
                    tvGrade.setText("Note: " + Constants.getInstance().points2Grade.get(db.getStudExam(db.getStudent(1).getId(), item.getId()).getGrade()));
                } else {
                    tvGrade.setText("Punkte: " + db.getStudExam(db.getStudent(1).getId(), item.getId()).getGrade());
                }
            }
        } else {
            tvGrade.setText("");
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
