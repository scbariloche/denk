package de.thinkad.grade_it.gui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.activities.MainActivity;
import de.thinkad.grade_it.gui.dialogs.AddExamDialog;
import de.thinkad.grade_it.gui.dialogs.ExamListDialog;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.Helper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.pojos.StudentExam;

/**
 * Created by andreas on 25.08.2017.
 */

public class GradesFragment extends Fragment {
    LineChart chart;
    ImageButton btnAddGrade;
    DBHelper db;
    MainNotifier callback;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_grades, container, false);
        chart = view.findViewById(R.id.student_chart);
        btnAddGrade = view.findViewById(R.id.btn_add_grade);

        btnAddGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (db.getCurrentSubject() != null) {
                    AlertDialog.Builder alertBuilder = Helper.createAlert(getActivity(), "neue Prüfung eintragen?");

                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), AddExamDialog.class);
                            getActivity().startActivityForResult(intent, Constants.RESULTCODE_NEWEXAMFROMGRADE_FINISHED);
                        }
                    });


                    if (db.getCurrentSubject() != null) {
                        if (db.getAllExamsBySubjID(db.getCurrentSubject().getId()).size() > 0) {
                            alertBuilder.setNegativeButton("vorhandene bewerten", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DialogFragment gradeDialog = new ExamListDialog();

                                    gradeDialog.show(getFragmentManager(), "blablabla");
                                }
                            });

                        }
                    }
                    alertBuilder.show();

                } else {
                    Helper.createAlert(getActivity(), getString(R.string.addSubjectFirst)).show();
                }
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        update(getActivity());

    }

    public void update(Context context) {
        ((MainActivity) context).update();
        db = new DBHelper(context);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.");
        List<Entry> entries = new ArrayList<Entry>();
        if (db.getCurrentSubject() != null) {
            if (db.getAllStudentExamsBySubjId(db.getCurrentSubject().getId()).size() > 0) {
                for (StudentExam studExam : db.getAllStudentExamsBySubjId(db.getCurrentSubject().getId())) {
                    // turn your data into Entry objects
                    if (studExam.getGrade() != null) {
                        if (Integer.parseInt(studExam.getGrade()) > -1) {
                            float grade;

                            if (db.getCurrentStudent().getGradesys() == 0) {
                                grade = Float.parseFloat(Constants.getGrade(Integer.parseInt(studExam.getGrade()),
                                        db.getCurrentStudent().getGradesys()));
                            } else {
                                grade = Float.parseFloat(studExam.getGrade());
                            }

                            entries.add(new Entry(studExam.getExam().getDate(), grade, studExam));


                            LineDataSet dataSet = new LineDataSet(entries, "Noten"); // add entries to dataset
                            dataSet.setValueFormatter(new IValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                    DecimalFormat mFormat = new DecimalFormat("###,###,##0"); // use one decima
                                    return mFormat.format(value);
                                }
                            });
                            dataSet.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
                            dataSet.setValueTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                            dataSet.setValueTextSize(14);// styling, ...

                            chart.setDescription(null);

                            chart.getAxisRight().setEnabled(false);

                            XAxis xAxis = chart.getXAxis();
                            xAxis.setGranularity(7f);
                            xAxis.setDrawGridLines(false);
                            xAxis.setEnabled(true);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {

                                    return sdf.format(value);
                                }
                            });
                            xAxis.setLabelRotationAngle(35f);
                            xAxis.setAvoidFirstLastClipping(true);
                            YAxis yAxis = chart.getAxisLeft();
                            yAxis.setTextSize(12f);

                            // set the text size
                            if (db.getCurrentStudent().getGradesys() == 0) {
                                yAxis.setInverted(true);
                                yAxis.setAxisMinimum(0f);
                                yAxis.setAxisMaximum(7f);
                                yAxis.setValueFormatter(new IAxisValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, AxisBase axis) {
                                        String ret = "";
                                        if (value >= 1 & value <= 6) {
                                            ret = String.valueOf((int) value);
                                        }
                                        return ret;
                                    }
                                });
                            } else {
                                yAxis.setInverted(false);
                                yAxis.setAxisMinimum(-1f);
                                yAxis.setAxisMaximum(16f);
                                yAxis.setValueFormatter(new IAxisValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, AxisBase axis) {
                                        String ret = "";
                                        if (value >= 0 & value <= 15) {
                                            ret = String.valueOf((int) value);
                                        }
                                        return ret;
                                    }
                                });
                            }

                            yAxis.setTextColor(Color.BLACK);
                            yAxis.setGranularity(1f); // interval 1

                            yAxis.setDrawGridLines(false);
                            LineData lineData = new LineData(dataSet);
                            chart.setData(lineData);
                            chart.invalidate(); // refresh
                        }
                    }
                }
            }
        }
        chart.setNoDataText(getString(R.string.noGradeData));
        chart.setNoDataTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                StudentExam studexam = (StudentExam) e.getData();

                Snackbar snackbar = Snackbar
                        .make(chart, studexam.getExam().getCategory().getName() + " am " + sdf.format(studexam.getExam().getDate()), Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        chart.notifyDataSetChanged();
        chart.invalidate(); // refresh

    }


    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            callback = (MainNotifier) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
