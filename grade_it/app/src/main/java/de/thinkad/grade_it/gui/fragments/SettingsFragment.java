package de.thinkad.grade_it.gui.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.activities.MainActivity;
import de.thinkad.grade_it.gui.dialogs.AddSubjectDialog;
import de.thinkad.grade_it.gui.dialogs.ImprintDialog;
import de.thinkad.grade_it.gui.dialogs.SelectWeightDialog;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.modeladapter.spinner.WeightSpinnerAdapter;
import de.thinkad.grade_it.pojos.Student;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andreas on 25.08.2017.
 */

public class SettingsFragment extends Fragment {

    DBHelper db;
    Switch switchGradesys;
    Spinner spFavWeight;
    ArrayList<Weight> topWeights;
    TextView tvImprint;
    TextView btnSettingsWeights;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        db = new DBHelper(getActivity());
        switchGradesys = view.findViewById(R.id.switch_gradesys);
        switchGradesys.setChecked(db.getCurrentStudent().getGradesys() == 0);
        switchGradesys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int sys = isChecked ? 0 : 1;
                db.insertStudent(new Student(db.getCurrentStudent().getId(), "andi denk", sys));
                ((MainActivity) getActivity()).notifyDataChanged();
            }
        });
        btnSettingsWeights= view.findViewById(R.id.btn_settings_weights);
        btnSettingsWeights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectWeightDialog.class);
                ArrayList<Weight> allTopWeights = db.getAllTopWeights();
                int position = 0;
                for (int i = 0; i < allTopWeights.size(); i++) {
                    if (allTopWeights.get(i).getId() == db.getFavouriteWeightId()) {
                        position = i;
                    }
                }
                intent.putExtra("position", position);
                intent.putExtra("disableChoose",true);
                startActivityForResult(intent, Constants.RESULTCODE_ONWEIGHTPICKED);

            }
        });


        tvImprint = view.findViewById(R.id.tv_settings_imprint);
        tvImprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ImprintDialog.class));
            }
        });

        topWeights = db.getAllTopWeights();
        spFavWeight = view.findViewById(R.id.spinner_settings);
        WeightSpinnerAdapter adapter = new WeightSpinnerAdapter(getActivity(), topWeights);

        spFavWeight.setAdapter(adapter);
        for (int i = 0; i < topWeights.size(); i++) {
            if (topWeights.get(i).getId() == db.getFavouriteWeightId()) {
                spFavWeight.setSelection(i);
            }
        }
        spFavWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                db.setFavouriteWeightId(topWeights.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
