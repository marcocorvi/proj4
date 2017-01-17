/** @file Country.java
 *
 * @author marco corvi
 * @date jan 2017
 *
 * @brief Country
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

// import android.content.res.Resources;
import android.content.Context;

import android.util.Log;

class Country
{
  String mCode; // two-char iso code
  String mName; // presentation name

  Country( String code, String name )
  {
    mCode = code;
    mName = name;
  }

}
