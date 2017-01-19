/* @file CRSEditDialog.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief CRS edit dialog
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

import android.os.Bundle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;

import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.widget.EditText;
import android.widget.Button;
import android.view.View;

// import android.widget.Toast;

import android.util.Log;

public class CRSEditDialog extends Dialog
                           implements View.OnClickListener
{
  private Context mContext;
  private CRSManager mCRSmanager;
  private CRSListDialog mCRSlist;
  private CRS      mCRS;
  private String   mName;

  private EditText mETname;
  private EditText mETdesc;
  private EditText mETdigits;
  private Button   mBtnOK;
  private Button   mBtnDelete;
  private Button   mBtnCancel;

  public CRSEditDialog( Context context, CRSManager crs, CRSListDialog list, String name )
  {
    super( context );
    mContext = context;
    mCRSmanager = crs;
    mCRSlist    = list;
    mName    = name;
    mCRS     = crs.getCRS( name );
  }

// -------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.crs_edit_dialog);
    mETname = (EditText) findViewById(R.id.crs_name);
    mETdesc = (EditText) findViewById(R.id.crs_desc);
    mETdigits = (EditText) findViewById(R.id.crs_digits);

    if ( mName != null ) {
      mETname.setText( mName );
    }
    if ( mCRS != null ) {
      mETdesc.setText( mCRS.mDesc );
      mETdigits.setText( Integer.toString( mCRS.mDigits ) );
    }

    mBtnOK     = (Button) findViewById(R.id.button_ok);
    mBtnDelete = (Button) findViewById(R.id.button_delete);
    mBtnCancel = (Button) findViewById(R.id.button_cancel);

    mBtnOK.setOnClickListener( this );
    mBtnDelete.setOnClickListener( this );
    mBtnCancel.setOnClickListener( this );
  }

  void askDelete()
  {
    AlertDialog.Builder alert = new AlertDialog.Builder( mContext );
    // alert.setTitle( R.string.delete );
    alert.setMessage( mContext.getResources().getString( R.string.really_delete) );
    
    alert.setPositiveButton( R.string.button_ok, 
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick( DialogInterface dialog, int btn ) {
          String name = mETname.getText().toString(); // change name last
          mCRSmanager.deleteCRS( name );
          mCRSmanager.saveCRS();
          mCRSlist.updateList();
          dismiss();
        }
    } );

    alert.setNegativeButton( R.string.button_cancel, 
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick( DialogInterface dialog, int btn ) { }
    } );
    alert.show();
  }

  // FIXME synchronized ?
  @Override
  public void onClick(View v) 
  {
    // When the user clicks, just finish this activity.
    // onPause will be called, and we save our data there.
    Button b = (Button) v;

    if ( b == mBtnOK ) {
      String desc = mETdesc.getText().toString();
      if ( desc.length() > 0 ) {
        mCRSmanager.setDescription( mName, desc );
      }
      String digits = mETdigits.getText().toString();
      if ( digits.length() > 0 ) {
        mCRSmanager.setDigits( mName, Integer.parseInt( digits ) );
      }
      String name = mETname.getText().toString(); // change name last
      if ( name.length() > 0 && ! mName.equals( name ) ) {
        if ( mCRSmanager.getCRS( name ) != null ) {
          mETname.setError("CRS name already exists" );
          return;
        }
        mCRSmanager.setName( mName, name );
      }
      mCRSmanager.saveCRS();
      mCRSlist.updateList();
      dismiss();
    } else if ( b == mBtnDelete ) {
      // ask confirm
      askDelete();
    } else {
      dismiss();
    }
  }

}

