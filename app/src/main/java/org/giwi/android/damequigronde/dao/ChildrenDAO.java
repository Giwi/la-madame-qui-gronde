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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * The Class ChildrenDAO.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ChildrenDAO {

    /**
     * The bdd.
     */
    private static SQLiteDatabase bdd;

    /**
     * The ma base sq lite.
     */
    private final BDDHelper maBaseSQLite;

    /**
     * Instantiates a new children dao.
     *
     * @param context the context
     */
    public ChildrenDAO(final Context context) {
        // On créer la BDD et sa table
        maBaseSQLite = new BDDHelper(context, BDDHelper.NOM_BDD, null, BDDHelper.VERSION_BDD);
    }

    /**
     * Gets the bdd.
     *
     * @return the bdd
     */
    public SQLiteDatabase getBDD() {
        return bdd;
    }

    /**
     * Gets the list of children.
     *
     * @return the list of children
     */
    public List<String> getListOfChildren() {
        bdd = maBaseSQLite.getReadableDatabase();
        final Cursor cur = bdd.rawQuery("SELECT " + BDDHelper.COL_NAME1 + " from " + BDDHelper.TABLE_NAME1, new String[]{});

        final List<String> datas = new ArrayList<String>(cur.getCount());
        while (cur.moveToNext()) {
            datas.add(cur.getString(cur.getColumnIndex(BDDHelper.COL_NAME1)));
        }
        cur.close();
        bdd.close();

        return datas;
    }

    /**
     * Store child.
     *
     * @param child the child
     * @return the long
     * @throws JSONException the jSON exception
     * @throws IOException   Signals that an I/O exception has occurred.
     */
    public long storeChild(final String child) throws JSONException, IOException {
        bdd = maBaseSQLite.getWritableDatabase();
        final ContentValues values = new ContentValues();
        // on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la
        // valeur)
        values.put(BDDHelper.COL_NAME1, child);
        // on insère l'objet dans la BDD via le ContentValues
        final long val = bdd.insert(BDDHelper.TABLE_NAME1, null, values);
        bdd.close();
        return val;
    }

    /**
     * Removes the child.
     *
     * @param selectedItem the selected item
     * @return the string
     */
    public String removeChild(final String selectedItem) {
        bdd = maBaseSQLite.getWritableDatabase();
        bdd.delete(BDDHelper.TABLE_NAME1, BDDHelper.COL_NAME1 + "='" + selectedItem + "'", null);
        bdd.close();
        return selectedItem;
    }
}
