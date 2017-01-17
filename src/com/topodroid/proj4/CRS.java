/** @file CRS.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief CRS
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.proj4;

// import android.content.res.Resources;
import android.content.Context;

import android.util.Log;

class CRS
{
  static final String TAG = "Proj4";

  String mName;
  String mDesc;
  int mDigits;     // digits after the point
  boolean mCustom; // whether the CRS is custom

  CRS( String name, String desc, int digits, boolean custom )
  {
    mName = name;
    mDesc = desc;
    mDigits = digits;
    mCustom = custom;
  }

}
