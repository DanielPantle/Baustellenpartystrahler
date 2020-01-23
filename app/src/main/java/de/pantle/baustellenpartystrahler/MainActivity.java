package de.pantle.baustellenpartystrahler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import java.util.Collection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;

public class MainActivity extends AppCompatActivity {
    BluetoothManager bluetoothManager;
    SimpleBluetoothDeviceInterface device;

    TextView label;
    Button buttonMode0;
    Button buttonMode1;
    Button buttonMode2;
    Button buttonFColor;
    Button buttonBColor;
    SeekBar seekBarSpeed;
    SeekBar seekBarSpeed2;
    SeekBar seekBarBrightness;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.label);
        buttonMode0 = findViewById(R.id.buttonMode0);
        buttonMode1 = findViewById(R.id.buttonMode1);
        buttonMode2 = findViewById(R.id.buttonMode2);
        buttonFColor = findViewById(R.id.buttonFColor);
        buttonBColor = findViewById(R.id.buttonBColor);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        seekBarSpeed2 = findViewById(R.id.seekBarSpeed2);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);

        label.setText("noch nicht verbunden");

        buttonMode0.setOnClickListener((View v) -> {
            sendMessage("mode:0");
        });
        buttonMode1.setOnClickListener((View v) -> {
            sendMessage("mode:1");
        });
        buttonMode2.setOnClickListener((View v) -> {
            sendMessage("mode:2");
        });
        buttonFColor.setOnClickListener((View v) -> {
            openColorPickerDialogFColor();
        });
        buttonBColor.setOnClickListener((View v) -> {
            openColorPickerDialogBColor();
        });
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendMessage("speed:" + Integer.toHexString(seekBar.getMax() - seekBar.getProgress()));
            }
        });
        seekBarSpeed2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendMessage("speed2:" + Integer.toHexString(seekBar.getMax() - seekBar.getProgress()));
            }
        });
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendMessage("bright:" + Integer.toHexString(seekBar.getProgress()));
            }
        });

        configureBluetooth();
    }

    private void configureBluetooth() {
        bluetoothManager = BluetoothManager.getInstance();
        if(bluetoothManager == null) {
            Toast.makeText(this, "Bluetooth ist auf deinem Gerät nicht verfügbar :/", Toast.LENGTH_LONG).show();
            finish();
        }

        Collection<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevicesList();
        for(BluetoothDevice pairedDevice : pairedDevices) {
            if(pairedDevice.getName().equals("Baustellenpartystrahler")) {
                connectDevice(pairedDevice.getAddress());
                return;
            }
        }

        label.setText("Kopple erst den Baustellenpartystrahler!");
    }

    private void connectDevice(String mac) {
        bluetoothManager.openSerialDevice(mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onError);
    }

    private void sendMessage(String message) {
        if (device != null) {
            device.sendMessage(message + "\n");
        } else {
            Toast.makeText(this, "Gerät nicht verbunden.", Toast.LENGTH_LONG).show();
        }
    }


    private void openColorPickerDialogFColor() {
        new ColorPickerDialog()
                .withAlphaEnabled(false)
                .withPresets(Color.rgb(255, 156, 0), Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE)
                .withListener((@Nullable ColorPickerDialog pickerView, int color) -> {
                    sendMessage("fcolor:" + colorIntToHex(color));
                })
                .show(getSupportFragmentManager(), "colorPicker");
    }

    private void openColorPickerDialogBColor() {
        new ColorPickerDialog()
                .withAlphaEnabled(false)
                .withListener((@Nullable ColorPickerDialog pickerView, int color) -> {
                    sendMessage("bcolor:" + colorIntToHex(color));
                })
                .show(getSupportFragmentManager(), "colorPicker");
    }

    private String colorIntToHex(int color) {
        return String.format("%06X", 0xFFFFFF & color);
    }


    private void onConnected(BluetoothSerialDevice connectedDevice) {
        device = connectedDevice.toSimpleDeviceInterface();
        label.setText("Verbunden!");
    }

    private void onError(Throwable error) {
        label.setText("Leider ist ein Fehler aufgetreten!");
        bluetoothManager.close();
        Toast.makeText(this, "Ein Fehler ist aufgetreten.", Toast.LENGTH_LONG).show();
        Log.e("ERROR", error.getMessage());
    }

    @Override
    protected void onStop() {
        super.onStop();

        bluetoothManager.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.connect) {
            configureBluetooth();
        }

        return true;
    }
}
