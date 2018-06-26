package de.thinkad.grade_it.modeladapter.spinner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.callbacks.OnValueClick;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andreas on 25.08.2017.
 */

public class WeightValueSpinnerAdapter extends ArrayAdapter<Integer> {
    private List objects= new ArrayList();
    DBHelper db;
    Context context;

    public WeightValueSpinnerAdapter(Context context, ArrayList<Integer> values) {
        super(context, 0, values);
        this.context = context;
        db = new DBHelper(context);
        this.objects = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_weight_list, parent, false);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.textview_weight_titel);
        txtTitle.setText(""+(position+1));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_weight_list, parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.textview_weight_titel);
        txtTitle.setText(""+(position+1));

        return convertView;
    }
}
