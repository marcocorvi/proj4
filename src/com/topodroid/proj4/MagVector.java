/* @file MagVector.java
 *
 * @author marco corvi
 * @date jan 2017
 *
 * @brief Proij4 World Magnetic Model (copied from TopoDroid)
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 * Implemented after GeomagneticLibrary.c by
 *  National Geophysical Data Center
 *  NOAA EGC/2
 *  325 Broadway
 *  Boulder, CO 80303 USA
 *  Attn: Susan McLean
 *  Phone:  (303) 497-6478
 *  Email:  Susan.McLean@noaa.gov
 */
package com.topodroid.proj4;

class MagVector
{
  double x, y, z;

  MagVector( double x0, double y0 , double z0 )
  {
    x = x0;
    y = y0;
    z = z0;
  }

  double distance( MagVector v )
  {
    return Math.sqrt( (x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z) );
  }
}
