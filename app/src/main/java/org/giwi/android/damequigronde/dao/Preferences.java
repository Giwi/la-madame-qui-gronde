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
package org.giwi.android.damequigronde.dao;


import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * The Interface Preferences.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    /**
     * Accepte done.
     *
     * @return true, if successful
     */
    @DefaultBoolean(false)
    boolean accepteDone();

    /**
     * Collectdata.
     *
     * @return true, if successful
     */
    @DefaultBoolean(false)
    boolean collectdata();
}
