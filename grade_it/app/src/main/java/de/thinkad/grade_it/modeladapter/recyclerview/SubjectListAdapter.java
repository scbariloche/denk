package de.thinkad.grade_it.modeladapter.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.activities.MainActivity;
import de.thinkad.grade_it.gui.dialogs.AddSubjectDialog;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.pojos.StudentSubject;
import de.thinkad.grade_it.pojos.Subject;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andreas on 25.08.2017.
 */

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder> {
    List<StudentSubject> objects = new ArrayList<>();
    Activity context;
    DBHelper db;
    MainNotifier mainNotifier;
    Vibrator vibrator;

    public SubjectListAdapter(Context context, List<StudentSubject> objects, MainNotifier
            onClickListener) {
        this.objects = objects;
        this.context = (Activity) context;
        db = new DBHelper(context);
        this.mainNotifier = onClickListener;
    }


    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_subject_list, parent, false);
        return new SubjectViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final SubjectViewHolder customViewHolder, final int position) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (db.getCurrentSubject().getId() == objects.get(position).getSubject().getId()) {
            customViewHolder.txtTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            mainNotifier.subjectListScrollTo(position);

        }
        if (objects.size() > 0) {

            final StudentSubject studSubj = objects.get(position);
            Subject subject = objects.get(position).getSubject();


            Drawable wrapDrawable = DrawableCompat.wrap(customViewHolder.button.getBackground());
            DrawableCompat.setTint(wrapDrawable, objects.get(position).getColor());

            customViewHolder.button.setBackgroundDrawable(DrawableCompat.unwrap(wrapDrawable));

            customViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    db.insertCurrentSubjectId(studSubj.getSubject().getId());
                    mainNotifier.subjectListScrollTo(position);
                    mainNotifier.notifyDataChanged();



                }
            });
            customViewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Helper.createAlert(context, "wirklich löschen?", "löschen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            db.delete(db.EXAM_TABLE, new String[]{db.SUBJ_ID},
                                    new String[]{String.valueOf(studSubj.getSubject().getId())});


                            db.delete(db.SUBJECT_TABLE, new String[]{db.SUBJ_ID},
                                    new String[]{String.valueOf(studSubj.getSubject().getId())});
                            db.delete(db.STUDENTSUBJECT_TABLE, new String[]{db.SUBJ_ID, db.STUD_ID},
                                    new String[]{String.valueOf(studSubj.getSubject().getId()),
                                            String.valueOf(studSubj.getStudent().getId())});


                            mainNotifier.notifyDataChanged();
                            if (db.getStudentSubjectsByStudentId(db.getCurrentStudent().getId()).size() > 0) {
                                db.insertCurrentSubjectId(db.getStudentSubjectsByStudentId(db.getCurrentStudent().getId())
                                        .get(0).getSubject().getId());
                            } else {
                                db.insertCurrentSubjectId(0);
                            }
                            notifyItemChanged(position);
                        }
                    }, "ändern", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(context, AddSubjectDialog.class);
                            Bundle args = new Bundle();
                            args.putSerializable("studSubj", studSubj);
                            intent.putExtras(args);
                            context.startActivityForResult(intent, Constants.INT_FRAGMENT_CODE_ADD_SUBJECT_DONE);
                            mainNotifier.subjectListScrollTo(position);
                        }
                    }).show();
                    vibrator.vibrate(40);
                    return false;
                }

            });
            customViewHolder.txtTitle.setText(subject.getName());
            final Weight w = Helper.buildWeight(db, objects.get(position).getWeight().getId());
            float avg = studSubj.getAverageGrade(db, w);
            if (avg > 0) {
                customViewHolder.button.setText(String.valueOf(
                        Math.round(studSubj.getAverageGrade(db, w) * 100.0) / 100.0));
            } else {
                customViewHolder.button.setText("-.-");
            }

            if (studSubj.getColor() >= Color.parseColor("#808080")) {

                customViewHolder.button.setTextColor(Color.BLACK);
            } else {
                customViewHolder.button.setTextColor(Color.WHITE);

            }
        } else {
            db.insertCurrentSubjectId(0);
        }
        customViewHolder.txtTitle.bringToFront();
        customViewHolder.txtTitle.invalidate();

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    class SubjectViewHolder extends RecyclerView.ViewHolder {
        protected Button button;
        protected TextView txtTitle;
        protected View view;

        public SubjectViewHolder(View view) {
            super(view);
            this.button = view.findViewById(R.id.btn_subjectList);
            this.txtTitle = view.findViewById(R.id.txt_subjectlist);
            this.view = view;
        }
    }
}
