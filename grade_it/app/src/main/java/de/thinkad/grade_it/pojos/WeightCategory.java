package de.thinkad.grade_it.pojos;

/**
 * Created by andreas on 25.08.2017.
 */

public class WeightCategory {
    Weight weight;
    Category category;
    int value;

    public WeightCategory() {

    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight subject) {
        this.weight = subject;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int weight) {
        this.value = weight;
    }

    public WeightCategory(Weight subject, Category category, int weight) {

        this.weight = subject;
        this.category = category;
        this.value = weight;
    }
}
