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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.giwi.android.damequigronde.dao.Preferences;
import org.giwi.android.damequigronde.dao.SentencesDAO;
import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * The Class GrondageActivity.
 */
@EActivity(R.layout.grondage)
public class GrondageActivity extends ListActivity implements TextToSpeech.OnInitListener {
    /**
     * The constant TAG.
     */
    private static final String TAG = GrondageActivity.class.getSimpleName();
    /**
     * The cname.
     */
    @Extra("cname")
    String cname;

    /**
     * The gronderies.
     */
    @StringArrayRes(R.array.gronderies)
    String[] gronderies;
    /**
     * The Sentences dao.
     */
    @Bean
    SentencesDAO sentencesDAO;
    /**
     * The vol control.
     */
    @ViewById(R.id.volumeBar)
    SeekBar volControl;
    /**
     * Called when the activity is first created.
     */
    protected GrondageAdapter m_adapter;

    /**
     * The selected item.
     */
    protected String selectedItem;

    /**
     * The m tts.
     */
    private TextToSpeech mTts;

    /**
     * The grondages.
     */
    private final List<String> grondages = new ArrayList<>();

    /**
     * The audio manager.
     */
    private AudioManager audioManager;

    /**
     * Update.
     */
    @AfterViews
    protected void update() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(final SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(final SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(final SeekBar arg0, final int arg1, final boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });
        mTts = new TextToSpeech(this, this);
        final List<String> listOfS = sentencesDAO.getListOfSentences();

        if (listOfS.isEmpty()) {
            for (final String s : gronderies) {
                try {
                    sentencesDAO.storeSentence(s);
                } catch (final JSONException | IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                grondages.add(s.replaceAll("XXX", cname));
            }
        } else {
            for (final String s : listOfS) {
                grondages.add(s.replaceAll("XXX", cname));
            }
        }

        m_adapter = new GrondageAdapter(this, R.layout.row, grondages);
        setListAdapter(m_adapter);

        final ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View clickedItemView, final int position, final long id) {
                final ImageView imageView = (ImageView) clickedItemView.findViewById(R.id.icon);
                imageView.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                selectedItem = grondages.get(position);
                synthese(selectedItem, imageView);
            }
        });

    }

    /**
     * On init.
     *
     * @param status the status
     */
    @Override
    public void onInit(final int status) {
        if (status == TextToSpeech.SUCCESS) {
            final int result = mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    /**
     * Synthese.
     *
     * @param mess      the mess
     * @param imageView the image view
     */
    @UiThread
    void synthese(final String mess, final ImageView imageView) {
        Log.i(TAG, mess);
        mTts.speak(mess, TextToSpeech.QUEUE_FLUSH, null);
        while (mTts.isSpeaking()) {
            try {
                Thread.sleep(500);
            } catch (final InterruptedException e) {
                imageView.setImageResource(android.R.drawable.ic_btn_speak_now);
            }
        }
        imageView.setImageResource(android.R.drawable.ic_btn_speak_now);
    }

    /**
     * The Class GrondageAdapter.
     */
    private class GrondageAdapter extends ArrayAdapter<String> {

        /**
         * The items.
         */
        private final List<String> items;

        /**
         * Instantiates a new grondage adapter.
         *
         * @param context            the context
         * @param textViewResourceId the text view resource id
         * @param items              the items
         */
        public GrondageAdapter(final Context context, final int textViewResourceId, final List<String> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        /**
         * Gets view.
         *
         * @param position    the position
         * @param convertView the convert view
         * @param parent      the parent
         * @return the view
         */
        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                final LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            final String o = items.get(position);
            if (o != null) {
                final TextView tt = (TextView) v.findViewById(R.id.toptext);
                tt.setText(o);
            }
            return v;
        }

    }

    /**
     * On destroy.
     */
    @Override
    protected void onDestroy() {
        // Close the Text to Speech Library
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }
        super.onDestroy();
    }

}
