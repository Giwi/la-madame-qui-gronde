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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.HtmlRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.giwi.android.damequigronde.dao.ChildrenDAO;
import org.giwi.android.damequigronde.dao.Preferences_;


/**
 * The Class DameQuiGrondeActivity.
 */
// @NoTitle
@EActivity(R.layout.main)
@OptionsMenu(R.menu.option_menu)
public class DameQuiGrondeActivity extends Activity {

    /**
     * The prefs.
     */
    @Pref
    Preferences_ prefs;

    /**
     * The list of c.
     */
    @ViewById(R.id.mainListOfC)
    Spinner listOfC;

    /**
     * The no child mess.
     */
    @StringRes(R.string.no_child)
    String noChildMess;

    /**
     * The alert_dialog_cancel.
     */
    @StringRes(R.string.alert_dialog_cancel)
    String alert_dialog_cancel;

    /**
     * The alert_dialog_ok.
     */
    @StringRes(R.string.alert_dialog_ok)
    String alert_dialog_ok;

    /**
     * The alert_dialog_mess.
     */
    @HtmlRes(R.string.alert_dialog_mess)
    CharSequence alert_dialog_mess;

    /**
     * The alert_dialog_title.
     */
    @StringRes(R.string.alert_dialog_title)
    String alert_dialog_title;

    /**
     * The diag color.
     */
    @ColorRes(android.R.color.primary_text_light)
    int diagColor;

    /**
     * The dao.
     */
    private ChildrenDAO dao;

    /**
     * The adapter.
     */
    private ArrayAdapter<String> adapter;

    /**
     * Button play.
     *
     * @param view the view
     */
    @Click(R.id.button1)
    void buttonPlay(final View view) {
        final String name = (String) listOfC.getSelectedItem();
        if (name != null) {
            GrondageActivity_.intent(view.getContext()).cname(name).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        } else {
            Toast.makeText(getApplicationContext(), noChildMess, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @AfterViews
    void update() {
        dao = new ChildrenDAO(getApplicationContext());
        // Créer un adapter avec une liste de String
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dao.getListOfChildren());
        // Définir le style des éléments de l'adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Assigner l'adapter à la vue
        listOfC.setAdapter(adapter);

        final boolean accepteDone = prefs.accepteDone().getOr(false);
        if (!accepteDone) {
            final AlertDialog.Builder b = new AlertDialog.Builder(DameQuiGrondeActivity.this);
            b.setTitle(alert_dialog_title);
            final TextView wv = new TextView(getApplicationContext());
            wv.setText(alert_dialog_mess.toString());
            b.setView(wv);
            wv.setTextColor(diagColor);
            b.setPositiveButton(alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            final int whichButton) {
                            prefs.accepteDone().put(true);
                            prefs.collectdata().put(true);
                            dialog.cancel();
                        }
                    });
            b.setNegativeButton(alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            final int whichButton) {
                            // pas collecter
                            prefs.collectdata().put(false);
                            prefs.accepteDone().put(true);
                            dialog.cancel();
                        }
                    }).create().show();

        }
    }

    /**
     * On resume.
     */
    protected void onResume() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dao.getListOfChildren());
        // Définir le style des éléments de l'adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Assigner l'adapter à la vue
        listOfC.setAdapter(adapter);
        super.onResume();
    }

    /**
     * Manage children.
     *
     * @return true, if successful
     */
    @OptionsItem(R.id.manageChildren)
    boolean manageChildren() {
        ManageChildrenActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        return true;
    }

    /**
     * Menu manage sentences.
     *
     * @return true, if successful
     */
    @OptionsItem(R.id.menuManageSentences)
    boolean menuManageSentences() {
        ManageSentencesActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        return true;
    }

    /**
     * About.
     *
     * @return true, if successful
     */
    @OptionsItem(R.id.about)
    boolean about() {
        AboutActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        return true;
    }
}