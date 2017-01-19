/** @file Proj4CountryDialog.java
 *
 * @author marco corvi
 * @date jan 2017
 *
 * @brief Country/Continent dialog
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
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
import android.view.View.OnClickListener;

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
                                , OnClickListener
{
  private Context mContext;
  private Proj4Activity mParent;
  private CRSManager mCRS;
  private ArrayAdapter<String> mArrayAdapter;
  private Button mBTcountry;

  private ListView mList;
  private long mContinent;

  public Proj4CountryDialog( Context context, Proj4Activity parent, CRSManager crs )
  {
    super( context );
    mContext = context;
    mParent  = parent;
    mCRS     = crs;
    mContinent = -1L;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.proj4_country_dialog );
    mArrayAdapter = new ArrayAdapter<String>( mContext, R.layout.message );

    mBTcountry = (Button) findViewById(R.id.country);
    mBTcountry.setOnClickListener( this );

    mList = (ListView) findViewById(R.id.list);
    mList.setAdapter( mArrayAdapter );
    mList.setOnItemClickListener( this );
    mList.setDividerHeight( 2 );

    updateList( mContinent );
    mBTcountry.setText( mCRS.getCountryName() );
    setTitle( R.string.title_country );
  }

  private void updateList( long continent )
  {
    // Log.v("Proj4", "continent " + continent );
    mArrayAdapter.clear();
    if ( continent < 0L ) {
      ArrayList<String> continents = mCRS.getContinentNames();
      for ( String n : continents ) {
        mArrayAdapter.add( n );
      }
    } else {
      mArrayAdapter.add( ".." );
      for ( String n : mCRS.getCountryNames( continent ) ) {
        mArrayAdapter.add( n );
      }
    }
  }
 
  // ---------------------------------------------------------------
  // list items click

  @Override 
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    String str = ((TextView) view).getText().toString();
    // String name = (String) mArrayAdapter.getItem(pos);
    // Log.v( "Proj4", "item click: pos " + pos + " name " + name + " = " + str );
    if ( mContinent < 0L ) {
      mContinent = mCRS.getContinentCode( str );
      updateList( mContinent );
    } else {
      if ( str.equals("..") ) {
        mContinent = -1;
        updateList( mContinent );
      } else {
        mBTcountry.setText( str );
      }
    }
  }

  @Override
  public void onClick( View v )
  {
    if ( (Button)v == mBTcountry ) {
      setTheCountry();
    }
    dismiss();
  }

  @Override
  public void onBackPressed()
  {
    setTheCountry();
    super.onBackPressed();
  }

  private void setTheCountry()
  {
    String name = mBTcountry.getText().toString();
    // Log.v( "Proj4", "item long click: pos " + pos + " name " + name + " = " + str );
    if ( name != null ) {
      String country = mCRS.getCountryCode( name );
      mParent.setCountry( country );
    }
  }


}

