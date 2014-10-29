package com.hkapps.flashit;

/* Code originally based on the article from Java Papers */

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	/* Variables to access through out the Main Activity */
	
	private Camera camera;
    ImageButton flashlightSwitchImg;
    private boolean isFlashlightOn;
    Parameters params;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get the image from the RelativeLayout which will act as a button
        flashlightSwitchImg = (ImageButton) findViewById(R.id.flashlightSwitch);
        
        //Check if there is a flash in the phone/device on which the application is installed
        boolean isCameraFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        
        //if no flash is found then close the app, else open the app.
        if (!isCameraFlash) {
            showNoCameraAlert();
        } else {
            camera = Camera.open();
            params = camera.getParameters();
        }

        /*setting onClick listener to track the touches on the Image Button declared
         * above as flashlightSwitchImg
         */
        
        flashlightSwitchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashlightOn) {
                	//If it is already ON, then turn OFF
                    setFlashlightOff();
                } else {
                	//If it is OFF, then turn ON
                    setFlashlightOn();
                }
            }
        });
        
	}
	
	//Method to display a message that there is no Camera Flash on the device the app is installed.
	private void showNoCameraAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Error: No Camera Flash!")
                .setMessage("Camera flashlight not available in this Android device!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // close the Android app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return;
    }
	
	/* Method to switch the LIGHT ON */
    private void setFlashlightOn() {
       params = camera.getParameters();
       params.setFlashMode(Parameters.FLASH_MODE_TORCH);
       camera.setParameters(params);
       camera.startPreview();
       isFlashlightOn = true;
       flashlightSwitchImg.setImageResource(R.drawable.light_on);
    }
    
    
    /* Method to switch the LIGHT OFF */
    private void setFlashlightOff() {
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashlightOn = false;
        flashlightSwitchImg.setImageResource(R.drawable.light_off);
    }
    
    /* OnStop method of the activity */
    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
