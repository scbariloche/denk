package de.thinkad.grade_it.gui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.modeladapter.listview.ExamListAdapter;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.StudentExam;

/**
 * Created by andenk on 04.10.2017.
 */

public class ExamListDialog extends DialogFragment {
    DBHelper db;


    MainNotifier callback;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        db = new DBHelper(getActivity());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ArrayList<StudentExam> allStudentExamsBySubjId = db.getAllStudentExamsBySubjId(db.getCurrentSubject().getId());
        final ArrayList<Exam> availableExams = new ArrayList<>();
        for (StudentExam studex : allStudentExamsBySubjId) {
            availableExams.add(studex.getExam());
        }

        builder.setTitle(db.getCurrentSubject().getName())
                .setAdapter(new ExamListAdapter(getActivity(), availableExams, new MainNotifier() {
                    @Override
                    public void notifyDataChanged() {
                        callback.notifyDataChanged();
                    }

                    @Override
                    public void onExamChosen(Exam exam) {
                        ExamListDialog.this.dismiss();
                        callback.onExamChosen(exam);
                    }

                    @Override
                    public void onGradeAdded() {
                        callback.onGradeAdded();
                    }

                    @Override
                    public void onSubjectListChanged() {
                        callback.onSubjectListChanged();
                    }

                    @Override
                    public void subjectListScrollTo(int position) {

                    }
                }, false), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            callback = (MainNotifier) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
