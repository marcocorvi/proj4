/* @file Proj4OpenHelper.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief Proj4 SQLite "CRS" database open helper
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
// import android.content.ContentValues;
import android.database.Cursor;
// import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
// import android.database.sqlite.SQLiteDiskIOException;

import android.util.Log;

public class Proj4OpenHelper extends SQLiteOpenHelper
{
  private String DB_NAME = "proj4.sqlite";
  private String DB_PATH = "";
  private String DB_VERSION = "1";

  private Context mContext;
  public SQLiteDatabase mDB;

  Proj4OpenHelper(Context context ) 
  {
    super(context, "proj4.sqlite", null, 1); // 1 = DB version
    mContext = context;
    if ( android.os.Build.VERSION.SDK_INT >= 17 ) {
      DB_PATH = mContext.getApplicationInfo().dataDir + "/databases/";
    } else {
      DB_PATH = "/data/data/" + mContext.getPackageName() + "/databases/";
    }
    createDatabase();
  }

  public void createDatabase()
  {
    boolean exists = checkDatabase();
    // Log.v("Proj4", "CREATE database exists " + exists );
    if ( exists ) {
      exists = false;
      if ( openDatabase() ) {
        // check version
        Cursor cursor = mDB.query( "config",
                                   new String[] { "value" }, // columns
                                   "key=?", new String[] {"version"}, null, null, null );
        if (cursor.moveToFirst()) {
          exists = ( DB_VERSION.equals( cursor.getString(0) ) );
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        closeDatabase();
      }
    } else {
      getReadableDatabase();
      close();
    }
    if ( ! exists ) {
      copyDatabase();
    }
  }

  public synchronized void close()
  {
    closeDatabase();
    super.close();
  }

  // ----------------------------------------------------------------------------------

  @Override
  public void onUpgrade( SQLiteDatabase db, int old, int current ) { }

  @Override
  public void onCreate( SQLiteDatabase db ) { }
  

  // ----------------------------------------------------------------------------------

  boolean openDatabase()
  {
    String path = DB_PATH + DB_NAME;
    // Log.v("Proj4", "OPEN database " + path );
    mDB = null;
    try {
      // mDB = SQLiteDatabase.openDatabase( path, null, SQLiteDatabase.CREATE_IF_NECESSARY );
      mDB = SQLiteDatabase.openDatabase( path, null, SQLiteDatabase.OPEN_READWRITE );
    } catch ( SQLiteException e ) {
      Log.v("Proj4", "open error " + e.getMessage() );
    }
    return mDB != null;
  }

  private void closeDatabase()
  {
    if ( mDB != null ) mDB.close();
  }

  private void copyDatabase() 
  {
    String path = DB_PATH + DB_NAME;
    Log.v("Proj4", "COPY database " + path );
    try {
      InputStream is = mContext.getAssets().open( DB_NAME );
      FileOutputStream os = new FileOutputStream( path );
      byte[] buf = new byte[1024];
      int len;
      while ( ( len = is.read(buf) ) > 0 ) { os.write(buf, 0, len); }
      os.flush();
      os.close();
      is.close();
    } catch ( IOException e ) {
      Log.v("Proj4", "copy error " + e.getMessage() );
    }
  }

  private boolean checkDatabase()
  { 
    File file = new File( DB_PATH + DB_NAME );
    return file.exists();
  }

  // private static final String create_table = "CREATE TABLE IF NOT EXISTS ";
  //
  // private void createTables( SQLiteDatabase db )
  // {
  //    db.setLockingEnabled( false );
  //    db.beginTransaction();
  //    try {
  //      db.execSQL(
  //          create_table + CONFIG_TABLE
  //        + " ( key TEXT,"
  //        +   " value TEXT "
  //        +   ")"
  //      );

  //      db.execSQL(
  //          create_table + CRS_TABLE
  //        + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, "
  //        +   " name TEXT, "
  //        +   " desc TEXT, "
  //        +   " digits INTEGER "
  //        +   ")"
  //      );

  //      db.execSQL(
  //          create_table + COUNTRY_TABLE 
  //        + " ( code TEXT, " // PRIMARY KEY AUTOINCREMENT, "
  //        +   " name TEXT "
  //        +   ")"
  //      );

  //      db.execSQL(
  //          create_table + COUNTRY_CRS_TABLE
  //        + " ( country TEXT, "
  //        +   " crs INTEGER "
  //        +   ")"
  //      );

  //      db.setTransactionSuccessful();
  //    } catch ( SQLException e ) {
  //      Log.v("Proj4", "createTables exception " + e.toString() );
  //    } finally {
  //      db.endTransaction();
  //      db.setLockingEnabled( true );
  //    }
  // }

}
