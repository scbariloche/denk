package de.thinkad.grade_it.gui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import de.thinkad.grade_it.gui.activities.MainActivity;

import static java.lang.Thread.sleep;

/**
 * Created by andreas on 14.11.2017.
 */

public class SplashScreen extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        };
        r.run();

    }

}
