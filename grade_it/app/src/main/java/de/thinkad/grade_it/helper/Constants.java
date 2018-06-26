package de.thinkad.grade_it.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import de.thinkad.grade_it.pojos.Category;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.Student;
import de.thinkad.grade_it.pojos.StudentExam;
import de.thinkad.grade_it.pojos.StudentSubject;
import de.thinkad.grade_it.pojos.Subject;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andreas on 25.08.2017.
 */

public class Constants {


    public static String FRAGEMNT_CODE = "FRAGMENT_CODE";


    public static Map<String, String> points2Grade = new HashMap<>();
    public static Map<String, String> grade2Points = new HashMap<>();

    public static final int RESULTCODE_NEWEXAM_FINISHED = 10;
    public static final int RESULTCODE_NEWEXAMFROMGRADE_FINISHED = 15;
    public static final int INT_FRAGMENT_CODE_ADD_SUBJECT_DONE = 20;
    public static final int RESULTCODE_ONDATEPICKED = 25;
    public static final int RESULTCODE_ONWEIGHTPICKED = 30;
    public static final int RESULTCODE_CREATEWEIGHTFINISHED = 35;

    public Constants() {

        points2Grade.put("15", "1.0");
        points2Grade.put("14", "1.0");
        points2Grade.put("13", "1.3");
        points2Grade.put("12", "1.7");
        points2Grade.put("11", "2.0");
        points2Grade.put("10", "2.3");
        points2Grade.put("9", "2.7");
        points2Grade.put("8", "3.0");
        points2Grade.put("7", "3.3");
        points2Grade.put("6", "3.7");
        points2Grade.put("5", "4.0");
        points2Grade.put("4", "4.3");
        points2Grade.put("3", "4.7");
        points2Grade.put("2", "5.0");
        points2Grade.put("1", "5.3");
        points2Grade.put("0", "6.0");

        grade2Points.put("1", "14");
        grade2Points.put("2", "11");
        grade2Points.put("3", "8");
        grade2Points.put("4", "5");
        grade2Points.put("5", "2");
        grade2Points.put("6", "0");

    }

    public static Constants getInstance() {
        return new Constants();
    }

    public static String getGrade(int points, int gradesystem) {
        float a = 17 - points;
        float b = (float) ((a / 3) + 0.5);

        switch (gradesystem) {
            case 0:
                return String.valueOf((int) b);
            case 1:
                return String.valueOf(b);
            default:
                return String.valueOf(points);
        }

    }
}
