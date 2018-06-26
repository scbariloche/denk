package de.thinkad.grade_it.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.gui.dialogs.AddGradeDialog;
import de.thinkad.grade_it.gui.dialogs.AddSubjectDialog;
import de.thinkad.grade_it.gui.dialogs.SplashScreen;
import de.thinkad.grade_it.gui.fragments.ExamFragment;
import de.thinkad.grade_it.gui.fragments.GradesFragment;
import de.thinkad.grade_it.gui.fragments.SettingsFragment;
import de.thinkad.grade_it.helper.Constants;
import de.thinkad.grade_it.helper.DBHelper;
import de.thinkad.grade_it.helper.callbacks.MainNotifier;
import de.thinkad.grade_it.modeladapter.recyclerview.SubjectListAdapter;
import de.thinkad.grade_it.pojos.Exam;

public class MainActivity extends FragmentActivity implements MainNotifier {

//    upload 20.11.2017

    View subjectListContainer;
    RecyclerView subjectListView;
    DBHelper db = new DBHelper(this);
    static int pagerItem;
    static BottomNavigationView navigation;
    ImageButton btnAddSubject;
    TextView lblNoSubjects;
    private boolean isBackPressed = false;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private MenuItem prevMenuItem;
    AdView adview;
    private static int subjListPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper mydb = new DBHelper(this);
        mydb.upgradeDB(mydb.getWritableDatabase(), mydb.getdbVersion());
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,
                "ca-app-pub-2068969811639051~2808878515");
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        btnAddSubject = (ImageButton) findViewById(R.id.btn_main_addSubject);
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSubjectDialog.class);
                startActivityForResult(intent, Constants.INT_FRAGMENT_CODE_ADD_SUBJECT_DONE);

            }
        });
        subjectListContainer = findViewById(R.id.container_subject_list);
        subjectListView = subjectListContainer.findViewById(R.id.subject_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        subjectListView.setLayoutManager(layoutManager);

        mPager = (ViewPager) findViewById(R.id.main_fragment_container);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }

                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState == null) {
            pagerItem = 0;
            mPager.setCurrentItem(pagerItem, true);
        }
        notifyDataChanged();

        adview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

//        startActivity(new Intent(this, SplashScreen.class));


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_grades:
                    mPager.setCurrentItem(0, true);
                    break;
                case R.id.navigation_exams:
                    mPager.setCurrentItem(1, true);
                    break;
                case R.id.navigation_settings:
                    mPager.setCurrentItem(2, true);
                    db.displayTables();
                    break;
            }
            return false;
        }

    };


    public void update() {
        ArrayList subjList = db.getStudentSubjectsByStudentId(db.getCurrentStudent().getId());
        lblNoSubjects = (TextView) findViewById(R.id.txt_main_list_noSubjects);
        if (subjList.size() < 1) {
            lblNoSubjects.setText(getString(R.string.noSubjectData));
        } else {
            lblNoSubjects.setText("");
        }
        SubjectListAdapter adapter = new SubjectListAdapter(this, subjList,
                this);
        subjectListView.setAdapter(adapter);
        subjectListView.scrollToPosition(subjListPosition);

    }


    @Override
    public void onExamChosen(Exam exam) {
        AddGradeDialog gradeDialog = new AddGradeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("exam", exam);
        gradeDialog.setArguments(bundle);
        gradeDialog.show(getFragmentManager(), "");
    }

    @Override
    public void onGradeAdded() {
        pagerItem = mPager.getCurrentItem();
        mPager.setCurrentItem(pagerItem, true);
        notifyDataChanged();
    }

    @Override
    public void onSubjectListChanged() {

    }

    @Override
    public void subjectListScrollTo(int position) {
        this.subjListPosition = position;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {


            switch (requestCode) {
                case Constants.RESULTCODE_NEWEXAMFROMGRADE_FINISHED:
                    Exam exam = (Exam) data.getSerializableExtra("exam");
                    onExamChosen(exam);
                    break;
                case Constants.INT_FRAGMENT_CODE_ADD_SUBJECT_DONE:
                    onSubjectListChanged();
                    notifyDataChanged();
                    subjectListView.scrollToPosition(subjListPosition);
                    break;

                case Constants.RESULTCODE_NEWEXAM_FINISHED:

                default:
                    notifyDataChanged();

            }

        }
    }

    @Override
    public void notifyDataChanged() {
        pagerItem = mPager.getCurrentItem();
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(pagerItem);
        update();
    }

    @Override
    public void onBackPressed() {


        if (!isBackPressed) {
            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 2000);
            isBackPressed = true;
            Toast.makeText(getApplicationContext(), getString(R.string.doubletap2close), Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new GradesFragment();
                case 1:
                    return new ExamFragment();
                case 2:
                    return new SettingsFragment();
                default:
                    return new GradesFragment();
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}

