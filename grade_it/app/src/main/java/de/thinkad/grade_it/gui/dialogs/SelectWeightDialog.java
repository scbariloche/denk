package de.thinkad.grade_it.gui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.fragments.WeightFragment;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.callbacks.WeightFragmentCallback;
import de.thinkad.grade_it.pojos.Weight;

/**
 * Created by andreas on 14.11.2017.
 */

public class SelectWeightDialog extends FragmentActivity implements WeightFragmentCallback {

    private DBHelper db;
    private ArrayList<Weight> weightList = new ArrayList<>();
    ViewPager vPager;
    FrameLayout frameLeft;
    FrameLayout frameRight;
    TextView btnNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = new DBHelper(this);
        weightList = db.getAllTopWeights();
        setContentView(R.layout.dialog_select_weight);
        vPager = findViewById(R.id.vpager_weights);
        FragmentManager fm = getSupportFragmentManager();
        btnNew = findViewById(R.id.btn_select_weight_new);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(SelectWeightDialog.this, CreateWeightDialog.class),
                        Constants.RESULTCODE_CREATEWEIGHTFINISHED);
            }
        });


        frameLeft = findViewById(R.id.frame_left);
        frameRight = findViewById(R.id.frame_right);
        vPager.setAdapter(new WeightPagerAdapter(fm));
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                frameRight.setVisibility(View.VISIBLE);
                frameLeft.setVisibility(View.VISIBLE);
                if (position == 0) {
                    frameLeft.setVisibility(View.INVISIBLE);
                }
                if (position == weightList.size() - 1) {
                    frameRight.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int position= getIntent().getIntExtra("position", 0);
        vPager.setCurrentItem(getIntent().getIntExtra("position", 0), true);
        if (position == 0) {
            frameLeft.setVisibility(View.INVISIBLE);
        }
        if (position == weightList.size() - 1) {
            frameRight.setVisibility(View.INVISIBLE);
        }

        frameLeft.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (vPager.getCurrentItem() - 1 >= 0) {
                    vPager.setCurrentItem(vPager.getCurrentItem() - 1, true);
                } else {
                    vPager.setCurrentItem(weightList.size() - 1);
                }
            }
        });
        frameRight.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (vPager.getCurrentItem() + 1 < vPager.getAdapter().getCount())
                    vPager.setCurrentItem(vPager.getCurrentItem() + 1, true);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        vPager.setCurrentItem(getIntent().getIntExtra("position", 0), true);

    }

    @Override
    public void onWeightSelected(int weightId) {
        Intent resultIntent = getIntent();
        resultIntent.putExtra("weightID", weightId);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onWeightDeleted() {
        weightList = db.getAllTopWeights();
        vPager.setAdapter(new WeightPagerAdapter(getSupportFragmentManager()));
        int pos = getIntent().getIntExtra("position", 0);
        if (pos >= weightList.size()) {
            pos = 0;
        }
        vPager.setCurrentItem(pos, true);
    }


    private class WeightPagerAdapter extends FragmentStatePagerAdapter {
        public WeightPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment ret = new WeightFragment();
            Bundle args = new Bundle();
            args.putSerializable("weight", weightList.get(position));

            ret.setArguments(args);

            return ret;

        }

        @Override
        public int getCount() {
            return weightList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            weightList = db.getAllTopWeights();
            vPager.setAdapter(new WeightPagerAdapter(getSupportFragmentManager()));
            vPager.setCurrentItem(weightList.size() - 1, true);
        }
    }
}
