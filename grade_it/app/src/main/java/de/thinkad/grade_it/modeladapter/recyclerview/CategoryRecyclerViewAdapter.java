package de.thinkad.grade_it.modeladapter.recyclerview;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.pojos.Category;

/**
 * Created by andreas on 25.08.2017.
 */

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder> {
    List<Category> objects = new ArrayList<>();
    Activity context;
    DBHelper db;
    Vibrator vibrator;

    public CategoryRecyclerViewAdapter(Context context, List<Category> objects) {
        this.objects = objects;
        this.context = (Activity) context;
        db = new DBHelper(context);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_category_recycler, parent, false);
        return new CategoryViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final CategoryViewHolder view, final int position) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        view.txtTitle.setText(objects.get(position).getName());
        view.view.setOnTouchListener(new MyTouchListener(objects.get(position)));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        protected Button button;
        protected TextView txtTitle;
        protected View view;

        public CategoryViewHolder(View view) {
            super(view);

            this.txtTitle = view.findViewById(R.id.txt_categoryList_title);
            this.view = view;
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        Object o;
        Category category;

        public MyTouchListener(Category category) {
            this.category = category;
        }




        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            vibrator.vibrate(20);
            ClipData data = ClipData.newPlainText("cate", ""+category.getId());

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
//                view.setVisibility(View.INVISIBLE);
            return false;}
    }
}
