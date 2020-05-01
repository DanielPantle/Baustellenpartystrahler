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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    TextView viewSpeed;
    Button buttonMode0;
    Button buttonMode1;
    Button buttonMode2;
    Button buttonMode3;
    Button buttonMode4;
    Button buttonFColor;
    Button buttonBColor;
    Button modus1;
    Button modus2;
    Button modus3;
    Button modus4;

    SeekBar seekBarSpeed;
    SeekBar seekBarBrightness;
  //  ListView listmodi;

    int color= Color.WHITE;
    int color2= Color.BLACK;
    int mode=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.label);
        buttonMode0 = findViewById(R.id.buttonlangweilig);
        buttonMode1 = findViewById(R.id.buttonKreis);
        buttonMode2 = findViewById(R.id.buttonBaustelle);
        buttonMode3 = findViewById(R.id.buttonRainbow);
        buttonMode4 = findViewById(R.id.buttonBlinken);
        buttonFColor = findViewById(R.id.buttonFColor);
        buttonBColor = findViewById(R.id.buttonBColor);
        modus1 = findViewById(R.id.modi1);
        modus2 = findViewById(R.id.modi2);
        modus3 = findViewById(R.id.modi3);
        modus4 = findViewById(R.id.modi4);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        viewSpeed= findViewById(R.id.textspeed);
       // listmodi = findViewById(R.id.lightmodi);

        label.setText("noch nicht verbunden");

        designchanges();

        /*ArrayAdapter adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_single_choice, modi);
        listmodi.setAdapter(adapter);
*/

        buttonMode0.setOnClickListener((View v) -> {
            mode=0;
            sendMessage("mode:" + mode);
            designchanges();
        });
        buttonMode1.setOnClickListener((View v) -> {
            mode=1;
            sendMessage("mode:" + mode);
            designchanges();
        });
        buttonMode2.setOnClickListener((View v) -> {
            mode=2;
            sendMessage("mode:" + mode);
            designchanges();
        });
        buttonMode3.setOnClickListener((View v) -> {
            mode=3;
            sendMessage("mode:" + mode);
            designchanges();
        });
        buttonMode4.setOnClickListener((View v) -> {
            mode=4;
            sendMessage("mode:" + mode);
            designchanges();
        });
        buttonFColor.setOnClickListener((View v) -> {
            openColorPickerDialogFColor();
        });
        buttonBColor.setOnClickListener((View v) -> {
            openColorPickerDialogBColor();
        });
        modus1.setOnClickListener((View v) -> {
            mode=101;
            sendMessage("mode:" + mode);
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

    private void designchanges() {
        if (mode==0) {
            viewSpeed.setVisibility(View.INVISIBLE);
            seekBarSpeed.setVisibility(View.INVISIBLE);
            buttonBColor.setVisibility(View.GONE);
            buttonMode0.setBackgroundColor(Color.parseColor("#FAFAFA"));
            modus1.setText("Langweilig");
            modus2.setText("Random");
            modus3.setText("SingleAuto");
            modus4.setText("FullAuto");
            modus1.setVisibility(View.VISIBLE);
            modus2.setVisibility(View.VISIBLE);
            modus3.setVisibility(View.VISIBLE);
            modus4.setVisibility(View.VISIBLE);
            update();
        }else {
            modus1.setVisibility(View.GONE);
            modus2.setVisibility(View.GONE);
            modus3.setVisibility(View.GONE);
            modus4.setVisibility(View.GONE);
            viewSpeed.setVisibility(View.VISIBLE);
            seekBarSpeed.setVisibility(View.VISIBLE);
            buttonMode0.setBackgroundColor(Color.parseColor("#018577"));

        }

        if (mode==1) {
            buttonBColor.setVisibility(View.VISIBLE);
            buttonMode1.setBackgroundColor(Color.parseColor("#FAFAFA"));
            seekBarSpeed.setMax(300);
            seekBarSpeed.setProgress(100);
            update();
        }else {
            seekBarSpeed.setMax(5000);
            seekBarSpeed.setProgress(1000);
            buttonMode1.setBackgroundColor(Color.parseColor("#018577"));
        }

        if (mode==2) {
            buttonBColor.setVisibility(View.GONE);
            buttonFColor.setBackgroundColor(Color.rgb(255, 155, 0));
            color= (Color.rgb(255, 155, 0));
            buttonBColor.setBackgroundColor(Color.rgb(0, 0, 0));
            color2 = (Color.rgb(0, 0, 0));
            buttonMode2.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }else {
            buttonFColor.setBackgroundColor(color);
            buttonBColor.setBackgroundColor(color2);
            buttonMode2.setBackgroundColor(Color.parseColor("#018577"));
        }

        if (mode==3) {
            buttonBColor.setVisibility(View.GONE);
            buttonFColor.setVisibility(View.GONE);
            seekBarSpeed.setMax(20000);
            seekBarSpeed.setProgress(5000);
            buttonMode3.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }else {
            buttonFColor.setVisibility(View.VISIBLE);
            buttonFColor.setBackgroundColor(color);
            buttonBColor.setBackgroundColor(color2);
            buttonMode3.setBackgroundColor(Color.parseColor("#018577"));

        }

        if (mode==4) {
            buttonBColor.setVisibility(View.VISIBLE);
            buttonMode4.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }else {
            buttonMode4.setBackgroundColor(Color.parseColor("#018577"));
        }

        //update(); gab ne fehlermeldung beim rainbow


    }

    private void update() {
        sendMessage("fcolor:" + colorIntToHex(color));
        sendMessage("bcolor:" + colorIntToHex(color2));
        sendMessage("bright:" + Integer.toHexString(seekBarBrightness.getProgress()));
        sendMessage("speed:" + Integer.toHexString(seekBarSpeed.getMax() - seekBarSpeed.getProgress()));

    }


    private void openColorPickerDialogFColor() {
        new ColorPickerDialog()
                .withColor(color)
                .withAlphaEnabled(false)
                .withPresets(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.rgb(255, 156, 0), Color.CYAN, Color.MAGENTA)
                .withListener((@Nullable ColorPickerDialog pickerView, int color) -> {
                    this.color= color;
                    buttonFColor.setBackgroundColor(color);
                    sendMessage("fcolor:" + colorIntToHex(color));
                })
                .show(getSupportFragmentManager(), "colorPicker");
    }

    private void openColorPickerDialogBColor() {
        new ColorPickerDialog()
                .withColor(color2)
                .withAlphaEnabled(false)
                .withPresets(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.rgb(255, 156, 0), Color.CYAN, Color.MAGENTA)
                .withListener((@Nullable ColorPickerDialog pickerView, int color) -> {
                    this.color2= color;
                 buttonBColor.setBackgroundColor(color2);
                    sendMessage("bcolor:" + colorIntToHex(color2));
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
