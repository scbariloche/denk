package de.thinkad.grade_it.modeladapter.weightbuilder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andenk on 23.11.2017.
 */

public class WeightView extends View {

    private Weight weight;

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public WeightView(Context context, Weight weight) {
        super(context);
        this.weight = weight;
    }

    public WeightView(Context context, @Nullable AttributeSet attrs, Weight weight) {
        super(context, attrs);
        this.weight = weight;
    }

    public WeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Weight weight) {
        super(context, attrs, defStyleAttr);
        this.weight = weight;
    }
}
