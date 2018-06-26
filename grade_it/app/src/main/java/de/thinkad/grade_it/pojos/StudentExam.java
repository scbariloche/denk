package de.thinkad.grade_it.pojos;

import java.util.HashMap;
import java.util.Map;

import de.thinkad.grade_it.helper.Constants;

/**
 * Created by andreas on 25.08.2017.
 */

public class StudentExam  {
    Student student;
    Exam exam;
    String grade;

    public StudentExam( Student student, Exam exam, String grade) {

        this.student = student;
        this.exam = exam;
        this.grade = grade;
    }

    public StudentExam() {

    }

    public String getGrade() {
        return grade;

    }


    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }


}
