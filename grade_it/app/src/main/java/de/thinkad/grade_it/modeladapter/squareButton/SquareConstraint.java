package de.thinkad.grade_it.modeladapter.squareButton;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

/**
 * Created by andenk on 05.10.2017.
 */
public class SquareConstraint extends ConstraintLayout {
    public SquareConstraint(Context context) {
        super(context);
    }

    public SquareConstraint(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareConstraint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size); // make it square

    }
}