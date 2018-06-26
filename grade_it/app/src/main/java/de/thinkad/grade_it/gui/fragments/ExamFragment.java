package de.thinkad.grade_it.gui.fragments;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.activities.MainActivity;
import de.thinkad.grade_it.gui.dialogs.AddExamDialog;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.modeladapter.listview.ExamListAdapter;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.StudentExam;

/**
 * Created by andreas on 25.08.2017.
 */

public class ExamFragment extends Fragment {

    ListView listView;
    DBHelper db;
    ImageButton btnAddExam;
    private Vibrator vibrator;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        View view = inflater.inflate(R.layout.fragment_exams, container, false);
        listView = view.findViewById(R.id.listview_student);
        btnAddExam = view.findViewById(R.id.btn_add_exam);
        update(getActivity());


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update(getActivity());

    }

    public void update(Context context) {
        ((MainActivity) getActivity()).update();
        db = new DBHelper(context);
        listView.setAdapter(null);
        if (db.getCurrentSubject() != null) {

            final ArrayList<StudentExam> allStudentExamsBySubjId = db.getAllStudentExamsBySubjId(db.getCurrentSubject().getId());
            final ArrayList<Exam> aviableExams = new ArrayList<>();
            for (StudentExam studex : allStudentExamsBySubjId) {
                aviableExams.add(studex.getExam());
            }


            listView.setAdapter(new ExamListAdapter(context, aviableExams, (MainNotifier) getActivity(), true));
        }
        btnAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.getCurrentSubject() != null) {


                    Intent intent = new Intent(getActivity(), AddExamDialog.class);
                    getActivity().startActivityForResult(intent, Constants.RESULTCODE_NEWEXAM_FINISHED);
                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getActivity());
                    }
                    builder.setMessage(getString(R.string.addSubjectFirst))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                }
            }
        });
        btnAddExam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(40);
                db.setdbVersion(0);
                db.upgradeDB(db.getWritableDatabase(),db.getdbVersion());
                onResume();
                return true;
            }
        });
    }


}
