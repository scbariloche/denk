package de.thinkad.grade_it.gui.dialogs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.thinkad.grade_it.R;
import de.thinkad.grade_it.modeladapter.expandableListview.ExpandableListViewAdapter;

/**
 * Created by andreas on 14.11.2017.
 */

public class ImprintDialog extends FragmentActivity {
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_imprint);
        expListView = findViewById(R.id.explv_imprint);
        prepareListData();
        expListView.setAdapter(new ExpandableListViewAdapter(this, listDataHeader, listDataChild));

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
        listDataHeader.add("über mich");
        listDataHeader.add("Lizenzen");
        // Adding child data
        List<String> about = new ArrayList<String>();
        about.add("");

        List<String> licences = new ArrayList<String>();
        licences.add("------QuadFlask ColorPicker------" +
                "\n\nCopyright 2014-2017 QuadFlask\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");" +
                "you may not use this file except in compliance with the License." +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software" +
                "distributed under the License is distributed on an \"AS IS\" BASIS," +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied." +
                "See the License for the specific language governing permissions and" +
                "limitations under the License.");
        licences.add("------MPAndroidChart------" +
                "\n\nCopyright 2016 Philipp Jahoda\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\"); you may " +
                "not use this file except in compliance with the License. You may obtain a " +
                "copy of the License at\n" +
                "\n" +
                "http://www.apache.org/licenses/LICENSE-2.0\n" +
                "Unless required by applicable law or agreed to in writing, software distributed " +
                "under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR " +
                "CONDITIONS OF ANY KIND, either express or implied. See the License for the " +
                "specific language governing permissions and limitations under the License.");
        listDataChild.put(listDataHeader.get(0), about); // Header, Child data
        listDataChild.put(listDataHeader.get(1), licences);
    }
}
