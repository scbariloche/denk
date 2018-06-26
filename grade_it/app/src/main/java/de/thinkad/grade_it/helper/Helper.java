package de.thinkad.grade_it.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.pojos.Weight;
import de.thinkad.grade_it.pojos.WeightCategory;

/**
 * Created by andreas on 31.08.2017.
 */

public class Helper {


    public static AlertDialog.Builder createAlert(Context context, String message, DialogInterface.OnClickListener yesAction, DialogInterface.OnClickListener noAction) {
        return createAlert(context, message, yesAction).setNegativeButton(android.R.string.no, noAction);
    }


    public static AlertDialog.Builder createAlert(Context context, String message, DialogInterface.OnClickListener yesAction) {
        return createAlert(context, message).setPositiveButton(android.R.string.yes, yesAction);
    }


    public static AlertDialog.Builder createAlert(Context context, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.grade_it_alert_dialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder;
    }

    public static AlertDialog.Builder createAlert(Context context, String message, String yesOptionString, DialogInterface.OnClickListener yesAction, String noOptionString, DialogInterface.OnClickListener noAction) {
        return createAlert(context, message).setPositiveButton(yesOptionString, yesAction).setNegativeButton(noOptionString, noAction);
    }

    public static Weight buildWeight(DBHelper db, int topWeightId) {
        Weight ret = db.getWeight(topWeightId);
        List<Weight> weightList = db.getAllWeightsByWeigWeigId(topWeightId);
        for (Weight w : weightList) {
            ret.add(buildWeight(db, w.getId()));
        }
        ret.addAll(db.getAllWeigCateByWeigId(topWeightId));
        return ret;
    }

    public static void deleteWeight(DBHelper db, Weight topweight) {
        db.delete(DBHelper.WEIGHT_TABLE, new String[]{DBHelper.WEIG_ID},
                new String[]{String.valueOf(topweight.getId())});
        for (Object o : topweight.getList()) {

            if (o instanceof Weight) {
                deleteWeight(db, (Weight) o);
            } else if (o instanceof WeightCategory) {
                db.delete(DBHelper.WEIGHT_CATEGORY_TABLE, new String[]{DBHelper.WEIG_ID, DBHelper.CATE_ID},
                        new String[]{String.valueOf(((WeightCategory) o).getWeight().getId()),
                                String.valueOf(((WeightCategory) o).getCategory().getId())});
            }

        }

    }

}