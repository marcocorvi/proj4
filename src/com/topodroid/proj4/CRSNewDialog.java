/* @file CRSNewDialog.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief new-CRS dialog
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 * CHANGES
 */
package com.topodroid.proj4;

import android.app.Dialog;
import android.os.Bundle;

import android.content.Context;

import android.widget.EditText;
import android.widget.Button;
import android.view.View;

// import android.widget.Toast;

public class CRSNewDialog extends Dialog
                          implements View.OnClickListener
{
  private Context mContext;
  private CRSManager mCRS;

  private EditText mETname;
  private EditText mETdesc;
  private EditText mETdigits;
  private Button   mBtnOK;
  private Button   mBtnCancel;

  public CRSNewDialog( Context context, CRSManager crs )
  {
    super( context );
    mContext = context;
    mCRS     = crs;
  }

// -------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.crs_new_dialog);
    mETname = (EditText) findViewById(R.id.crs_name);
    mETdesc = (EditText) findViewById(R.id.crs_desc);
    mETdigits = (EditText) findViewById(R.id.crs_digits);

    mBtnOK = (Button) findViewById(R.id.button_ok);
    mBtnCancel = (Button) findViewById(R.id.button_cancel);

    mBtnOK.setOnClickListener( this );
    mBtnCancel.setOnClickListener( this );
  }

  // FIXME synchronized ?
  @Override
  public void onClick(View v) 
  {
    // When the user clicks, just finish this activity.
    // onPause will be called, and we save our data there.
    Button b = (Button) v;

    if ( b == mBtnOK ) {
      String name = mETname.getText().toString();
      String desc = mETdesc.getText().toString();
      String digits = mETdigits.getText().toString();
      if ( name.length() > 0 && desc.length() > 0 ) {
        int d = 8;
        if ( digits.length() > 0 ) {
          d = Integer.parseInt( digits );
          if ( d < 0 )  d = 0;
          if ( d > 10 ) d = 10;
        }
        if ( mCRS.getCRS( name ) != null ) { // already there
          mETname.setError( "CRS altready exists" );
          return;
        } 
        mCRS.addCrs( name, desc, d, true );
      }
    }
    dismiss();
  }

}

