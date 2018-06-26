package de.thinkad.grade_it.modeladapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.helper.callbacks.AdapterOnClickListener;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.StudentExam;
import de.thinkad.grade_it.pojos.StudentSubject;
import de.thinkad.grade_it.pojos.Subject;

/**
 * Created by andreas on 25.08.2017.
 */

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder> {
    List<StudentSubject> objects = new ArrayList<>();
    Activity context;
    DBHelper db;
    AdapterOnClickListener mainNotifier;
    Vibrator vibrator;

    public SubjectListAdapter(Context context, List<StudentSubject> objects, AdapterOnClickListener onClickListener) {
        this.objects = objects;
        this.context = (Activity) context;
        db = new DBHelper(context);
        this.mainNotifier = onClickListener;
    }


    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_subject_list_text, parent, false);
        return new SubjectViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final SubjectViewHolder customViewHolder, final int position) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (db.getCurrentSubject().getId() == objects.get(position).getSubject().getId()) {
            customViewHolder.txtTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        if (objects.size() > 0) {

            final StudentSubject studSubj = objects.get(position);
            Subject subject = objects.get(position).getSubject();
            customViewHolder.txtTitle.setText(subject.getName());

            Drawable wrapDrawable = DrawableCompat.wrap(customViewHolder.textButton.getBackground());
            DrawableCompat.setTint(wrapDrawable, objects.get(position).getColor());

            customViewHolder.textButton.setBackgroundDrawable(DrawableCompat.unwrap(wrapDrawable));
            customViewHolder.textButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    db.insertCurrentSubjectId(studSubj.getSubject().getId());
                    mainNotifier.onClick();
                }
            });
            customViewHolder.textButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Helper.createAlert(context, "wirklich löschen?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            List studexams = db.getAllStudentExamsBySubjId(studSubj.getSubject().getId());
                            for (Exam exam :
                                    db.getAllExamsBySubjID(studSubj.getSubject().getId())) {

                                db.delete(db.EXAM_TABLE,new String[]{db.SUBJ_ID},
                                        new String[]{String.valueOf(studSubj.getSubject().getId())});
                            }

                            db.delete(db.SUBJECT_TABLE, new String[]{db.SUBJ_ID},
                                    new String[]{String.valueOf(studSubj.getSubject().getId())});
                            db.delete(db.STUDENTSUBJECT_TABLE, new String[]{db.SUBJ_ID, db.STUD_ID},
                                    new String[]{String.valueOf(studSubj.getSubject().getId()),
                                            String.valueOf(studSubj.getStudent().getId())});



                            mainNotifier.onClick();
                            if (db.getStudentSubjectsByStudentId(db.getCurrentStudent().getId()).size() > 0) {
                                db.insertCurrentSubjectId(db.getStudentSubjectsByStudentId(db.getCurrentStudent().getId())
                                        .get(0).getSubject().getId());
                            }else{
                                db.insertCurrentSubjectId(0);
                            }
                            notifyItemChanged(position);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    vibrator.vibrate(40);
                    return false;
                }

            });
        } else {
            db.insertCurrentSubjectId(0);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    class SubjectViewHolder extends RecyclerView.ViewHolder {
        protected Button textButton;
        protected TextView txtTitle;
        protected View view;

        public SubjectViewHolder(View view) {
            super(view);
            this.textButton = view.findViewById(R.id.btn_subjectList);
            this.txtTitle = view.findViewById(R.id.txt_subjectlist);
            this.view = view;
        }
    }
}
