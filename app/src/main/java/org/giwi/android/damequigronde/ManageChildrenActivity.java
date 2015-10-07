/*************************************************************************
 * Giwi Softwares CONFIDENTIAL
 * __________________
 * <p/>
 * [2002] - [2013] Giwi Softwares
 * All Rights Reserved.
 * <p/>
 * NOTICE:  All information contained here is, and remains
 * the property of Giwi Softwares and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * here are proprietary to Giwi Softwares
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Giwi Softwares.
 */
package org.giwi.android.damequigronde;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.giwi.android.damequigronde.dao.ChildrenDAO;
import org.giwi.android.damequigronde.dao.Preferences_;
import org.giwi.android.damequigronde.tools.HTTPTracker;
import org.json.JSONException;

import java.io.IOException;


/**
 * The Class ManageChildrenActivity.
 */
@EActivity(R.layout.managechildren)
public class ManageChildrenActivity extends Activity {

    /**
     * The Constant TAG.
     */
    private static final String TAG = ManageChildrenActivity.class.getSimpleName();

    /**
     * The prefs.
     */
    @Pref
    Preferences_ prefs;

    /**
     * The list of c.
     */
    @ViewById(R.id.listOfChildrenMgt)
    Spinner listOfC;

    /**
     * The child to add.
     */
    @ViewById(R.id.childToAdd)
    EditText childToAdd;

    /**
     * The child_mgnt_store.
     */
    @StringRes(R.string.child_mgnt_store)
    String child_mgnt_store;

    /**
     * The child_mgnt_del.
     */
    @StringRes(R.string.child_mgnt_del)
    String child_mgnt_del;

    /**
     * The adapter.
     */
    private ArrayAdapter<String> adapter;
    /**
     * The Http tracker.
     */
    @Bean
    HTTPTracker httpTracker;
    /**
     * The dao.
     */
    @Bean
    ChildrenDAO childrenDAO;

    /**
     * On add click.
     */
    @Click(R.id.add)
    void onAddClick() {
        final String name = childToAdd.getText().toString();
        adapter.add(name);
        try {
            childrenDAO.storeChild(name);
            if (prefs.collectdata().getOr(false)) {
                sendData(name);
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        final int spinnerPosition = adapter.getPosition(name);
        listOfC.setSelection(spinnerPosition);
        childToAdd.setText("");
        Toast.makeText(getApplicationContext(), child_mgnt_store + " " + name,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Send data.
     *
     * @param name the name
     */
    @Background
    void sendData(String name) {
        try {
            httpTracker.storeChild(name);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * On del click.
     */
    @Click(R.id.remove)
    void onDelClick() {
        final String name = childrenDAO.removeChild((String) listOfC.getSelectedItem());
        adapter.remove(name);
        Toast.makeText(getApplicationContext(), child_mgnt_del + " " + name, Toast.LENGTH_LONG).show();
    }

    /**
     * Called when the activity is first created.
     */
    @AfterViews
    void update() {
        childrenDAO = new ChildrenDAO(getApplicationContext());
        // Créer un adapter avec une liste de String
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, childrenDAO.getListOfChildren());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Assigner l'adapter à la vue
        listOfC.setAdapter(adapter);
    }

}
