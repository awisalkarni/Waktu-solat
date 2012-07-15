package com.awis.waktusolat;

import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QiblaActivity extends SherlockActivity{
	private SensorManager sensorManager;
//	private ImageView compass;
	private Sensor mSensor;
	private LocationManager mlocManager;
	private LocationListener mlocListener;
	private TextView txtInfo, txtgyroX;
	private float[] sValues;
	//	private Float gyrox,gyroy,gyroz;
	private double Q;
	private double latitude,longitude;
//	private int screenWidth, screenHeight;
    private static final String TAG = "Compass";
	private LinearLayout compassLayout;
    private SampleView mView;
    private GoogleAnalyticsTracker tracker;
//	private Timer timer;
    
    
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Waktusolat.THEME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tracker = WaktuSolatNew.tracker;
        setContentView(R.layout.qibla);
        setTitle("Arah Qiblat(beta)");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
       
        mView = new SampleView(this);
        //MyLocationListener mls = new MyLocationListener();
        getlastknownGPS();
        setupLoc();
        initUI();
        calc2();
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
        
//        if (Integer.valueOf(android.os.Build.VERSION.SDK) <= 10){
//   		 screenWidth = display.getWidth();
//   		 screenHeight = display.getHeight();
//        }else {
//   		 Point size = new Point();
//   		 display.getSize(size);
//   		 screenWidth = size.x;
//   		screenHeight = size.y;
//   	 }
    }
    
    

	public void rotateQibla(){
    	
    }
    
   
    
    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            //if (false) Log.d(TAG,"sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            sValues = event.values;
            //txtgyroX.setText("X: "+Float.toString(sValues[0]));
            //txtgyroY.setText("Y: "+Float.toString(sValues[1]));
            //txtgyroZ.setText("Z: "+Float.toString(sValues[2]));
            
//            txtgyroY.setText("");
//            txtgyroZ.setText("");
            txtInfo.setText("latitude: "+latitude+"\n"+"longitude: "+longitude+"\n"+"Kiblat: "+Double.toString(Q));
          
            
            if (mView != null) {
                mView.invalidate();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    
    
    public void setupLoc(){  	
    	mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	mlocListener = new MyLocationListener();
    	mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
    	
    }
    
    private void getlastknownGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location loc = null;
        
        for (int i=providers.size()-1; i>=0; i--) {
                loc = lm.getLastKnownLocation(providers.get(i));
                if (loc != null) break;
        }
        if (loc != null) {
        	getLat(loc.getLatitude());
        	getLong(loc.getLongitude());
        }
    }
    
    
    
    public void initUI(){
//    	compass = (ImageView) findViewById(R.id.compass);
    	txtInfo = (TextView) findViewById(R.id.txtInfo);
    	txtgyroX = (TextView) findViewById(R.id.txtgyroX);
//    	txtgyroY = (TextView) findViewById(R.id.txtgyroY);
//    	txtgyroZ = (TextView) findViewById(R.id.txtgyroZ);
    	txtgyroX.setText("Arah kiblat Malaysia sepatutnya 29X.XXX\njika anggaran terlalu jauh, sila lock GPS anda duhulu");
    	compassLayout = (LinearLayout)findViewById(R.id.compassLayout);
    	compassLayout.addView(mView);

    }
    
    public void getLat(double latitude){
    	this.latitude = latitude;
    	
    }
    
    public void getLong(double longitude){
    	this.longitude = longitude;
    }

    
    public void calc2(){
    	double PI = Math.PI;
    	double phiK = 21.4*PI/180.0;
    	double lambdaK = 39.8*PI/180.0;
    	double phi = latitude*PI/180.0;
    	double lambda = longitude*PI/180.0;
    	//double phi = 2.52966*PI/180.0;
    	//double lambda = 102.395965*PI/180.0;
    	double psi = 180.0/PI*Math.atan2(Math.sin(lambdaK-lambda),Math.cos(phi)*Math.tan(phiK)-Math.sin(phi)*Math.cos(lambdaK-lambda));
    	Q = 360+psi;
    	}
    
    
    @Override
    protected void onResume() {
       super.onResume();
       sensorManager.registerListener(mListener, mSensor,SensorManager.SENSOR_DELAY_GAME);
       tracker.trackPageView("/waktuSolat_qibla");
       tracker.dispatch();
    }

	@Override
	protected void onPause(){
		super.onPause();
		mlocManager.removeUpdates(mlocListener);
	}

    @Override
    protected void onStop() {
       super.onStop();
       sensorManager.unregisterListener(mListener);
       mlocManager.removeUpdates(mlocListener);
       finish();
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	mlocManager.removeUpdates(mlocListener);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
	public void onBackPressed() {
	    super.onBackPressed();
	    this.finish();
	    finish();
	    overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
	}
    
    private class SampleView extends View {
        private Paint   mPaint = new Paint();
//        private Path    mPath = new Path();
//        private Path ARROW_PATH = new Path();
        private Path path = new Path();
        private boolean mAnimate;

        public SampleView(Context context) {
            super(context);

            // Construct a wedge-shaped path
//            mPath.moveTo(0, -50);
//            mPath.lineTo(-20, 60);
//            mPath.lineTo(0, 50);
//            mPath.lineTo(20, 60);
//            mPath.close();
//            
//            
//            ARROW_PATH.setLastPoint(0, 17);
//            
//            ARROW_PATH.lineTo(-6, 5);
//            ARROW_PATH.lineTo(-2, 6);
//            ARROW_PATH.lineTo(-3, -16);
//            ARROW_PATH.lineTo(3, -16);
//            ARROW_PATH.lineTo(2, 6);
//            ARROW_PATH.lineTo(6, 5);
//
//            ARROW_PATH.close();

            path.moveTo(0, -50);
			path.lineTo(10, 0);
			path.lineTo(-10, 0);
			path.close();
        }
        @Override protected void onDraw(Canvas canvas) {
            Paint paint = mPaint;
//            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
//            canvas.drawColor(Color.TRANSPARENT);
            
        	 
        	 
//    		int width = b.getWidth();
//            int height = b.getHeight();
//            double newSize = Math.floor(canvas.getWidth()*90/100);
          
//            float xScale = ((float) newSize) / width;
//            float yScale = ((float) newSize) / height;
//            float scale = (xScale <= yScale) ? xScale : yScale;

//            Matrix matrix = new Matrix();
//            if (scale < xScale){
//            	matrix.postScale(xScale, yScale);
//            }else {
//            	matrix.postScale(scale, scale);
//            }
            
//            Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0,width, height, matrix, true);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            int w = canvas.getWidth();
            int h = canvas.getHeight();
//            txtInfo.setText(Integer.toString(w)+" "+Integer.toString(h));
            int cx = w / 2;
            int cy = h / 2;

            canvas.translate(cx, cy);
            if (sValues != null) {
                canvas.rotate(-sValues[0]+Float.parseFloat(Double.toString(Q)));
                //txtTest.setText(Float.toString(sValues[0]));
            }
//            int x=0; int y=0;
//            canvas.drawBitmap(resizedBitmap,x - resizedBitmap.getWidth() / 2, y - resizedBitmap.getHeight(), null);
            canvas.drawPath(path, mPaint);
        }

        @SuppressWarnings("unused")
		@Override
        protected void onAttachedToWindow() {
            mAnimate = true;
            if (false) Log.d(TAG, "onAttachedToWindow. mAnimate=" + mAnimate);
            super.onAttachedToWindow();
        }

        @SuppressWarnings("unused")
		@Override
        protected void onDetachedFromWindow() {
            mAnimate = false;
            if (false) Log.d(TAG, "onDetachedFromWindow. mAnimate=" + mAnimate);
            super.onDetachedFromWindow();
        }
    }
    
    /* Class My Location Listener */
    class MyLocationListener implements LocationListener{
    	//QiblaActivity gt = new QiblaActivity();
    
    @Override
    public void onLocationChanged(Location loc){
    	getLat(loc.getLatitude());
    	getLong(loc.getLongitude());
    	calc2();
    }
    
    
    
    @Override
    public void onProviderDisabled(String provider){}
    @Override
    public void onProviderEnabled(String provider){}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}
    }/* End of Class MyLocationListener */
    
    
}
