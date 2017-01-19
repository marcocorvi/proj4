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

  long   mEPSG;    // EPSG number
  String mName;    // "custom" name
  String mDesc;    // proj4 syntax
  int mDigits;     // digits after the point
  boolean mCustom; // whether the CRS is custom

  CRS( long epsg, String name, String desc, int digits, boolean custom )
  {
    mEPSG = epsg;
    mName = name;
    mDesc = desc;
    mDigits = digits;
    mCustom = custom;
  }

}
