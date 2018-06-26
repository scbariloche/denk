package de.thinkad.grade_it.pojos;

/**
 * Created by andreas on 25.08.2017.
 */

public class Category extends PojoMaster {
    public Category() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public Category(String name) {
        this.name = name;
    }
    public Category(int id,String name) {
        setId(id );
       setName(name);
    }
}
