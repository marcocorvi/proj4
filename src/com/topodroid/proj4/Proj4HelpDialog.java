/* @file Proj4HelpDialog.java
 *
 * @author marco corvi
 * @date feb 2013
 *
 * @brief Proj4 help
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 */
package com.topodroid.proj4;

// import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import android.content.Context;
// import android.content.Intent;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;
// import android.view.View.OnKeyListener;
// import android.view.KeyEvent;

import android.util.Log;

public class Proj4HelpDialog extends Dialog
                             // implements View.OnClickListener
{
  // private TextView mTVtext;
  // private Button   mButtonOK;

  Proj4HelpDialog( Context context )
  {
    super( context );
    // Log.v( "Proj4", "Proj4HelpDialog cstr" );
  }

// -------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);

    // Log.v( "Proj4", "Proj4HelpDialog create" );

    // setTitle( mContext.getResources().getString( R.string.help_title ) );
    setTitle( "Proj4 - HELP" );

    setContentView(R.layout.help_dialog);

    // mTVtext  = (TextView) findViewById(R.id.help_text );
    // mButtonOK = (Button) findViewById(R.id.button_ok );
    // mButtonOK.setOnClickListener( this );

    // Bundle extras = getIntent().getExtras();
    // String title  = extras.getString( key );

  }

  // public void onClick(View v) 
  // {
  //   // When the user clicks, just finish this activity.
  //   // onPause will be called, and we save our data there.
  //   Button b = (Button) v;
  //   if ( b == mButtonOK ) {
  //   }
  //   dismiss();
  // }

}


