/* @file Proj4Activity.java
 *
 * @author marco corvi
 * @date jan 2013
 *
 * @brief Proj4 main activity
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 * for Long-Lat Z is ellipsoid altitude
 */
package com.topodroid.proj4;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Locale;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import android.text.InputType;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.SharedPreferences.Editor;

import android.net.Uri;

import android.util.Log;

import org.proj4.PJ;
import org.proj4.PJException;

import java.util.Map;

public class Proj4Activity extends Activity 
                           implements OnClickListener
                           , OnLongClickListener
                           , OnSharedPreferenceChangeListener
{

    /** class TAG */
    private static final String TAG = "Proj4";
    private static final String CRS_FILENAME = "crs";
    private static final String VERSION = "1.1.0";  // version for the Launch intent
    // private static final String EXPORT_FILENAME = "/sdcard/proj4.txt";

    static final int TYPE_FROM = 0;
    static final int TYPE_TO   = 1;

    private CRSManager mCRSmanager;
    private String  mCountry = null;
    private Bundle  mExtras;
    private boolean mOnResult;
    private int     mResultCode;
    private double  mResultLong;
    private double  mResultLat;
    private double  mResultAlt;
    private int     mResultDecimals;
    private String  mResultCS;

    private Button   mBTfromcrs;
    private Button   mBTtocrs;
    private EditText mETfromX;
    private EditText mETfromY;
    private EditText mETfromZ;
    private EditText mETtoX;
    private EditText mETtoY;
    private EditText mETtoZ;
    private ImageButton mBTfrom2to;
    private ImageButton mBTto2from;
    private ImageButton mBTfrom2map;
    private ImageButton mBTto2map;
    private ImageButton mBTmobile2from;
    private ImageButton mBTgeomag;
    private EditText mETdeclination;
    private MenuItem mMIexport;
    private MenuItem mMIcountry;
    // private MenuItem mMIoptions;
    private MenuItem mMIhelp;

    private String   mFromCRS;
    private String   mToCRS;

    private WorldMagneticModel mWMM;

    void setCRS( String name, int type ) 
    { 
      if ( type == TYPE_FROM ) {
        mFromCRS = name;
        mBTfromcrs.setText( mFromCRS );
      } else {
        mToCRS   = name;
        mBTtocrs.setText( mToCRS );
      }
    }

    CRSManager getCRSManager() { return mCRSmanager; }

    /**
     * Loads the Proj4 library.
     */
    static {
      System.loadLibrary("proj");
    }

    private void setCRSmap()
    {
      mCRSmanager = new CRSManager( this, CRS_FILENAME, mCountry );
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        mExtras = getIntent().getExtras();
        mOnResult = (mExtras != null);

        setContentView(R.layout.main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
        prefs.registerOnSharedPreferenceChangeListener( this );

        // TextView text = (TextView)findViewById(R.id.text_view);
        mBTfromcrs = (Button)findViewById(R.id.from_crs);
        // mBTfrombtn = (Button)findViewById(R.id.from_btn);

        mBTtocrs   = (Button)findViewById(R.id.to_crs);
        // mBTtobtn   = (Button)findViewById(R.id.to_btn);
        mETfromX  = (EditText)findViewById(R.id.from_x);
        mETfromY  = (EditText)findViewById(R.id.from_y);
        mETfromZ  = (EditText)findViewById(R.id.from_z);
        mETtoX     = (EditText)findViewById(R.id.to_x);
        mETtoY     = (EditText)findViewById(R.id.to_y);
        mETtoZ     = (EditText)findViewById(R.id.to_z);
        mETdeclination = (EditText)findViewById(R.id.declination);
        mBTfrom2to = (ImageButton)findViewById(R.id.from2to);
        mBTto2from = (ImageButton)findViewById(R.id.to2from);
        mBTfrom2map = (ImageButton)findViewById(R.id.from2map);
        mBTto2map   = (ImageButton)findViewById(R.id.to2map);
        mBTmobile2from = (ImageButton)findViewById(R.id.mobile2from);
        mBTgeomag   = (ImageButton)findViewById(R.id.geomag);

        mBTfromcrs.setOnClickListener( this );
        mBTtocrs.setOnClickListener( this );
        // mBTfrombtn.setOnClickListener( this );
        // mBTtobtn.setOnClickListener( this );
        mBTfrom2to.setOnClickListener( this );
        mBTto2from.setOnClickListener( this );
        mBTfrom2map.setOnClickListener( this );
        mBTto2map.setOnClickListener( this );
        mBTmobile2from.setOnClickListener( this );
        mBTgeomag.setOnClickListener( this );

        mETfromX.setOnLongClickListener( this );
        mETfromY.setOnLongClickListener( this );
        mETtoX.setOnLongClickListener( this );
        mETtoY.setOnLongClickListener( this );

        setTitle( "PROJ4 " + PJ.getVersion());
        // Log.d(TAG, PJ.getVersion());

        getCRSprefs();
        setCRSmap();

        mWMM = new WorldMagneticModel( this );
    }

    @Override
    protected void onStart()
    {
      super.onStart();
      mCRSmanager.loadCRS();
      if ( mExtras != null ) {

        setCRS( "Long-Lat", TYPE_FROM );
        String version  = mExtras.getString( "version" );
        if ( version == null ) {
          mOnResult = false;
        } else {
          if ( VERSION.startsWith( version ) ) // FIXME version check
          {
            mBTfromcrs.setOnClickListener( null );
            mBTto2from.setOnClickListener( null );
            mBTto2map.setOnClickListener( null );
            mBTto2from.setVisibility( View.GONE );
            mBTto2map.setVisibility( View.GONE );
            mBTmobile2from.setVisibility( View.GONE ); // no MobileTopographer with Intent Request
            mETtoX.setInputType( InputType.TYPE_NULL );
            mETtoY.setInputType( InputType.TYPE_NULL );
            mETtoZ.setInputType( InputType.TYPE_NULL );

            String request = mExtras.getString( "request" );
            if ( request.equals( "CRS_INPUT_REQUEST" ) ) {
              mBTtocrs.setText( "Long-Lat" );
              mBTtocrs.setOnClickListener( null );
              mETtoX.setOnLongClickListener( null );
              mETtoY.setOnLongClickListener( null );
              mBTto2from.setOnClickListener( null );
              mBTto2map.setOnClickListener( null );
              mBTto2from.setVisibility( View.GONE );
              mBTto2map.setVisibility( View.GONE );
              setCRS( "Long-Lat", TYPE_TO );
              mResultCode = RESULT_OK;
            } else if ( request.equals( "CRS_CONVERSION_REQUEST" ) ) {
              String cs_from  = mExtras.getString( "cs_from" );
              String cs_to =  mExtras.getString( "cs_to" );
              double lng = mExtras.getDouble( "longitude" ); // X
              double lat = mExtras.getDouble( "latitude" );  // Y
              double alt = mExtras.getDouble( "altitude" ); // Z
              // Log.v( TAG, "EXTRA cs_from " + cs_from );
              // Log.v( TAG, "data " + lng + " " + lat + " " + alt );
              int d = 2;
              if ( cs_from != null && mCRSmanager.hasCRS( cs_from ) ) {
                setCRS( cs_from, TYPE_FROM );
                // mBTfromcrs.setText( mFromCRS );
                d = mCRSmanager.getDigits( cs_from );
                // Log.v( TAG, "set cs from " + cs_from);
              } 
              if ( cs_to != null && mCRSmanager.hasCRS( cs_to ) ) {
                setCRS( cs_to, TYPE_TO );
                // Log.v( TAG, "set cs to " + cs_to);
              }
              String fmt;
              StringWriter sw = new StringWriter();
              PrintWriter  pw = new PrintWriter( sw );
              if ( alt > 0 ) {
                fmt = "%." + d + "f %." + d + "f + %.2f";
                pw.format(Locale.US, fmt, lng, lat, alt );
              } else {
                fmt = "%." + d + "f %." + d + "f";
                pw.format(Locale.US, fmt, lng, lat );
              }
              
              // mETfrom.setText( sw.getBuffer().toString() );
              mETfromX.setText( coord2text( lng, d ) );
              mETfromY.setText( coord2text( lat, d ) );
              mETfromZ.setText( coord2text( alt, 0 ) );
              mResultCode = RESULT_OK;
            } else {
              Log.e( TAG, "EXTRA bad request " + request );
              mResultCode = RESULT_CANCELED;
              finish();
            }
          } else {
            Log.e( TAG, "EXTRA bad version " + version );
            mResultCode = RESULT_CANCELED;
            finish();
          }
        }
      }
    }

    // @Override
    // protected void onStop()
    // {
    //   super.onStop();
    // }

    @Override
    protected void onPause()
    {
      super.onPause();
      mCRSmanager.saveCRS();
      setCRSprefs();
    }

    private boolean isDDMMSS( String data )
    {
      int len = data.length();
      if ( len > 4 ) len = 4;
      for ( int k=0; k<len; ++k ) {
        if ( data.charAt(k) == 'd' ) return true;
      }
      return false;
    }

    String switchUnits( String data ) 
    {
      int len = data.length();
      for ( int k=0; k<len; ++k ) {
        if ( data.charAt(k) == 'd' ) { // ddmmss
          int d = Integer.parseInt( data.substring(0,k) );
          for ( int k1=k+1; k1<len; ++k1 ) {
            if ( data.charAt(k1) == '\'' ) { // minutes
              int m = Integer.parseInt( data.substring(k+1,k1) );
              float s = Float.parseFloat( data.substring(k1+1,len-1) );
              s = d + m / 60.0f + s / 3600.0f;
              StringWriter sw = new StringWriter();
              PrintWriter  pw = new PrintWriter( sw );
              pw.format(Locale.US, "%.8f", s );
              return sw.getBuffer().toString();
            }
          }
        } else if ( k < 3 && data.charAt(k) == '.' ) { // degrees
          float s = Float.parseFloat( data );
          int d = (int)s;
          s = (s-d)*60;
          int m = (int)s;
          s = (s-m)*60;
          StringWriter sw1 = new StringWriter();
          PrintWriter  pw1 = new PrintWriter( sw1 );
          pw1.format(Locale.US, "%dd%02d\'%05.2f\"", d, m, s );
          return sw1.getBuffer().toString();
        }
      }
      return data;
    }

    @Override
    public boolean onLongClick( View view )
    {
      if ( view.getId() == R.id.from_x || view.getId() == R.id.from_y ) {
        EditText et = (EditText) view;
        String data = et.getText().toString();
        String[] vals = data.split(" ");
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter( sw );
        int cnt = 0;
        for ( int k=0; k<vals.length; ++k ) {
          if ( vals[k].length() > 0 ) {
            if ( cnt < 2 ) { // switch only first two data
              vals[k] = switchUnits( vals[k] );
            }
            if ( cnt == 0 ) {
              pw.format(Locale.US,"%s", vals[k] );
            } else {
              pw.format(Locale.US," %s", vals[k] );
            }
            ++cnt;
          }
        }
        et.setText( sw.getBuffer().toString() );
        return true;
      } else if ( view.getId() == R.id.to_x || view.getId() == R.id.to_y ) {
        EditText et = (EditText) view;
        // int sdk = android.os.Build.VERSION.SDK_INT;
        // if ( sdk < android.os.Build.VERSION_CODES.HONEYCOMB ) {
          android.text.ClipboardManager clipboard =
            (android.text.ClipboardManager)getSystemService( CLIPBOARD_SERVICE );
          clipboard.setText( et.getText() );
          Toast.makeText(this, "Copied " + et.getText(), Toast.LENGTH_SHORT).show();
        // } else {
        //   android.content.ClipboardManager clipboard =
        //     (android.content.ClipboardManager)getSystemService( CLIPBOARD_SERVICE );
        //   android.content.ClipData clip = android.content.ClipData.newPlainText("COPY", et.getText() );
        //   clipboard.setPrimaryClip( clip );
        // }
        return true;
      }
      return false;
    }


    @Override
    public void onClick( View view )
    {
      if ( view.getId() == R.id.from_crs ) {
        new CRSListDialog( this, this, mCRSmanager, TYPE_FROM ).show();
      } else if ( view.getId() == R.id.to_crs ) {
        new CRSListDialog( this, this, mCRSmanager, TYPE_TO ).show();
      // } else if ( view.getId() == R.id.from_btn ) {
      //   new CRSEditDialog( this, this, mFromCRS, TYPE_FROM ).show();
      // } else if ( view.getId() == R.id.to_btn ) {
      //   new CRSEditDialog( this, this, mToCRS, TYPE_TO ).show();
      } else if ( view.getId() == R.id.from2to ) {
        // convert( mFromCRS, mToCRS, mETfrom, mETto );
        if ( convert( mFromCRS, mToCRS, mETfromX, mETfromY, mETfromZ, mETtoX, mETtoY, mETtoZ ) ) {
          if ( mOnResult ) {
            Intent result = new Intent();
            if ( mResultCode == RESULT_OK ) {
              result.putExtra( "longitude", mResultLong );
              result.putExtra( "latitude",  mResultLat );
              result.putExtra( "altitude",  mResultAlt );
              result.putExtra( "decimals",  mResultDecimals );
              result.putExtra( "cs_to", mToCRS );
            }
            setResult( mResultCode, result );
            finish();
          }
        } else {
          if ( ! mOnResult ) {
            Toast.makeText(this, "Failed coordinate conversion", Toast.LENGTH_SHORT );
          }
        }
      } else if ( view.getId() == R.id.to2from ) {
        if ( convert( mToCRS, mFromCRS, mETtoX, mETtoY, mETtoZ, mETfromX, mETfromY, mETfromZ ) ) {
          // nothing
        } else {
          Toast.makeText(this, "Failed coordinate conversion", Toast.LENGTH_SHORT );
        }
      } else if ( view.getId() == R.id.from2map ) {
        double[] coords = new double[3];
        if ( convert( mFromCRS, "Long-Lat", mETfromX, mETfromY, mETfromZ, coords ) ) {
          Uri uri = Uri.parse( "geo:" + coords[1] + "," + coords[0] + "?q=" + coords[1] + "," + coords[0] );
          startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
        } else {
          Toast.makeText(this, "Failed coordinate conversion", Toast.LENGTH_SHORT );
        }
      } else if ( view.getId() == R.id.to2map ) {
        double[] coords = new double[3];
        if ( convert( mToCRS, "Long-Lat", mETtoX, mETtoY, mETtoZ, coords ) ) {
          Uri uri = Uri.parse( "geo:" + coords[1] + "," + coords[0] + "?q=" + coords[1] + "," + coords[0] );
          startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
        } else {
          Toast.makeText(this, "Failed coordinate conversion", Toast.LENGTH_SHORT );
        }
      } else if ( view.getId() == R.id.mobile2from ) { // get point from Mobile Topographer
        (new MobileTopographerImportDialog( this, this )).show();
      } else if ( view.getId() == R.id.geomag ) {
        GregorianCalendar gc = new GregorianCalendar();
        int year  = gc.get( Calendar.YEAR ); 
        int month = gc.get( Calendar.MONTH ); 
        int day   = gc.get( Calendar.DAY_OF_MONTH); 
        // compute magnetic declination
        double[] c = new double[3];
        c[0] = c[1] = c[2] = 0;
        int kcf = edittext2coords( mETfromX, mETfromY, mETfromZ, c );
        boolean ok = ( kcf == 3 );
        if ( ok && ! mFromCRS.equals("Long-Lat" ) ) {
          ok = convert( mFromCRS, "Long-Lat", c );
        } 
        if ( ok ) {
          // mHGeo = mWMM.ellipsoidToGeoid( mLat, mLng, mHEll ); 
          // NOTE MAG month Jan=1
          MagElement elem = mWMM.computeMagElement( c[1], c[0], c[2], year, month+1, day );
          StringWriter sw = new StringWriter();
          PrintWriter  pw = new PrintWriter( sw );
          pw.format( Locale.US, "%.3f", elem.Decl );
          mETdeclination.setText( sw.getBuffer().toString() );
          // Log.v("Proj4", "Coords " + c[0] + " " + c[1] + " " + c[2] + " Declination " + elem.Decl );
        } else {
          Toast.makeText(this, "Failed Magnetic Declination at " + year + "/" + month + "/" + day , Toast.LENGTH_SHORT );
        }
      }
    }

    int edittext2coords( EditText et, double[] c )
    {
      String coords_str = et.getText().toString().trim();
      String[] vals = coords_str.split(" ");
      c[2] = 0.0;
      int kc = 0;
      for (int k =0; k<vals.length; ++k ) {
        if ( vals[k].length() > 0 ) {
          try { 
            if ( isDDMMSS(vals[k]) ) vals[k] = switchUnits( vals[k] );
            c[kc] = Double.parseDouble( vals[k] );
            ++kc;
          } catch ( NumberFormatException nfe ) {
            // nothing;
          }
          if ( kc >= 3 ) break;
        }
      }
      return kc;
    }
 
    int edittext2coords( EditText etx, EditText ety, EditText etz, double[] c )
    {
      if ( etx.getText() == null ) return 0;
      if ( ety.getText() == null ) return 0;
      String coordx = etx.getText().toString().trim();
      String coordy = ety.getText().toString().trim();
      int kc = 3;
      try { 
        if ( etz.getText() == null ) {
          kc = 2;
          c[2] = 0.0;
        } else {
          c[2] = Double.parseDouble( etz.getText().toString() );
        }
        if ( isDDMMSS(coordx) ) coordx = switchUnits( coordx );
        if ( isDDMMSS(coordy) ) coordy = switchUnits( coordy );
        c[0] = Double.parseDouble( coordx );
        c[1] = Double.parseDouble( coordy );
      } catch ( NumberFormatException nfe ) {
        return 0;
      }
      return kc;
    }
  
    String coords2text( double[] c, int kc, int d )
    {
      String fmt2 = "%." + d + "f %." + d + "f";
      String fmt3 = fmt2 + " %.2f";                // format-3
      StringWriter sw = new StringWriter();
      PrintWriter  pw = new PrintWriter( sw );
      if ( kc == 2 ) {
        pw.format(Locale.US, fmt2, c[0], c[1] );
      } else {
        pw.format(Locale.US, fmt3, c[0], c[1], c[2] );
      }
      return sw.getBuffer().toString();
    }

    String coord2text( double c, int d )
    {
      String fmt = "%." + d + "f";
      StringWriter sw = new StringWriter();
      PrintWriter  pw = new PrintWriter( sw );
      pw.format(Locale.US, fmt, c );
      return sw.getBuffer().toString();
    }

    private PJ getCrsPJ( String crs )
    {
      PJ ret = null;
      String desc = mCRSmanager.getDescription( crs );
      if ( desc == null ) return null;
      try {
        ret = new PJ( desc );
      } catch (IllegalArgumentException e ) {
        Toast.makeText(this, "Bad source CRS definition " + e.getMessage(), Toast.LENGTH_SHORT );
        return null;
      }
      return ret;
    }

    // coords[] must have been initialized with values in fCrs
    boolean convert( String fCrs, String tCrs, double coords[] )
    {
      PJ from_pj = getCrsPJ( fCrs );
      PJ to_pj   = getCrsPJ( tCrs );
      if ( from_pj == null || to_pj == null ) return false;

      int d = mCRSmanager.getDigits( tCrs );
      String res = doConvert( from_pj, to_pj, d, 3, coords );
      return true;
    }

    boolean convert( String fCrs, String tCrs, EditText fromX, EditText fromY, EditText fromZ,
                                            double coords[] )
    {
      PJ from_pj = getCrsPJ( fCrs );
      PJ to_pj   = getCrsPJ( tCrs );
      if ( from_pj == null || to_pj == null ) return false;

      int d = mCRSmanager.getDigits( tCrs );
      int kc = edittext2coords( fromX, fromY, fromZ, coords );
      setResultValues( coords, kc );
      String res = doConvert( from_pj, to_pj, d, kc, coords );
      setResultValues( coords, kc );
      return true;
    }

    boolean convert( String fCrs, String tCrs, EditText fromX, EditText fromY, EditText fromZ,
                                            EditText toX, EditText toY, EditText toZ )
    {
      PJ from_pj = getCrsPJ( fCrs );
      PJ to_pj   = getCrsPJ( tCrs );
      if ( from_pj == null || to_pj == null ) return false;

      int d = mCRSmanager.getDigits( tCrs );
      double[] c = new double[3];
      int kc = edittext2coords( fromX, fromY, fromZ, c );
      // Log.v(TAG, "from coords " + c[0] + " " + c[1] + " " + c[2] + " kc " + kc );
      setResultValues( c, kc );

      String res = doConvert( from_pj, to_pj, d, kc, c );
      setResultValues( c, kc );

      toX.setText( coord2text(c[0], d) );
      toY.setText( coord2text(c[1], d) );
      toZ.setText( coord2text(c[2], 0) );
      return true;
    }

    boolean convert( String fCrs, String tCrs, EditText from, EditText to )
    {
      PJ from_pj = getCrsPJ( fCrs );
      PJ to_pj   = getCrsPJ( tCrs );
      if ( from_pj == null || to_pj == null ) return false;
      
      mResultDecimals = mCRSmanager.getDigits( tCrs );
      // Log.v( TAG, "convert " + fCrs + " -> " + tCrs + ": " + coords_str);
      
      double[] c = new double[3];
      int kc = edittext2coords( from, c );
      setResultValues( c, kc );

      String res = doConvert( from_pj, to_pj, mResultDecimals, kc, c );
      setResultValues( c, kc );
      return true;
    }

    void setResultValues( double[] c, int kc )
    {
      mResultLong = c[0];
      mResultLat  = c[1];
      mResultAlt  = ( kc == 2 )? 0.0 : c[2];
    }
     

    String doConvert( PJ from_pj, PJ to_pj, int d, int kc, double[] c )
    {
      String res = "";
      if ( kc >= 2 ) {
        // Log.v( TAG, "FROM " + c[0] + " " + c[1] + " " + c[2] );
        try {
          from_pj.transform( to_pj, (kc>3)?3:kc, c, 0, 1 );
          res = coords2text( c, kc, d );
          // Log.v( TAG, "TO " + c[0] + " " + c[1] + " " + c[2] );
        } catch (PJException pje ) {
          // String error = from_pj.getLastError();
          Toast.makeText(this, "Failed transform", Toast.LENGTH_SHORT );
        }
      } else {
        Toast.makeText(this, "Too few coords ("+kc+")", Toast.LENGTH_SHORT );
      }
      return res;
    }

  /* ============================================================== */
  // MENU

   @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    super.onCreateOptionsMenu( menu );

    mMIexport  = menu.add( R.string.menu_export );
    mMIcountry = menu.add( R.string.menu_country );
    // mMIoptions = menu.add( R.string.menu_options );
    mMIhelp = menu.add( R.string.menu_help );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if ( item == mMIhelp ) { // HELP DIALOG
      new Proj4HelpDialog(this).show();
    } else if ( item == mMIexport ) {  // EXPORT
      (new Proj4ExportDialog( this, this )).show();
    } else if ( item == mMIcountry ) {  // SET COUNTRY
      (new Proj4CountryDialog( this, this, mCRSmanager )).show();

    // } else if ( item == mMIoptions ) { // OPTIONS DIALOG
    //   Intent optionsIntent = new Intent( this, Proj4Preferences.class );
    //   startActivity( optionsIntent );
    }
    return true;
  }

  void exportToFile( String filename )
  {
    // Log.v("Proj4", "export to file <" + filename + ">" );
    // export coords to file
    double[] c = new double[3];
    StringWriter sw = new StringWriter();
    PrintWriter pw  = new PrintWriter(sw);
    c[0] = c[1] = c[2] = 0;
    int kcf = edittext2coords( mETfromX, mETfromY, mETfromZ, c );
    if ( kcf >= 2 ) {
      pw.format(Locale.US, "From CRS: %s\n", mFromCRS );
      pw.format(Locale.US, "%f %f %f\n", c[0], c[1], c[2] );
    }
    c[0] = c[1] = c[2] = 0;
    int kct = edittext2coords( mETtoX, mETtoY, mETtoZ, c );
    if ( kct >= 2 ) {
      pw.format(Locale.US, "To CRS: %s\n", mToCRS );
      pw.format(Locale.US, "%f %f %f\n", c[0], c[1], c[2] );
    }
    if ( kcf >= 2 || kct >= 2 ) {
      try {
        FileWriter fw = new FileWriter( filename, true );
        BufferedWriter bw = new BufferedWriter( fw );
        bw.write( sw.getBuffer().toString() );
        bw.flush();
        bw.close();
        Toast.makeText(this, "written file " + filename, Toast.LENGTH_SHORT );
      } catch ( IOException e ) {
        Toast.makeText(this, "I/O error " + e.getMessage(), Toast.LENGTH_SHORT );
      } 
    } else {
      Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT );
    }
  }

  /* ================================================================ */
  // PREFERENCES

  @Override
  public void onSharedPreferenceChanged( SharedPreferences prefs, String k )
  {
    //   if ( k.equals( "FROM_CRS" ) ) {
    //     mBTfromcrs.setText( prefs.getString( k, "" ) );
    //   } else if ( k.equals( "TO_CRS" ) ) {
    //     mBTtocrs.setText( prefs.getString( k, "" ) );
    //   }
    // if ( k.equals( "UNITS_ANGLE" ) ) {
    //   mUnitsAngle = prefs.getString( "UNITS_ANGLE", "degree" ); 
    //   // "degree" "ddmmss"
    // }
  }

  // to be called by the Proj4CountryDialog
  // country = two-char country iso-code
  void setCountry( String country )
  {
    if ( country != null && ! country.equals( mCountry ) ) {
      mCountry = country;
      setCRSmap();
      mCRSmanager.setCountry( mCountry );
      mCRSmanager.loadCRS();

      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString( "COUNTRY", mCountry );
      editor.commit();
    }
  }
    

  private void getCRSprefs()
  {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
    mFromCRS = prefs.getString( "FROM_CRS", "Long-Lat" );
    mToCRS   = prefs.getString( "TO_CRS", "UTM32N" );
    mCountry = prefs.getString( "COUNTRY", "it" );
    mBTfromcrs.setText( mFromCRS );
    mBTtocrs.setText( mToCRS );
  }

  private void setCRSprefs()
  {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( this );
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString( "FROM_CRS", mFromCRS );
    editor.putString( "TO_CRS", mToCRS );
    editor.commit();
  }

  void setMobileTopographerPoint( double mLng, double mLat, double mHGeo )
  {
    double[] c = new double[3];
    c[0] = mLng;
    c[1] = mLat;
    c[2] = mHGeo;
    boolean ok = true;
    if ( ! mFromCRS.equals("Long-Lat" ) ) {
      ok = convert( mFromCRS, "Long-Lat", c );
    }
    if ( ok ) {
      int d = mCRSmanager.getDigits( mFromCRS );
      mETfromX.setText( coord2text( c[0], d ) );
      mETfromY.setText( coord2text( c[1], d ) );
      mETfromZ.setText( coord2text( c[2], 0 ) );
    } 
  }

}
