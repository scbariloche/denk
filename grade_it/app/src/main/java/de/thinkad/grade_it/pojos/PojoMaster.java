package de.thinkad.grade_it.pojos;

import java.io.Serializable;

/**
 * Created by andreas on 25.08.2017.
 */

public class PojoMaster implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;

}
