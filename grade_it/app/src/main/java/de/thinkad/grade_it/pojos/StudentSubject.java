package de.thinkad.grade_it.pojos;

import android.graphics.Color;

import java.util.List;

import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;

/**
 * Created by andreas on 25.08.2017.
 */

public class StudentSubject extends PojoMaster {
    Student student;
    Subject subject;
    int color;
    Weight weight;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public StudentSubject(Student student, Subject subject, int color, Weight weight) {
        this.student = student;
        this.subject = subject;
        this.weight = weight;

        this.color = color;

    }

    public StudentSubject() {

    }


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public float getAverageGrade(DBHelper db, Weight weight) {
        float zaehler = 0f;
        int nenner = 0;
        for (Object o : weight.getList()) {
            if (o instanceof Weight) {
                float res = getAverageGrade(db, (Weight) o) * ((Weight) o).getValue();
                if (res > 0) {
                    zaehler += getAverageGrade(db, (Weight) o) * ((Weight) o).getValue();
                    nenner += ((Weight) o).getValue();
                }
            } else if (o instanceof WeightCategory) {
                List<StudentExam> exams = db.getAllStudentExamsBySubjId(this.getSubject().getId());
                for (StudentExam e : exams) {
                    if (e.getGrade() != null) {
                        if (Integer.parseInt(e.getGrade()) > -1) {
                            if (e.getExam().getCategory().getId() == ((WeightCategory) o).getCategory().getId()) {
                                String grade = (db.getCurrentStudent().getGradesys() == 0 ?
                                        Constants.getInstance().points2Grade.get(e.getGrade()) : e.getGrade());
                                if (grade != null) {
                                    zaehler += Float.parseFloat(grade) * ((WeightCategory) o).getValue();
                                    nenner += ((WeightCategory) o).getValue();
                                }
                            }
                        }
                    }
                }
            }
        }


        return zaehler / ((nenner == 0) ? -1 : nenner);
    }
}
