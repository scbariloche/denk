package de.thinkad.grade_it.pojos;

/**
 * Created by andreas on 25.08.2017.
 */

public class Subject extends PojoMaster {

    String name;
    int admin;


    public Subject(String name,  int admin) {
        this.name = name;
        this.admin = admin;
    }

    public Subject() {

    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
