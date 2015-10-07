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
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.giwi.android.damequigronde.dao.Preferences_;
import org.giwi.android.damequigronde.dao.SentencesDAO;
import org.giwi.android.damequigronde.tools.HTTPTracker;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * The Class ManageSentencesActivity.
 */
@EActivity(R.layout.managesentences)
public class ManageSentencesActivity extends Activity {

    /**
     * The Constant TAG.
     */
    private static final String TAG = ManageSentencesActivity.class.getSimpleName();

    /**
     * The prefs.
     */
    @Pref
    Preferences_ prefs;
    /**
     * The Http tracker.
     */
    @Bean
    HTTPTracker httpTracker;
    /**
     * The Sentences dao.
     */
    @Bean
    SentencesDAO sentencesDAO;
    /**
     * The list of c.
     */
    @ViewById(R.id.listOfSentencesMgt)
    Spinner listOfC;

    /**
     * The gronderies.
     */
    @StringArrayRes(R.array.gronderies)
    String[] gronderies;

    /**
     * The sentence_mgnt_store.
     */
    @StringRes(R.string.sentence_mgnt_store)
    String sentence_mgnt_store;

    /**
     * The sentence_mgnt_del.
     */
    @StringRes(R.string.sentence_mgnt_del)
    String sentence_mgnt_del;

    /**
     * The sentence to add.
     */
    @ViewById(R.id.sentenceToAdd)
    EditText sentenceToAdd;

    /**
     * The adapter.
     */
    private ArrayAdapter<String> adapter;

    /**
     * Update.
     */
    @AfterViews
    void update() {
        final List<String> listOfS = sentencesDAO.getListOfSentences();
        if (listOfS.isEmpty()) {
            for (final String s : gronderies) {
                try {
                    sentencesDAO.storeSentence(s);
                } catch (final JSONException | IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sentencesDAO.getListOfSentences());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfC.setAdapter(adapter);
    }

    /**
     * On add click.
     */
    @Click(R.id.add)
    void onAddClick() {
        final String name = sentenceToAdd.getText().toString();
        adapter.add(name);
        try {
            sentencesDAO.storeSentence(name);
            if (prefs.collectdata().getOr(false)) {
                sendData(name);
            }
        } catch (final JSONException | IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        final int spinnerPosition = adapter.getPosition(name);
        // set the default according to value
        listOfC.setSelection(spinnerPosition);
        sentenceToAdd.setText("");
        Toast.makeText(getApplicationContext(), sentence_mgnt_store + " " + name, Toast.LENGTH_LONG).show();
    }

    /**
     * Send data.
     *
     * @param name the name
     */
    @Background
    void sendData(String name) {
        try {
            httpTracker.storeSentences(name);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * On del c click.
     */
    @Click(R.id.remove)
    void onDelCClick() {
        final String name = sentencesDAO.removeSentences((String) listOfC.getSelectedItem());
        adapter.remove(name);
        Toast.makeText(getApplicationContext(), sentence_mgnt_del + " " + name, Toast.LENGTH_LONG).show();
    }
}
