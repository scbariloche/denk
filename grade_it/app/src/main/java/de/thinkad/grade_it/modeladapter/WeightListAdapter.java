package de.thinkad.grade_it.modeladapter;


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
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andreas on 25.08.2017.
 */

public class WeightListAdapter extends ArrayAdapter<Weight> {
    private ArrayList<Weight> objects;
    DBHelper db;
    Context context;

    public WeightListAdapter(Context context, ArrayList<Weight> exams) {
        super(context, 0, exams);
        this.context = context;
        db = new DBHelper(context);
        this.objects = exams;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Weight weight = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_weight_list, parent, false);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.textview_weight_titel);
        txtTitle.setText(weight.getName());


        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Weight weight = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_weight_list, parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.textview_weight_titel);
        txtTitle.setText(weight.getName());

        return convertView;
    }
}
