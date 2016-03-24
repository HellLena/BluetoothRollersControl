package com.example.BluetoothRollersControl;

/**
 * Created by Stargazer on 07.03.2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

public class RollerControlActivity extends Activity {

    TrippleStateButton btnBottomLights, btnHeadLights, btnTailLights;
    Button btnDisconnect, btnTest;
    SeekBar brightnessBar;
    TextView brightnessValue;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.controls);

        //call the widgets
        btnBottomLights = (TrippleStateButton)findViewById(R.id.btnBottomLights);
        btnBottomLights.disableAnimation(); // disable blinking mode in bottomLights button
        btnHeadLights = (TrippleStateButton)findViewById(R.id.btnHeadLights);
        btnTailLights = (TrippleStateButton)findViewById(R.id.btnTailLights);
        btnDisconnect = (Button)findViewById(R.id.btnDisconnect);
        btnTest = (Button)findViewById(R.id.btnTest);
        brightnessBar = (SeekBar)findViewById(R.id.brightnessBar);
        brightnessValue = (TextView)findViewById(R.id.brightnessValue);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        btnBottomLights.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeLEDMode(btnBottomLights.getState(), (byte) 0x80);
            }
        });

        btnHeadLights.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeLEDMode(btnHeadLights.getState(), (byte) 0x85);
            }
        });

        btnTailLights.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeLEDMode(btnTailLights.getState(), (byte) 0x87);
            }
        });

        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    brightnessValue.setText(String.valueOf(progress));
                    sendBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect(); //close connection
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendColor();
            }
        });
    }

    private void changeLEDMode(int state, byte reg_address)
    {
        if (btSocket!=null)
        {
            try
            {
                byte b[] = {0x01,0x10,0x00,0x00,0x00,0x01,0x02,0x00,0x00,0x00,0x00};
                b[3] = reg_address;
                switch(state){
                    case 0: b[8] = 0x00;
                        break;
                    case 1: b[8] = 0x01;
                        break;
                    case 2: b[8] = 0x02;   // blinking mode
                        break;
                    default: b[8] = 0x00;
                        break;
                }
                int crc = ModRTU_CRC(b, b.length - 2);
                b[9] = (byte)(crc & 0xFF);
                b[10] = (byte)((crc >> 8) & 0xFF);
                btSocket.getOutputStream().write(b);
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    // Compute the MODBUS RTU CRC
    private static int ModRTU_CRC(byte[] buf, int len)
    {
        int crc = 0xFFFF;

        for (int pos = 0; pos < len; pos++) {
            crc ^= (int)buf[pos] & 0xFF;   // XOR byte into least sig. byte of crc

            for (int i = 8; i != 0; i--) {    // Loop over each bit
                if ((crc & 0x0001) != 0) {      // If the LSB is set
                    crc >>= 1;                    // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                }
                else                            // Else LSB is not set
                    crc >>= 1;                    // Just shift right
            }
        }
        // Note, this number has low and high bytes swapped, so use it accordingly (or swap bytes)
        return crc;
    }

    private void sendColor()
    {
        if (btSocket!=null)
        {
            try
            {
                byte b[] = {0x01,0x10,0x00,(byte)0x81,0x00,0x03,0x06,0x00,0x00,0x00,(byte)0xFF,0x00,0x00,0x00,0x00};
                int crc = ModRTU_CRC(b, b.length - 2);
                b[13] = (byte)(crc & 0xFF);
                b[14] = (byte)((crc >> 8) & 0xFF);
                btSocket.getOutputStream().write(b);
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendBrightness(int brightness){
        if (btSocket!=null)
        {
            try
            {
                byte b[] = {0x01,0x10,0x00,(byte)0x86,0x00,0x01,0x02,0x00,0x00,0x00,0x00}; // 88
                b[7] = (byte)((brightness >> 8) & 0xFF);
                b[8] = (byte)(brightness & 0xFF);
                int crc = ModRTU_CRC(b, b.length - 2);
                b[9] = (byte)(crc & 0xFF);
                b[10] = (byte)((crc >> 8) & 0xFF);
                btSocket.getOutputStream().write(b);
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
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

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(RollerControlActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

