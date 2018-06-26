package de.thinkad.grade_it.modeladapter.spinner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.pojos.Category;

/**
 * Created by andreas on 25.08.2017.
 */

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> objects;
    DBHelper db;
    Context context;

    public CategorySpinnerAdapter(Context context, ArrayList<Category> exams) {
        super(context, 0, exams);
        this.context = context;
        db = new DBHelper(context);
        this.objects = exams;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_category_spinner, parent, false);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.tv_imprint_titel);
        txtTitle.setText(category.getName());


        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_category_spinner, parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.tv_imprint_titel);
        txtTitle.setText(category.getName());

        return convertView;
    }
}
