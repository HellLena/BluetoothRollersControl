package com.example.BluetoothRollersControl;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.bluetooth.*;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MyActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        BluetoothAdapter bluetooth= BluetoothAdapter.getDefaultAdapter();

        if(bluetooth!=null){
            // ���������� ������������ Bluetooth.

            if (!bluetooth.isEnabled()){
                // Bluetooth ��������. ��������� ������������ �������� ���.
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            // ���������� �� ������������ Bluetooth.
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Your device doesn't support Bluetooth!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    // ����� Bluetooth ���������
    public void devicesSearch(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Search for devices!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
