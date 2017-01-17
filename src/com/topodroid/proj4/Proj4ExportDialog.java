/* @file Proj4ExportDialog.java
 *
 * @author marco corvi
 * @date jan 2017
 *
 * @brief Proj4 export doialog
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

import android.app.Dialog;
import android.os.Bundle;

import android.content.Context;

import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.view.Window;
import android.view.WindowManager;

public class Proj4ExportDialog extends Dialog
                           implements View.OnClickListener
{
  private Proj4Activity  mParent;

  private EditText mETfilename;
  
  private Button   mButtonOK;
  private Button   mButtonCancel;

  Proj4ExportDialog( Context context, Proj4Activity parent )
  {
    super( context );
    mParent  = parent;
  }

// -------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);

    setContentView( R.layout.proj4_export_dialog );
    getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

    mETfilename = (EditText) findViewById( R.id.filename );

    mButtonOK     = (Button) findViewById(R.id.button_ok );
    mButtonCancel = (Button) findViewById(R.id.button_cancel );

    mButtonOK.setOnClickListener( this );
    mButtonCancel.setOnClickListener( this );

    setTitle( R.string.title_export );

  }

  public void onClick(View v) 
  {
    Button b = (Button) v;
    // TDLog.Log( TDLog.LOG_INPUT, "GM dialog onClick button " + b.getText().toString() );
    if ( b == mButtonOK ) {
      String filename = mETfilename.getText().toString();
      if ( filename == null || filename.length() == 0 ) {
        mETfilename.setError( "filename required" );
        return;
      } else {
        mParent.exportToFile( "/sdcard/" + filename );
      }
    } else if ( b == mButtonCancel ) {
      /* nothing */
    }
    dismiss();
  }

}
