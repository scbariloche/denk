package de.thinkad.grade_it.gui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.dialogs.CreateWeightDialog;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.helper.callbacks.OnValueClick;
import de.thinkad.grade_it.helper.callbacks.WeightFragmentCallback;
import de.thinkad.grade_it.pojos.Weight;
import de.thinkad.grade_it.pojos.WeightCategory;

/**
 * Created by andreas on 19.10.2017.
 */

public class WeightFragment extends Fragment implements OnValueClick {


    private DBHelper db;
    LinearLayout outerGroup;
    private TextView btnSelectWeight;
    private TextView title;
    private TextView btnEdit;
    private TextView btnDelete;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(getActivity());
        View view = inflater.inflate(R.layout.fragment_weight, container, false);

        final Weight weight = (Weight) getArguments().getSerializable("weight");
        btnSelectWeight = view.findViewById(R.id.btn_ok_weight);
        btnEdit = view.findViewById(R.id.btn_edit_weight);
        btnDelete = view.findViewById(R.id.btn_delete_weight);
        title = view.findViewById(R.id.tv_create_weight_title);
        title.setText(weight.getName());

        if (getActivity().getIntent().getBooleanExtra("disableChoose", false)) {
            btnSelectWeight.setVisibility(View.GONE);
        }


//        list below
        outerGroup = view.findViewById(R.id.container_newWeight_list);
        final Weight builtWeight = Helper.buildWeight(db, weight.getId());
        fillList(outerGroup, LayoutInflater.from(getActivity()), builtWeight.getList());

        btnSelectWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WeightFragmentCallback) getActivity()).onWeightSelected(weight.getId());
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("topweight", weight.getId());
                Intent intent = new Intent(getActivity(), CreateWeightDialog.class);
                intent.putExtras(args);
                startActivityForResult(intent,
                        Constants.RESULTCODE_CREATEWEIGHTFINISHED);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getAllTopWeights().size() >= 2) {


                    Helper.createAlert(getActivity(), getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Helper.deleteWeight(db, builtWeight);
                            ((WeightFragmentCallback) getActivity()).onWeightDeleted();


                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else {
Helper.createAlert(getActivity(),getString(R.string.delete_last)).show();
                }
            }
        });


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.height = ((int) (height * 0.95));
        params.width = ((int) (width * 0.98));
        getActivity().getWindow().setAttributes(params);


        return view;
    }

    private void fillList(LinearLayout view, LayoutInflater inflater, List<Object> list) {
        for (final Object o : list) {
            if (o instanceof Weight) {
                LinearLayout row = (LinearLayout) inflater.inflate(R.layout.display_weight_outergroup, view, false);
                TextView tv_value = row.findViewById(R.id.display_weightBuilder_value);
                TextView title = row.findViewById(R.id.txt_btn_weightBuilder_addGroup);
                title.setText(((Weight) o).getName());

                tv_value.setText("" + ((Weight) o).getValue());

                fillList((LinearLayout) row.findViewById(R.id.display_weightBuilder_list), inflater, ((Weight) o).getList());
                view.addView(row);
            } else if (o instanceof WeightCategory) {
                ConstraintLayout row = (ConstraintLayout) inflater.inflate(R.layout.display_category_list, view, false);
                TextView title = row.findViewById(R.id.txt_categoryList_title);
                title.setText(((WeightCategory) o).getCategory().getName());

                TextView tv_value = row.findViewById(R.id.txt_categoryList_value);
                tv_value.setText("" + ((WeightCategory) o).getValue());
                view.addView(row);

            }

        }
    }

    @Override
    public void onWeightClick(Weight weight) {

    }

    @Override
    public void onWeightCategoryClick(WeightCategory weigCate) {

    }
}
