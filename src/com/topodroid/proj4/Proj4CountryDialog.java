/** @file Proj4CountryDialog.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief CRS list
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 * CHANGES
 */
package com.topodroid.proj4;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Dialog;

import android.content.Context;

import android.view.View;
// import android.view.View.OnClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;

import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.Toast;

import android.util.Log;

public class Proj4CountryDialog extends Dialog
                                implements OnItemClickListener
{
  private Context mContext;
  private Proj4Activity mParent;
  private CRSManager mCRS;
  private ArrayAdapter<String> mArrayAdapter;
  private Button mBTcountry;

  private ListView mList;

  public Proj4CountryDialog( Context context, Proj4Activity parent, CRSManager crs )
  {
    super( context );
    mContext = context;
    mParent  = parent;
    mCRS     = crs;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.proj4_country_dialog );
    mArrayAdapter = new ArrayAdapter<String>( mContext, R.layout.message );

    mBTcountry = (Button) findViewById(R.id.country);

    mList = (ListView) findViewById(R.id.list);
    mList.setAdapter( mArrayAdapter );
    mList.setOnItemClickListener( this );
    mList.setDividerHeight( 2 );

    mArrayAdapter.clear();
    for ( String n : mCRS.getCountryNames() ) {
      mArrayAdapter.add( n );
    }
    mBTcountry.setText( mCRS.getCountryName() );
    setTitle( R.string.title_country );
  }
 
  // ---------------------------------------------------------------
  // list items click

  @Override 
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    String str = ((TextView) view).getText().toString();
    // String name = (String) mArrayAdapter.getItem(pos);
    // Log.v( "Proj4", "item click: pos " + pos + " name " + name + " = " + str );
    mBTcountry.setText( str );
  }

  @Override
  public void onBackPressed()
  {
    String name = mBTcountry.getText().toString();
    // Log.v( "Proj4", "item long click: pos " + pos + " name " + name + " = " + str );
    if ( name != null ) {
      String country = mCRS.getCountryCode( name );
      mParent.setCountry( country );
    }
    super.onBackPressed();
  }

}

