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

/**
 * Created by andreas on 25.08.2017.
 */

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> objects;
    DBHelper db;
    Context context;

    public CategoryListAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
        this.context = context;
        db = new DBHelper(context);
        this.objects = categories;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_category_list, parent, false);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_categoryList_title);
        txtTitle.setText(category.getName());


        return convertView;
    }

}
