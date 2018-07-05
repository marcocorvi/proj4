/** @file CRSListDialog.java
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
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.Toast;

import android.util.Log;

public class CRSListDialog extends Dialog
                        implements OnItemClickListener
                                , OnItemLongClickListener
                                , View.OnClickListener
{
  private Context mContext;
  private Proj4Activity mParent;
  private CRSManager mCRS;
  private ArrayAdapter<String> mArrayAdapter;
  private Button mBTnew;
  private int mType;

  private ListView mList;

  public CRSListDialog( Context context, Proj4Activity parent, CRSManager crs, int type )
  {
    super( context );
    mContext = context;
    mParent  = parent;
    mCRS     = crs;
    mType    = type;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.crs_list );
    mArrayAdapter = new ArrayAdapter<String>( mContext, R.layout.message );

    mBTnew = (Button) findViewById(R.id.crs_new);

    mList = (ListView) findViewById(R.id.list);
    mList.setAdapter( mArrayAdapter );
    mList.setOnItemClickListener( this );
    mList.setOnItemLongClickListener( this );
    mList.setDividerHeight( 2 );

    mBTnew.setOnClickListener( this );

    updateList();
    setTitle( R.string.title_crs );
  }

  void updateList()
  {
    mArrayAdapter.clear();
    // for ( String k : mCRS.getNames() ) {
    //   mArrayAdapter.add( k );
    // }
    for ( CRS crs : mCRS.getCRSs() ) {
      mArrayAdapter.add( crs.mEPSG + " " + crs.mName );
    }
  }
 
  // @Override
  public void onClick(View v) 
  {
    Button b = (Button) v;
    if ( b == mBTnew ) {
      hide();
      new CRSNewDialog( mParent, mParent, mCRS, this ).show();
    } else {
      hide();
      dismiss();
    }
  }

  // ---------------------------------------------------------------
  // list items click

  @Override 
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    String str = ((TextView) view).getText().toString();
    String name = (String) mArrayAdapter.getItem(pos);
    int idx = str.indexOf(' ') + 1;
    String key = str.substring( idx );
    Log.v( "Proj4", "item click: pos " + pos + " name " + name + " = " + str + " idx " + idx + " " + key );

    // mParent.setCRS( str, mType );
    mParent.setCRS( key, mType );
    dismiss();
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
  {
    String str = ((TextView) view).getText().toString();
    String name = (String) mArrayAdapter.getItem(pos);
    
    int idx = str.indexOf(' ') + 1;
    String key = str.substring( idx );
    Log.v( "Proj4", "item click: pos " + pos + " name " + name + " = " + str + " idx " + idx + " " + key );

    if ( key != null ) {
      new CRSEditDialog( mContext, mCRS, this, key ).show();
      return true;
    } else {
      return false;
    }
  }

}

