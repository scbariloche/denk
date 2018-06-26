package de.thinkad.grade_it.gui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.text.SimpleDateFormat;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.StudentExam;

/**
 * Created by andenk on 04.10.2017.
 */

public class AddGradeDialog extends DialogFragment {

    DBHelper db;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
    private MainNotifier callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        db = new DBHelper(getActivity());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        final Exam chosenExam = (Exam) args.get("exam");
        String grade_points="";
        callback= (MainNotifier) getActivity();
        if(db.getCurrentStudent().getGradesys()==0){
          grade_points="Note";
        }else{
           grade_points="Punkte";
        }
        builder.setTitle(grade_points+" für " + chosenExam.getSubject().getName() + "-" +
                chosenExam.getCategory().getName() + " vom " + sdf.format(chosenExam.getDate()));

        if (db.getCurrentStudent().getGradesys() == 0) {
            builder.setItems(R.array.grades_1_6, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.insertStudentExam(new StudentExam(db.getStudent(1), chosenExam,
                            (i!=6)?  Constants.getInstance().grade2Points.get(String.valueOf(i + 1)):String.valueOf(-1)));
                    callback.onGradeAdded();
                }
            });
        } else {
            builder.setItems(R.array.grades_15_0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.insertStudentExam(new StudentExam(db.getStudent(1), chosenExam,
                            (i!=15)?String.valueOf(15-i):String.valueOf(-1)));
                    callback.onGradeAdded();
                }
            });

        }
        return builder.create();
    }

}
