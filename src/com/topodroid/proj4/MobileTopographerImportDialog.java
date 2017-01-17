/** @file MobileTopographerImportDialog.java
 *
 * @author marco corvi
 * @date jan 2017
 *
 * @brief Proj MobileTopographer pointlist files dialog (from TopoDroid FixedImportDialog)
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;

// import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import android.content.Context;
import android.content.Intent;

import android.inputmethodservice.KeyboardView;

import android.net.Uri;

import android.util.Log;

public class MobileTopographerImportDialog extends Dialog
                               implements OnItemClickListener
                                        , OnClickListener
{ 
  static final String POINTLISTS     = Environment.getExternalStorageDirectory().getPath() + "/MobileTopographer/pointlists";
  static final String POINTLISTS_PRO = Environment.getExternalStorageDirectory().getPath() + "/MobileTopographerPro/pointlists";

  private Context mContext;
  private Proj4Activity mParent;

  private ArrayAdapter<String> mArrayAdapter;
  private ListView mList;

  private Button mBtnOk;
  private Button mBtnCancel;

  private TextView mTVlat;
  private TextView mTVlng;
  private TextView mTVhell;
  private TextView mTVhgeo;

  private double mLat, mLng, mHEll, mHGeo;
  private boolean isSet;

  public MobileTopographerImportDialog( Context context, Proj4Activity parent )
  {
    super( context );
    mContext = context;
    mParent  = parent;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate( savedInstanceState );

    setContentView( R.layout.mobiletopographer_import_dialog );
    getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

    setTitle( R.string.title_import );

    mArrayAdapter = new ArrayAdapter<String>( mContext, R.layout.message );
    mList = (ListView) findViewById(R.id.list);
    mList.setOnItemClickListener( this );
    mList.setDividerHeight( 2 );
    readPoints();

    mTVlat  = (TextView) findViewById( R.id.tv_lat );
    mTVlng  = (TextView) findViewById( R.id.tv_lng );
    mTVhell = (TextView) findViewById( R.id.tv_alt );
    mTVhgeo = (TextView) findViewById( R.id.tv_asl );

    mBtnOk     = (Button) findViewById( R.id.btn_ok );
    mBtnCancel = (Button) findViewById( R.id.btn_cancel );
    mBtnOk.setOnClickListener( this );
    mBtnCancel.setOnClickListener( this );

    isSet = false;
  }

  private boolean readPoints()
  {
    mArrayAdapter.clear();

    File dir = new File( POINTLISTS );
    if ( ! dir.exists() ) dir = new File( POINTLISTS_PRO );
    if ( ! dir.exists() ) return false;

    File[] files = dir.listFiles();
    if ( files == null || files.length == 0 ) return false;
    // Log.v("DistoX", "number of files " + files.length );

    boolean ret = false;
    for ( File f : files ) {
      // Log.v("DistoX", "file " + f.getName() + " is dir " + f.isDirectory() );
      if ( ! f.isDirectory() ) {
        ret = readPointFile( dir, f.getName() ) || ret; // N.B. read file before oring with ret
      }
    }
    if ( ret ) {
      mList.setAdapter( mArrayAdapter );
    } else {
      Toast.makeText( mContext, R.string.MT_points_none, Toast.LENGTH_SHORT ).show();
      dismiss();
    }
    return ret;
  }

  private boolean readPointFile( File dir, String filename )
  {
    // Log.v("DistoX", "reading file " + filename );
    boolean ret = false;
    try {
      File file = new File( dir, filename );
      FileReader fr = new FileReader( file );
      BufferedReader br = new BufferedReader( fr );
      for ( ; ; ) {
        String line = br.readLine();
        if ( line == null ) break;
        // Log.v("DistoX", "read " + line );

        String[] vals = line.split(",");
        int len = vals.length;
        if ( len >= 4 ) {
          ret = true;
          StringBuilder sb = new StringBuilder();
          sb.append( vals[len-3].trim() );
          sb.append( " " );
          sb.append( vals[len-4].trim() );
          sb.append( " " );
          sb.append( vals[len-2].trim() );
          sb.append( " " );
          sb.append( vals[len-1].trim() );
          // int k=0;
          // String name = vals[k];
          // while ( ++k < len-4 ) {
          //    name = name + "," + vals[k];
          // }
          mArrayAdapter.add( sb.toString() );
        }
      }
    } catch ( IOException e ) { 
    } catch ( NumberFormatException e ) {
    }
    return ret;
  }

  @Override
  public void onClick( View v ) 
  {
    Button b = (Button)v;
    if ( b == mBtnOk ) {
      if ( isSet ) mParent.setMobileTopographerPoint( mLng, mLat, mHGeo );
    } else {
      /* nothing */
    }
    dismiss();
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
  {
    String item = ((TextView) view).getText().toString();
    // TDLog.Log(  TDLog.LOG_INPUT, "MobileTopographerImportDialog onItemClick() " + item.toString() );
    String[] vals = item.split(" ");
    if ( vals.length == 4 ) {
      String lngstr = vals[0].trim();
      String latstr = vals[1].trim();
      String altstr = vals[2].trim();
      String aslstr = vals[3].trim();
      mHGeo = Double.parseDouble( aslstr );
      mHEll = Double.parseDouble( altstr );
      mLng  = Double.parseDouble( lngstr );
      mLat  = Double.parseDouble( latstr );
      mTVlat.setText( latstr );
      mTVlng.setText( lngstr );
      mTVhell.setText( altstr );
      mTVhgeo.setText( aslstr );
      isSet = true;
    }
  }
}


