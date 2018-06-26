package de.thinkad.grade_it.helper.callbacks;

import de.thinkad.grade_it.pojos.Exam;

/**
 * Created by andenk on 05.10.2017.
 */

public interface MainNotifier {
    void notifyDataChanged();
    void onExamChosen(Exam exam);
    void onGradeAdded();
    void onSubjectListChanged();
    void subjectListScrollTo(int position);
}
