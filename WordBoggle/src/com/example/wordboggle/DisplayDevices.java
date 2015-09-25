/*
 * DisplayDevices.java
 * CS 454
 * Group 2
 */

package com.example.wordboggle;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/*
 * 
 */
public class DisplayDevices extends Activity{
	// Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter btAdapter;
    private ArrayAdapter<String> pairedDevicesArray;
    private ArrayAdapter<String> newDevicesArray;
	
	
    /*
     * Behavior when activity is created
     */
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.devices_list);
        
        
        //Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED);
        
        pairedDevicesArray = new ArrayAdapter<String>(this, R.layout.device);
        newDevicesArray = new ArrayAdapter<String>(this, R.layout.device);
        
        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArray);
        pairedListView.setOnItemClickListener(myDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(newDevicesArray);
        newDevicesListView.setOnItemClickListener(myDeviceClickListener);

        
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(myReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(myReceiver, filter);
        
        // Get the local Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

     	// If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArray.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "No Devices";
            pairedDevicesArray.add(noDevices);
        }
        
    }
    
	
	/*
	 * Behavior when activity is destroyed
	 */
	protected void onDestroy() {
        super.onDestroy();
        
        // Make sure we're not doing discovery anymore
        if (btAdapter != null) {
            btAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(myReceiver);
	}
	
	
	/*
	 * the listern that scans for all nearby bluetooth devices
	 * when the scan button is pressed
	 */
	public void onClickScan(View view){
		doDiscovery();
        view.setVisibility(View.GONE);
        setResult(Activity.RESULT_CANCELED);
	}
	
	
	/*
	 * the listern that exits out of the activity without 
	 * connecting to a bluetooth device when the exit button is pressed
	 */
	public void onClickExit(View view){
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
	
	/*
	 *  The on-click listener for all devices in the ListViews
	 */
    private OnItemClickListener myDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            btAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
	
	
	/*
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle("");
	
        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        btAdapter.startDiscovery();
    }
    
	
	/* 
	 * The BroadcastReceiver that listens for discovered devices and
     * changes the title when discovery is finished
     */
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDevicesArray.add(device.getName() + "\n" + device.getAddress());
                }
                
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("Select Device");
                if (newDevicesArray.getCount() == 0) {
                    String noDevices = "No Devices Found";
                    newDevicesArray.add(noDevices);
                }
            }
        }
    };
}
