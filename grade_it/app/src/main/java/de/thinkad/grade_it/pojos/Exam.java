package de.thinkad.grade_it.pojos;

/**
 * Created by andreas on 25.08.2017.
 */

public class Exam extends PojoMaster {

    Subject subject;
    Category category;
    long date;

    public Exam(Subject subject, Category category, long date) {
        this.subject = subject;
        this.category = category;
        this.date = date;
    }

    public Exam() {

    }

    public Subject getSubject() {

        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
