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
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * The Class AboutActivity.
 */
@EActivity(R.layout.about)
public class AboutActivity extends Activity {

    /**
     * The t2.
     */
    @ViewById(R.id.textView2)
    TextView t2;

    /**
     * Update.
     */
    @AfterViews
    protected void update() {
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
