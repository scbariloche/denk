package de.thinkad.grade_it.gui.dialogs;

import android.content.ClipData;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.modeladapter.recyclerview.CategoryRecyclerViewAdapter;
import de.thinkad.grade_it.pojos.Category;
import de.thinkad.grade_it.pojos.Weight;
import de.thinkad.grade_it.pojos.WeightCategory;

/**
 * Created by andreas on 19.10.2017.
 */

public class CreateWeightDialog extends FragmentActivity {


    private DBHelper db;
    LinearLayout outerGroup;
    private TextView btnSave;
    private EditText title;
    private View newGroupContainer;
    private Weight topWeight;
    private RecyclerView catListView;
    private Vibrator vibrator;
    private List<Category> usedCategories = new ArrayList<>();
    private List<Category> categories2use = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = new DBHelper(this);
        setContentView(R.layout.dialog_create_weight);
        newGroupContainer = findViewById(R.id.new_weight_group_element);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        catListView = findViewById(R.id.listview_category_create_weight);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        catListView.setLayoutManager(layoutManager);

        CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(this, db.getAllCategories());
        catListView.setAdapter(adapter);

        title = findViewById(R.id.tv_create_weight_title);
        if (getIntent().hasExtra("topweight")) {
            int weightId = getIntent().getIntExtra("topweight", -1);
            topWeight = Helper.buildWeight(db, weightId);
            title.setText(topWeight.getName());
        } else {
            topWeight = new Weight();
            topWeight.setWeightId(-1);
            topWeight.setValue(1);
        }

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                topWeight.setName(s.toString());
            }
        });


        btnSave = findViewById(R.id.btn_create_weight_ok);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topWeight.getName() != null && topWeight.getName() != "") {
                    if (save(topWeight)) {
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    Toast.makeText(CreateWeightDialog.this, "bitte gebe einen Titel ein", Toast.LENGTH_SHORT).show();
                }


            }
        });


        newGroupContainer.setOnTouchListener(new TemplateGroupTouchListener(topWeight));

        outerGroup = findViewById(R.id.fragment_container_createweight);
        outerGroup.setOnDragListener(new InsertDragListener(topWeight));


        update();


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ((int) (height * 0.95));
        params.width = ((int) (width * 0.98));
        getWindow().setAttributes(params);


    }

    public void update() {
        usedCategories.clear();
        topWeight.getUsedCategories(usedCategories);


        invertLists();

        CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(this, categories2use);
        catListView.setAdapter(adapter);


        fillList(outerGroup, LayoutInflater.from(this), topWeight);
    }

    private void invertLists() {
        categories2use.clear();
        categories2use.addAll(db.getAllCategories());
        for (Category c : usedCategories) {
            for (int i = 0; i < categories2use.size(); i++) {
                if (categories2use.get(i).getId() == c.getId()){
//                    categories2use.remove(i);

                }
            }
        }
    }

    private void fillList(LinearLayout view, LayoutInflater inflater, Weight weight) {
        view.removeAllViews();
        for (final Object o : weight.getList()) {
            if (o instanceof Weight) {
                LinearLayout row = (LinearLayout) inflater.inflate(
                        R.layout.display_weight_outergroup_editable, view, false);
                TextView edTxtValue = row.findViewById(R.id.display_weightBuilder_value);
                edTxtValue.setText(String.valueOf(((Weight) o).getValue()));
                EditText title = row.findViewById(R.id.txt_btn_weightBuilder_addGroup);
                title.setText(((Weight) o).getName() == null | ((Weight) o).getName() == ""
                        ? "neue Gruppe" : ((Weight) o).getName());
                title.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        ((Weight) o).setName(editable.toString());
                    }
                });

                edTxtValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().isEmpty()) {
                            if (editable.toString().matches("[1-9]+")) {
                                ((Weight) o).setValue(Integer.parseInt(editable.toString()));
                            } else {
                                Toast.makeText(CreateWeightDialog.this,
                                        "bitte eine Gewichtung größer null eingeben",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                LinearLayout listviewInside = row.findViewById(R.id.display_weightBuilder_list);
                listviewInside.setOnDragListener(new InsertDragListener((Weight) o));
                row.setOnTouchListener(new ListGroupTouchListener((Weight) o, weight));
                fillList(listviewInside, inflater, (Weight) o);
                view.addView(row);
            } else if (o instanceof WeightCategory) {
                ConstraintLayout row = (ConstraintLayout) inflater.inflate(
                        R.layout.display_category_list_editable, view, false);
                TextView title = row.findViewById(R.id.txt_categoryList_title);
                title.setText(((WeightCategory) o).getCategory().getName());
                row.setOnTouchListener(new WeightCategoryTouchListener(((WeightCategory) o), weight));
                EditText edTxtValue = row.findViewById(R.id.txt_categoryList_value);
                edTxtValue.setText(String.valueOf(((WeightCategory) o).getValue()));
                edTxtValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().isEmpty()) {
                            if (editable.toString().matches("[1-9]+")) {
                                ((WeightCategory) o).setValue(Integer.parseInt(editable.toString()));

                            } else {
                                Toast.makeText(CreateWeightDialog.this,
                                        "bitte eine Gewichtung größer null eingeben",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                view.addView(row);

            }

        }
    }

    private final class TemplateGroupTouchListener implements View.OnTouchListener {
        Object o;

        TemplateGroupTouchListener(Object o) {
            this.o = o;
        }




        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {

            vibrator.vibrate(20);
            ClipData data = ClipData.newPlainText("weight", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadowBuilder, v, 0);
            }else{
                v.startDrag(data, shadowBuilder, v, 0);
            }
            return false;
        }
    }

    private final class ListGroupTouchListener implements View.OnTouchListener {
        Weight parent;
        Weight weight;

        ListGroupTouchListener(Weight weight, Weight parent) {
            this.weight = weight;
            this.parent = parent;
        }



        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
    vibrator.vibrate(20);
        ClipData data = ClipData.newPlainText("weight", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(data, shadowBuilder, v, 0);
        }else{
            v.startDrag(data, shadowBuilder, v, 0);
        }
            v.setVisibility(View.INVISIBLE);

            parent.remove(weight);
            return false;
    }     }


    class InsertDragListener implements View.OnDragListener {

        Weight weight;

        InsertDragListener(Weight weight) {
            this.weight = weight;
        }


        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.list_background_accent);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundResource(R.drawable.list_background);

                    break;
                case DragEvent.ACTION_DROP:

                    ClipData data = event.getClipData();
                    if (data.getDescription().getLabel().toString().matches("cate")) {
                        WeightCategory wcate = new WeightCategory();
                        wcate.setCategory(db.getCategory(Integer.parseInt(data.getItemAt(0).
                                getText().toString())));
                        wcate.setWeight(weight);
                        wcate.setValue(0);
                        weight.add(wcate);

                    } else {
                        Weight newWeight = new Weight();
                        newWeight.setWeightId(weight.getId());
                        newWeight.setValue(0);
                        weight.add(newWeight);
                    }
                    update();
                    db.displayTables();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundResource(R.drawable.list_background);
                default:
                    break;
            }
            return true;
        }
    }

    private final class WeightCategoryTouchListener implements View.OnTouchListener{
        Object o;
        WeightCategory wcate;
        Weight parent;

        public WeightCategoryTouchListener(WeightCategory category, Weight parent) {
            this.parent = parent;
            this.wcate = category;
        }



        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            vibrator.vibrate(20);
            ClipData data = ClipData.newPlainText("cate", "" + wcate.getCategory().getId());

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadowBuilder, v, 0);
            }else{
                v.startDrag(data, shadowBuilder, v, 0);
            }

            parent.remove(wcate);

            v.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    private boolean save(Weight topweight) {


        if (checkFilledData(topweight)) {
            int parentID = (int) db.insertWeight(topweight);

            for (Object o : topweight.getList()) {
                if (o instanceof Weight) {
                    ((Weight) o).setWeightId(parentID);
                    save(((Weight) o));
                } else if (o instanceof WeightCategory) {
                    ((WeightCategory) o).getWeight().setId(parentID);
                    db.insertWeigCategory((WeightCategory) o);
                }

            }

            return true;
        } else {
            Toast.makeText(this, "befülle alle Titel und vergebe Gewichtungen größer null",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean checkFilledData(Weight topweight) {

        if (topweight.getName() == "") {
            return false;
        }
        if (topweight.getValue() == 0) {
            return false;
        }
        if (topweight.getName() == null) {
            return false;
        }
        for (Object o : topweight.getList()) {
            if (o instanceof Weight) {
                return checkFilledData((Weight) o);
            } else if (o instanceof WeightCategory) {
                if (((WeightCategory) o).getValue() == 0) {
                    return false;
                }
            }
        }


        return true;
    }


}
