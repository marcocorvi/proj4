/* @file MagLegendre.java
 *
 * @author marco corvi
 * @date jan 2017
 *
 * @brief Proj4 World Magnetic Model (copied from TopoDroid)
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


// MAGtype_LegendreFunction;
class MagLegendre
{
  int nTerms;
  double[] Pcup; /* Legendre Function */
  double[] dPcup; /* Derivative of Legendre fcn */

  MagLegendre( int nt ) 
  {
    nTerms = nt;
    Pcup  = new double[ nTerms + 1 ];
    dPcup = new double[ nTerms + 1 ];
  }
}
