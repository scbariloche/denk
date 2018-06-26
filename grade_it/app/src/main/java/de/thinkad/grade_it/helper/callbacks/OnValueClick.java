package de.thinkad.grade_it.helper.callbacks;

import de.thinkad.grade_it.pojos.Weight;
import de.thinkad.grade_it.pojos.WeightCategory;

/**
 * Created by andreas on 24.10.2017.
 */

public interface OnValueClick {

    void onWeightClick(Weight weight);

    void onWeightCategoryClick(WeightCategory weigCate);
}
