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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Class BDDHelper.
 */
public class BDDHelper extends SQLiteOpenHelper {

    /**
     * The Constant TABLE_NAME1.
     */
    public static final String TABLE_NAME1 = "children";

    /**
     * The Constant COL_ID.
     */
    public static final String COL_ID = "ID";

    /**
     * The Constant COL_NAME1.
     */
    public static final String COL_NAME1 = "NAME";

    /**
     * The Constant TABLE_NAME2.
     */
    public static final String TABLE_NAME2 = "scolds";

    /**
     * The Constant COL_NAME2.
     */
    public static final String COL_NAME2 = "TEXT";

    /**
     * The Constant VERSION_BDD.
     */
    public static final int VERSION_BDD = 1;

    /**
     * The Constant NOM_BDD.
     */
    public static final String NOM_BDD = "ladamequigronde.db";

    /**
     * The Constant CREATE_BDD1.
     */
    private static final String CREATE_BDD1 = "CREATE TABLE " + TABLE_NAME1 + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME1 + " TEXT NOT NULL);";

    /**
     * The Constant CREATE_BDD2.
     */
    private static final String CREATE_BDD2 = "CREATE TABLE " + TABLE_NAME2 + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME2 + " TEXT NOT NULL);";

    /**
     * Instantiates a new bDD helper.
     *
     * @param context the context
     * @param name    the name
     * @param factory the factory
     * @param version the version
     */
    public BDDHelper(final Context context, final String name, final CursorFactory factory, final int version) {
        super(context, name, factory, version);
    }

    /**
     * On create.
     *
     * @param db the db
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        // on créé la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD1);
        db.execSQL(CREATE_BDD2);
    }

    /**
     * On upgrade.
     *
     * @param db         the db
     * @param oldVersion the old version
     * @param newVersion the new version
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        // comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_NAME1 + ";");
        db.execSQL("DROP TABLE " + TABLE_NAME2 + ";");
        onCreate(db);
    }

}