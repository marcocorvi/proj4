/* @file Proj4Preferences.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief Proj4 preferences
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

// import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
// import android.preference.CheckBoxPreference;
// import android.preference.EditTextPreference;
// import android.preference.ListPreference;

public class Proj4Preferences extends PreferenceActivity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate( savedInstanceState );

    // Bundle extras = getIntent().getExtras();
    // if ( extras != null ) {
    // }

    addPreferencesFromResource(R.xml.preferences);
  }

}
