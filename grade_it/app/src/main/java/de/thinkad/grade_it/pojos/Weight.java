package de.thinkad.grade_it.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 25.08.2017.
 */

public class Weight extends PojoMaster {
    String name;
    int weightId;
    int value;
    List<Object> list = new ArrayList();

    public List<Object> getList() {
        return list;
    }

    public void add(Object o) {
        this.list.add(o);
    }

    public void addAll(List objects) {
        this.list.addAll(objects);

    }

    public Weight(int id, int weightId, int weight, String name) {
        this.id = id;
        this.name = name;
        this.weightId = weightId;
        this.value = weight;
    }

    public Weight(int weightId, int weight, String name) {
        this.name = name;
        this.weightId = weightId;
        this.value = weight;
    }

    public Weight() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeightId() {
        return weightId;
    }

    public void setWeightId(int weightId) {
        this.weightId = weightId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Category> getCategories() {
        List<Category> ret = new ArrayList<>();
        for (Object o : getList()) {
            if (o instanceof Weight) {
                ret.addAll(((Weight) o).getCategories());
            } else if (o instanceof WeightCategory) {
                ret.add(((WeightCategory) o).getCategory());
            }
        }
        return ret;
    }

    public void remove(Object o2delete) {
        for (Object o : list) {
            if (o instanceof Weight) {
                if (o.equals(o2delete)) {
                    list.remove(o);
                    break;
                }
            } else if (o instanceof WeightCategory) {
                if (o.equals(o2delete)) {
                    list.remove(o);
                    break;
                }
            }
        }
    }

    public void getUsedCategories(List<Category> list) {
        for (Object o : getList()) {
            if (o instanceof Weight) {
                ((Weight) o).getUsedCategories(list);
            } else if (o instanceof WeightCategory) {
                list.add(((WeightCategory) o).getCategory());
            }
        }


    }
}
