package de.thinkad.grade_it.pojos;

/**
 * Created by andreas on 25.08.2017.
 */

public class Student extends PojoMaster {

    String name;
    int gradesys;

    public Student(int id,String name, int gradesys) {
        setId(id);
       setName( name);
       setGradesys(gradesys);
    }

    public Student() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGradesys() {
        return gradesys;
    }

    public void setGradesys(int gradesys) {
        this.gradesys = gradesys;
    }
}
