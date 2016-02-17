package org.dolphinziyo.carcontroller;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends ListActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private final int handlerState = 0;
    private Button btnMoveForward, btnMoveBackward, btnMoveLeft, btnMoveRight;
    private Button btToggle;
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket btSocket;
    private ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();
    private ConnectAsyncTask connectAsyncTask;
    private boolean btConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btConnected = false;

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        setListAdapter(mArrayAdapter);

        // Instance AsyncTask
        connectAsyncTask = new ConnectAsyncTask();

        //Get Bluettoth Adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check smartphone support Bluetooth
        if(mBluetoothAdapter == null){
            //Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Bluetooth no soportado", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check Bluetooth enabled
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        // Queryng paried devices
        Set<BluetoothDevice> pariedDevices = mBluetoothAdapter.getBondedDevices();
        if(pariedDevices.size() > 0){
            for(BluetoothDevice device : pariedDevices){
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btDeviceArray.add(device);
            }
        }

        btnMoveForward = (Button)findViewById(R.id.btn_move_forward);
        btnMoveBackward = (Button)findViewById(R.id.btn_move_backward);
        btnMoveLeft = (Button)findViewById(R.id.btn_move_left);
        btnMoveRight = (Button)findViewById(R.id.btn_move_right);
        btnMoveForward.setOnClickListener(this);
        btnMoveBackward.setOnClickListener(this);
        btnMoveLeft.setOnClickListener(this);
        btnMoveRight.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == btnMoveForward){
            OutputStream mmOutStream = null;
            try {
                if(btSocket.isConnected()){
                    mmOutStream = btSocket.getOutputStream();
                    String strAEnviar = "F";
                    mmOutStream.write(strAEnviar.getBytes());
                }

            } catch (IOException e) {
                System.out.println("Error enviando comando: " + mmOutStream.toString());
            }
        }else if(v == btnMoveBackward){
            OutputStream mmOutStream = null;
            try {
                if(btSocket.isConnected()){
                    mmOutStream = btSocket.getOutputStream();
                    String strAEnviar = "B";
                    mmOutStream.write(strAEnviar.getBytes());
                }

            } catch (IOException e) {
                System.out.println("Error enviando comando: " + mmOutStream.toString());
            }
        }else if(v == btnMoveLeft){
            OutputStream mmOutStream = null;
            try {
                if(btSocket.isConnected()){
                    mmOutStream = btSocket.getOutputStream();
                    String strAEnviar = "L";
                    mmOutStream.write(strAEnviar.getBytes());
                }

            } catch (IOException e) {
                System.out.println("Error enviando comando: " + mmOutStream.toString());
            }
        }else if(v == btnMoveRight){
            OutputStream mmOutStream = null;
            try {
                if(btSocket.isConnected()){
                    mmOutStream = btSocket.getOutputStream();
                    String strAEnviar = "R";
                    mmOutStream.write(strAEnviar.getBytes());
                }

            } catch (IOException e) {
                System.out.println("Error enviando comando: " + mmOutStream.toString());
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        BluetoothDevice device = btDeviceArray.get(position);
        if(!btConnected){
            connectAsyncTask.execute(device);
        }else{
            Toast.makeText(this, "Ya conectado", Toast.LENGTH_SHORT).show();
        }

    }

    private class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {

        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... device) {

            mmDevice = device[0];

            try {

                String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
                mmSocket.connect();

            } catch (Exception e) {
            }

            return mmSocket;
        }

        @Override
        protected void onPostExecute(BluetoothSocket result) {
            btConnected = true;
            btSocket = result;
            btnMoveForward.setEnabled(true);
            btnMoveBackward.setEnabled(true);
            btnMoveLeft.setEnabled(true);
            btnMoveRight.setEnabled(true);
        }
    }
}
