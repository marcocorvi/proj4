/** @file CRSManager.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief CRS list
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.Set;
import java.util.Hashtable;
import java.util.ArrayList;

// import android.content.res.Resources;
import android.content.Context;

import android.util.Log;

class CRSManager
{
  static final String TAG = "Proj4";

  String  mFilename; // custon CRSs
  String  mCountry;
  Context mContext;
  Map<String, CRS > mCrs;
  private CRSdatabase mDB;

  CRSManager( Context context, String filename, String country )
  {
    mFilename = filename;
    mCountry  = country;
    mContext  = context;
    mCrs = new Hashtable<String, CRS>();
    mDB  = new CRSdatabase( mContext );
  }

  void setCountry( String country ) { mCountry = country; }

  String getCountry() { return mCountry; }

  int size() { return mCrs.size(); }

  boolean hasCRS( String name )
  {
    return mCrs.containsKey( name );
  }

  Set<String> getNames() { return mCrs.keySet(); }

  CRS getCRS( String name )
  {
    if ( name == null ) return null;
    return (CRS)mCrs.get( name );
  }

  String getDescription( String name ) 
  { 
    if ( name == null ) return null;
    CRS crs = (CRS)mCrs.get( name );
    if ( crs != null ) return crs.mDesc;
    return null;
  }

  int getDigits( String name ) 
  {       
    if ( name == null ) return 0;
    CRS crs = (CRS)mCrs.get( name );
    if ( crs != null ) return crs.mDigits;
    return 0;
  }

  void setDescription( String name, String desc ) 
  { 
    if ( name == null || desc == null ) return;
    if ( name.length() == 0 || desc.length() == 0 ) return;
    CRS crs = (CRS)mCrs.get( name );
    if ( crs != null ) crs.mDesc = desc;
  }

  void setDigits( String name, int digits )
  { 
    if ( name == null ) return;
    if ( name.length() == 0 ) return;
    if ( digits < 0 ) digits = 0;
    if ( digits > 10 ) digits = 10;
    CRS crs = (CRS)mCrs.get( name );
    if ( crs != null ) crs.mDigits = digits;
  }

  void setName( String name, String new_name )
  { 
    if ( name == null || new_name == null ) return;
    if ( name.length() == 0 || new_name.length() == 0 ) return;
    CRS crs = (CRS)mCrs.get( name );
    if ( crs != null ) crs.mName = new_name;
    mCrs.remove( name );
    mCrs.put( new_name, crs );
  }

  void addCrs( CRS crs )
  {
    if ( mCrs.containsKey( crs.mName ) ) return;
    mCrs.put( crs.mName, crs );
  }

  void addCrs( String name, String desc, int digits, boolean custom )
  {
    if ( mCrs.containsKey( name ) ) return;
    CRS crs = new CRS( name, desc, digits, custom );
    mCrs.put( name, crs );
  }

  void deleteCRS( String name ) { mCrs.remove( name ); }

  // --------------------------------------------------------
    
  void loadCRS()
  {
    addCrs( "Long-Lat", "+proj=latlong +datum=WGS84", 8, false );
    loadCustomCRS();
    loadCountryCRS();
  }

  private void loadCustomCRS() // custom CRS
  {
    FileInputStream fis = null;
    try {
      fis = mContext.openFileInput( mFilename );
      BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );

      String line = br.readLine();
      String name = null;
      int digits = 8;
      String description = null;
      while ( line != null ) {
        if ( line.startsWith( "<!--" ) ) {
          // comment
        } else if ( line.startsWith( "<crs" ) ) {
          // get the name
          int i1 = line.indexOf( "name=" );
          int i2;
          if ( i1 > 0 ) {
            i1 += 6; // name="
            i2 = line.indexOf( '\"', i1 );
            if ( i2 > i1 ) {
              name = line.substring( i1, i2 );
            }
          }
          i1 = line.indexOf( "digits=" );
          if ( i1 > 0 ) {
            i1 += 8; // digits="
            i2 = line.indexOf( '\"', i1 );
            if ( i2 > i1 ) {
              digits = Integer.parseInt( line.substring( i1, i2 ) );
            }
          }
        } else if ( line.startsWith( "</crs>" ) ) {
          if ( name != null && description != null ) {
            description.replace( '\n', ' ' );
            description.replace( '\t', ' ' );
            addCrs( name, description, digits, true );
            name = null;
            description = null;
            digits = 8;
          }
        } else {
          if ( description == null ) {
            description = line;
          } else {
            description = description + " " + line;
          }
        }
        line = br.readLine();
      }
      fis.close();
    } catch ( FileNotFoundException e ) {
      // Log.e( TAG, "FILE NOT FOUND Exception " + e.getMessage() );
    } catch ( IOException e ) {
      Log.e( TAG, "IO Exception " + e.getMessage() );
    }
  }

//   addCrs( "UTM32N", "+proj=utm +zone=32 +ellps=WGS84  +datum=WGS84 +units=m +nodefs", 2 );
//   addCrs( "ED50-32", "+proj=utm +zone=32 +ellps=intl +units=m +nodefs", 2 );
//   addCrs( "Gauss-Boaga Zone 1",
//             "+proj=tmerc +ellps=intl +lat_0=0 +lon_0=9 +k=0.9996 +x_0=1500000 +y_0=0 +towgs84=-104.1, -49.1, -9.9, 0.971, -2.971, 0.714, -11.68, +nodefs", 2 );
//   addCrs( "Gauss-Boaga Zone 2",
//             "+proj=tmerc +ellps=intl +lat_0=0 +lon_0=9 +k=0.9996 +x_0=2520000 +y_0=0 +towgs84=-104.1, -49.1, -9.9, 0.971, -2.971, 0.714, -11.68 +nodefs", 2 );
//   addCrs( "Gauss Boaga Sardinia", "+proj=tmerc +ellps=intl +lat_0=0 +lon_0=9 +k=0.9996 +x_0=1500000 +y_0=0 +towgs84=-168.6, -34.0, 38.6, -0.374, -0.679, -1.379, -9.48 +nodefs", 2 );
//   addCrs( "Gauss Boaga Sicily", "+proj=tmerc +ellps=intl +lat_0=0 +lon_0=15 +k=0.9996 +x_0=2520000 +y_0=0 +towgs84=-50.2, -50.4, 84.8, -0.690, -2.012, 0.459, -28.08 +nodefs", 2 );


  void loadCountryCRS()
  {
    // Log.v("Proj4", "LOAD COUNTRY " + mCountry );
    // open CRS database
    // get all CRS for mCountry: key-descr
    ArrayList< CRS > crss = mDB.getAllCRSs( mCountry );
    for ( CRS crs : crss ) {
      addCrs( crs );
    }
  }

  void saveCRS() // save custom CRS
  {
    FileOutputStream fos = null;
    try {
      fos = mContext.openFileOutput( mFilename, Context.MODE_PRIVATE );
      BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( fos ) );

      for ( String k : mCrs.keySet() ) {
        CRS v = (CRS)mCrs.get( k );
        if ( v.mCustom ) {
          bw.write( "<crs name=\"" + k + "\" digits=\"" + v.mDigits + "\">\n" );
          bw.write( v.mDesc + "\n" );
          bw.write( "</crs>\n" );
        }
      }
      bw.flush();
      fos.close();
    } catch ( IOException e ) {
      Log.e( TAG, "IO Exception " + e );
    }
  }

}
