/* @file MyListPreferences.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief option list
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 */
package com.topodroid.proj4;

import android.content.Context;
import android.preference.Preference;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.preference.Preference.OnPreferenceChangeListener;

/**
 */
public class MyListPreference extends ListPreference
{
  public MyListPreference( Context c, AttributeSet a ) 
  {
    super(c,a);
    init();
  }

  public MyListPreference( Context c )
  {
    super( c );
    init();
  }

  private void init()
  {
    setOnPreferenceChangeListener( new OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange( Preference p, Object v ) 
      {
        p.setSummary( getEntry() );
        // TODO set the corresponding Proj4 value
        return true;
      }
    } );
  }

  @Override
  public CharSequence getSummary() { return super.getEntry(); }
}

