/* @file CRSdatabase.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief Proj4 SQLite "CRS" database
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.DataSetObservable;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDiskIOException;

import android.widget.Toast;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;

public class CRSdatabase extends DataSetObservable
{

  static final String DB_VERSION = "10";
  static final int DATABASE_VERSION = 10;

  private static final String CRS_TABLE     = "crs";
  private static final String COUNTRY_TABLE = "country";
  private static final String COUNTRY_CRS_TABLE = "relation";


  private Proj4OpenHelper mHelper = null;
  private SQLiteDatabase myDB = null;

  // ----------------------------------------------------------------------
  // DATABASE

  private Context mContext;

  public SQLiteDatabase getDb() { return myDB; }

  public CRSdatabase( Context context )
  {
    mContext = context;
    openDatabase();
  }

  public void closeDatabase()
  {
    mHelper.close();
    mHelper = null;
    myDB    = null;
  }


  public void openDatabase() 
  {
    mHelper = new Proj4OpenHelper( mContext );
    mHelper.openDatabase();
    // mHelper.close();
    mHelper.getReadableDatabase();
    myDB = mHelper.mDB;
    if ( myDB == null ) {
      Log.v("Proj4", "failed get readable database " );
      return;
    }
    // while ( myDB.isDbLockedByOtherThreads() ) {
    //   try {
    //     Thread.sleep( 200 );
    //   } catch ( InterruptedException e ) {}
    // }
  }
  
  // ----------------------------------------------------------------------
   // country_code two-char iso code
   public ArrayList<CRS> getAllCRSs( String country_code )
   {
     ArrayList< CRS > list = new ArrayList< CRS >();
     // if ( myDB == null ) return list;
     Cursor cursor = myDB.query( COUNTRY_CRS_TABLE,
                                new String[] { "crs" }, // columns: CRS id
                                "country=?",
                                new String[] { country_code },
                                null,  // groupBy
                                null,  // having
                                null ); // order by
     if (cursor.moveToFirst()) {
       do {
         CRS crs = getCRS( cursor.getLong(0) );
         if ( crs != null ) list.add( crs );
       } while (cursor.moveToNext());
     }
     if (cursor != null && !cursor.isClosed()) cursor.close();
     return list;
   }

   public String getCountryName( String code )
   {
     if ( code == null ) return null;
     String ret = null;
     Cursor cursor = myDB.query( COUNTRY_TABLE,
                                 new String[] { "name" },
                                 "code=?", new String[] { code }, null, null, null );
     if (cursor.moveToFirst()) {
       ret = cursor.getString(0);
     }
     if (cursor != null && !cursor.isClosed()) cursor.close();
     return ret;
   }

   public ArrayList< String > getCountryNames()
   {
     ArrayList< String > ret = new ArrayList<String>();
     Cursor cursor = myDB.query( COUNTRY_TABLE,
                                 new String[] { "name" },
                                 null, null, null, null, null );
     if (cursor.moveToFirst()) {
       do {
         ret.add( cursor.getString(0) );
       } while (cursor.moveToNext());
     }
     if (cursor != null && !cursor.isClosed()) cursor.close();
     return ret;
   }

   public String getCountryCode( String name )
   {
     String ret = null;
     Cursor cursor = myDB.query( COUNTRY_TABLE,
                                 new String[] { "code" },
                                 "name=?", new String[] { name }, null, null, null );
     if (cursor.moveToFirst()) {
       ret = cursor.getString(0);
     }
     if (cursor != null && !cursor.isClosed()) cursor.close();
     return ret;
   }

   public CRS getCRS( long id )
   {
     CRS crs = null;
     // if ( myDB == null ) return null;
     Cursor cursor = myDB.query( CRS_TABLE,
                                 new String[] { "name", "desc", "digits" }, // columns
                                 "id=?", 
                                 new String[] { Long.toString(id) },
                                 null, null, null );
     if (cursor.moveToFirst()) {
       crs = new CRS( cursor.getString(0), cursor.getString(1), (int)cursor.getLong(2), false );
     }
     if (cursor != null && !cursor.isClosed()) {
       cursor.close();
     }
     return crs;
   }
}
